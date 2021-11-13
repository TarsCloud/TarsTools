/**
 * Tencent is pleased to support the open source community by making Tars available.
 * <p>
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 * <p>
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * https://opensource.org/licenses/BSD-3-Clause
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceSyntaxHighlighter;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JceModuleAnnotator implements Annotator {
    public static void findAndAnnotateDuplicateSymbol(PsiFile containingFile, Collection<JceNamedElement> namedElementCollection, Map<String, JceNamedElement> symbolNameMap, JceModuleInfo moduleInfo, @NotNull AnnotationHolder annotationHolder) {
        if (!namedElementCollection.isEmpty()) {
            for (JceNamedElement namedElement : namedElementCollection) {
                PsiElement identifier = namedElement.getNameIdentifier();
                if (identifier == null) {
                    //语法错误
                    continue;
                }
                //region 检查名称是否重复
                JceNamedElement anotherNamedElement = symbolNameMap.putIfAbsent(namedElement.getName(), namedElement);
                if (anotherNamedElement != null) {
                    //已存在同样名字的字段
                    if (identifier.getContainingFile() == containingFile) {
                        annotationHolder.createErrorAnnotation(identifier, "Duplicate symbol name '" + namedElement.getName() + "' in module '"
                                + moduleInfo.getName() + "', previously defined at '" + anotherNamedElement.getContainingFile().getVirtualFile().getCanonicalPath() + "'");
                    }
                    if (anotherNamedElement.getContainingFile() == containingFile) {
                        //同一个文件才能标，否则会错乱的
                        annotationHolder.createErrorAnnotation(Objects.requireNonNull(anotherNamedElement.getNameIdentifier()),
                                "Duplicate symbol name '" + namedElement.getName() + "' in module '"
                                        + moduleInfo.getName() + "', previously defined at '" + identifier.getContainingFile().getVirtualFile().getCanonicalPath() + "'");
                    }
                }
                //endregion
            }
        }
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceFile)) return;
        JceFile jceFile = (JceFile) element;
        Set<String> moduleNameSet = new HashSet<>(jceFile.getModuleList().length);
        for (JceModuleInfo moduleInfo : jceFile.getModuleList()) {
            //标出identifier
            PsiElement nameIdentifier = moduleInfo.getNameIdentifier();
            if (nameIdentifier == null || nameIdentifier.getText().isEmpty()) {
                //语法错误
                return;
            }
            PsiFile containingFile = moduleInfo.getContainingFile();
            if (moduleNameSet.contains(moduleInfo.getName())) {
                //module命名重复
                annotationHolder.createErrorAnnotation(moduleInfo.getNameIdentifier(), "Duplicate module name '" + moduleInfo.getName() + "' in file '" + containingFile.getVirtualFile().getName() + "'");
            }
            moduleNameSet.add(moduleInfo.getName());
            Annotation structNameAnnotation = annotationHolder.createInfoAnnotation(nameIdentifier, null);
            structNameAnnotation.setTextAttributes(JceSyntaxHighlighter.CLASS_NAME);
            List<JceModuleInfo> jceModuleList = JceUtil.findModules(containingFile, nameIdentifier.getText(), true, false);
            Map<String, JceNamedElement> symbolNameMap = new HashMap<>(jceModuleList.size() * 16);
            for (JceModuleInfo jceModuleInfo : jceModuleList) {
                Collection<JceNamedElement> namedElementCollection = PsiTreeUtil.getChildrenOfAnyType(jceModuleInfo, JceEnumType.class, JceStructType.class, JceConstType.class);
                findAndAnnotateDuplicateSymbol(element.getContainingFile(), namedElementCollection, symbolNameMap, jceModuleInfo, annotationHolder);
                namedElementCollection = PsiTreeUtil.getChildrenOfAnyType(jceModuleInfo, JceInterfaceInfo.class);
                findAndAnnotateDuplicateSymbol(element.getContainingFile(), namedElementCollection, symbolNameMap, jceModuleInfo, annotationHolder);
            }
        }
    }
}
