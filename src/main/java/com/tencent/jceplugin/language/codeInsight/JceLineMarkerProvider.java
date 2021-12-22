/**
 * Tencent is pleased to support the open source community by making Tars available.
 *
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language.codeInsight;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.JceFunctionInfo;
import com.tencent.jceplugin.language.psi.JceInterfaceInfo;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import com.tencent.jceplugin.language.psi.JceNamedElement;
import com.tencent.jceplugin.language.psi.JceStructType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 标出Java接口
 */
public class JceLineMarkerProvider extends RelatedItemLineMarkerProvider {
    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
        if (!(element instanceof JceStructType || element instanceof JceInterfaceInfo)) {
            return;
        }
        String className = ((JceNamedElement) element).getName();
        if (className == null || className.isEmpty()) {
            return;
        }
        final PsiElement moduleInfo = PsiTreeUtil.findFirstParent(element, JceModuleInfo.class::isInstance);
        if (moduleInfo == null || ((JceModuleInfo) moduleInfo).getName() == null) {
            return;
        }
        @NotNull String moduleName = Objects.requireNonNull(((JceModuleInfo) moduleInfo).getName());
        if (element instanceof JceStructType) {
            //结构体，找出标记了@struct的java类
            JceStructType jceStructType = (JceStructType) element;
            PsiClass[] javaClasses = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(className,
                    GlobalSearchScope.allScope(element.getProject()));
            for (PsiClass javaClass : javaClasses) {
                if (javaClass.getQualifiedName() == null) {
                    continue;
                }
                final String packageName = StringUtil.getShortName(StringUtil.getPackageName(javaClass.getQualifiedName()));
                if (!moduleName.equalsIgnoreCase(packageName)) {
                    continue;
                }
                if (JceUtil.isJceStructClass(javaClass) && className.equals(javaClass.getName())) {
                    //是结构体
                    NavigationGutterIconBuilder<PsiElement> javaMethodBuilder =
                            NavigationGutterIconBuilder.create(AllIcons.Nodes.Class)
                                    .setTargets(javaClass.getNameIdentifier())
                                    .setTooltipText("Navigate to Java Class Declaration");

                    result.add(javaMethodBuilder.createLineMarkerInfo(Objects.requireNonNull(jceStructType.getNameIdentifier())));
                }
            }
        } else {
            JceInterfaceInfo jceInterfaceInfo = (JceInterfaceInfo) element;
            List<PsiClass> javaClasses = Stream.of(className, className + "Servant",
                            className + "Prx",
                            className + "ServantImpl",
                            className + "PrxImpl",
                            className + "Impl",
                            className + "ClientApi")
                    .map(name -> PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(name,
                            GlobalSearchScope.allScope(element.getProject())))
                    .flatMap(Stream::of)
                    .collect(Collectors.toList());
            for (PsiClass javaClass : javaClasses) {
                String name = javaClass.getQualifiedName();
                if (name == null) {
                    continue;
                }
                name = StringUtil.getShortName(name);
                if (javaClass.isInterface() || javaClass.isEnum()) {
                    //对于接口和枚举，要区分module，实现类就不需要
                    final String packageName = StringUtil.getShortName(StringUtil.getPackageName(javaClass.getQualifiedName()));
                    if (!moduleName.equalsIgnoreCase(packageName)) {
                        continue;
                    }
                }
                boolean isJceClass = JceUtil.isJceClass(javaClass);
                boolean isImplement = !javaClass.isInterface();
                if (!isJceClass) {
                    //看看实现接口有没有jce注解
                    PsiReferenceList implementsList = javaClass.getImplementsList();
                    if (implementsList != null && implementsList.getReferenceElements().length > 0) {
                        //看看是不是实现类
                        for (PsiJavaCodeReferenceElement referenceElement : implementsList.getReferenceElements()) {
                            PsiElement resolve = referenceElement.resolve();
                            if (resolve instanceof PsiClass && ((PsiClass) resolve).getName() != null) {
                                isJceClass = JceUtil.isJceClass((PsiClass) resolve);
                                if (isJceClass) {
                                    name = ((PsiClass) resolve).getName();
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!isJceClass || name == null) {
                    continue;
                }
                name = JceUtil.getJceServiceNameFromClassName(name);
                if (className.equals(name)) {
                    //是接口
                    Icon icon = isImplement ? AllIcons.Nodes.Class : AllIcons.Nodes.Interface;
                    NavigationGutterIconBuilder<PsiElement> javaClassBuilder =
                            NavigationGutterIconBuilder.create(icon)
                                    .setTargets(javaClass.getNameIdentifier())
                                    .setTooltipText("Navigate to Java " + (isImplement ? "implement class" : "interface") + " declaration");
                    result.add(javaClassBuilder.createLineMarkerInfo(Objects.requireNonNull(jceInterfaceInfo.getNameIdentifier())));
                    //标注方法
                    Icon methodIcon = isImplement ? AllIcons.Gutter.ImplementedMethod : AllIcons.Nodes.Interface;
                    for (PsiMethod javaMethod : javaClass.getMethods()) {
                        String methodName = JceUtil.getFunctionName(javaMethod);
                        if (methodName.isEmpty()) {
                            continue;
                        }
                        JceFunctionInfo jceFunctionInfo = JceUtil.findFunction(jceInterfaceInfo, methodName);
                        if (jceFunctionInfo != null) {
                            //找到了method
                            NavigationGutterIconBuilder<PsiElement> javaMethodBuilder =
                                    NavigationGutterIconBuilder.create(methodIcon)
                                            .setTargets(javaMethod.getNameIdentifier())
                                            .setTooltipText("Navigate to Java method declaration");
                            result.add(javaMethodBuilder.createLineMarkerInfo(Objects.requireNonNull(jceFunctionInfo.getNameIdentifier())));
                        }
                    }
                }
            }
        }
    }
}
