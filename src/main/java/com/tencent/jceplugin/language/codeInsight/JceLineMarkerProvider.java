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
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;

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
        final PsiElement moduleInfo = PsiTreeUtil.findFirstParent(element, e -> e instanceof JceModuleInfo);
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
                if (javaClass.hasAnnotation("com.qq.cloud.taf.common.annotation.JceStruct")
                        && className.equals(javaClass.getName())) {
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
            PsiClass[] shortJavaClasses = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(className,
                    GlobalSearchScope.allScope(element.getProject()));
            PsiClass[] servantJavaClasses = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(className + "Servant",
                    GlobalSearchScope.allScope(element.getProject()));
            PsiClass[] prxJavaClasses = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(className + "Prx",
                    GlobalSearchScope.allScope(element.getProject()));
            PsiClass[] servantImplJavaClasses = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(className + "ServantImpl",
                    GlobalSearchScope.allScope(element.getProject()));
            PsiClass[] prxImplJavaClasses = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(className + "PrxImpl",
                    GlobalSearchScope.allScope(element.getProject()));
            PsiClass[] implJavaClasses = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(className + "Impl",
                    GlobalSearchScope.allScope(element.getProject()));
            List<PsiClass> javaClasses = new ArrayList<>();
            javaClasses.addAll(Arrays.asList(shortJavaClasses));
            javaClasses.addAll(Arrays.asList(servantJavaClasses));
            javaClasses.addAll(Arrays.asList(prxJavaClasses));
            javaClasses.addAll(Arrays.asList(servantImplJavaClasses));
            javaClasses.addAll(Arrays.asList(prxImplJavaClasses));
            javaClasses.addAll(Arrays.asList(implJavaClasses));
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
                boolean isJceClass = javaClass.hasAnnotation("com.qq.cloud.taf.common.annotation.JceService");
                boolean isImplement = !javaClass.isInterface();
                if (!isJceClass) {
                    //看看实现接口有没有jce注解
                    PsiReferenceList implementsList = javaClass.getImplementsList();
                    if (implementsList != null && implementsList.getReferenceElements().length > 0) {
                        //看看是不是实现类
                        for (PsiJavaCodeReferenceElement referenceElement : implementsList.getReferenceElements()) {
                            PsiElement resolve = referenceElement.resolve();
                            if (resolve instanceof PsiClass && ((PsiClass) resolve).getName() != null) {
                                isJceClass = ((PsiClass) resolve).hasAnnotation("com.qq.cloud.taf.common.annotation.JceService");
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
                if (name.endsWith("Impl")) {
                    name = name.substring(0, name.length() - "Impl".length());
                }
                if (name.endsWith("Servant") || name.endsWith("Prx")) {
                    //是Prx
                    //查找interface
                    if (name.endsWith("Servant")) {
                        name = name.substring(0, name.length() - "Servant".length());
                    } else {
                        name = name.substring(0, name.length() - "Prx".length());
                    }
                }
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
                        String methodName = getFunctionName(javaMethod);
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

    @NotNull
    private static String getFunctionName(PsiMethod javaMethod) {
        String methodName;
        methodName = javaMethod.getName();
        if (methodName.startsWith("async_")) {
            methodName = methodName.substring("async_".length());
        }
        return methodName;
    }
}
