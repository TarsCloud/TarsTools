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

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceSyntaxHighlighter;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.JceConstType;
import com.tencent.jceplugin.language.psi.JceEnumType;
import com.tencent.jceplugin.language.psi.JceFile;
import com.tencent.jceplugin.language.psi.JceInterfaceInfo;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import com.tencent.jceplugin.language.psi.JceNamedElement;
import com.tencent.jceplugin.language.psi.JceStructType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class JceModuleAnnotator implements Annotator {
    public static void findAndAnnotateDuplicateSymbol(PsiFile containingFile, Collection<JceNamedElement> namedElementCollection, Map<String, JceNamedElement> symbolNameMap, JceModuleInfo moduleInfo, @NotNull AnnotationHolder annotationHolder) {
        if (!namedElementCollection.isEmpty()) {
            for (JceNamedElement namedElement : namedElementCollection) {
                PsiElement identifier = namedElement.getNameIdentifier();
                if (identifier == null) {
                    // 语法错误
                    continue;
                }
                // region 检查名称是否重复
                JceNamedElement anotherNamedElement = symbolNameMap.putIfAbsent(namedElement.getName(), namedElement);
                if (anotherNamedElement != null) {
                    // 已存在同样名字的字段
                    if (identifier.getContainingFile() == containingFile) {
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Duplicate symbol name '" + namedElement.getName() + "' in module '"
                                        + moduleInfo.getName() + "', previously defined at '" + anotherNamedElement.getContainingFile().getVirtualFile().getCanonicalPath() + "'")
                                .range(identifier)
                                .create();
                    }
                    if (anotherNamedElement.getContainingFile() == containingFile) {
                        // 同一个文件才能标，否则会错乱的
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Duplicate symbol name '" + namedElement.getName() + "' in module '"
                                        + moduleInfo.getName() + "', previously defined at '" + identifier.getContainingFile().getVirtualFile().getCanonicalPath() + "'")
                                .range(Objects.requireNonNull(anotherNamedElement.getNameIdentifier()))
                                .create();
                    }
                }
                // endregion
            }
        }
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceFile jceFile)) return;
        Set<String> moduleNameSet = new HashSet<>(jceFile.getModuleList().length);
        for (JceModuleInfo moduleInfo : jceFile.getModuleList()) {
            // 标出identifier
            PsiElement nameIdentifier = moduleInfo.getNameIdentifier();
            if (nameIdentifier == null || nameIdentifier.getText().isEmpty()) {
                // 语法错误
                return;
            }
            PsiFile containingFile = moduleInfo.getContainingFile();
            if (moduleNameSet.contains(moduleInfo.getName())) {
                // module命名重复
                annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Duplicate module name '" + moduleInfo.getName() + "' in file '" + containingFile.getVirtualFile().getName() + "'")
                        .range(moduleInfo.getNameIdentifier())
                        .create();
            }
            moduleNameSet.add(moduleInfo.getName());
            annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(nameIdentifier)
                    .textAttributes(JceSyntaxHighlighter.CLASS_NAME)
                    .create();
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
