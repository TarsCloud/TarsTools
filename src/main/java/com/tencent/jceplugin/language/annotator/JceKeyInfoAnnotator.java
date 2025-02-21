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
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceSyntaxHighlighter;
import com.tencent.jceplugin.language.psi.JceKeyInfo;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import com.tencent.jceplugin.language.psi.JceNamedElement;
import com.tencent.jceplugin.language.psi.JceRefStructField;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class JceKeyInfoAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceKeyInfo keyInfo)) return;
        // 引用的module
        JceModuleInfo parentModule = PsiTreeUtil.getParentOfType(keyInfo, JceModuleInfo.class);
        if (parentModule == null) {
            // 语法错误
            return;
        }
        if (keyInfo.getRefStruct() != null) {
            PsiReference structReference = keyInfo.getRefStruct().getReference();
            if (structReference == null || structReference.resolve() == null) {
                // 结构体
                annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Unresolved struct '" + keyInfo.getRefStruct().getIdentifier().getText() + "' in module '" + parentModule.getName() + "'")
                        .range(keyInfo.getRefStruct())
                        .create();
            } else {
                // 结构体
                annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(keyInfo.getRefStruct())
                        .textAttributes(JceSyntaxHighlighter.CLASS_NAME)
                        .create();
            }
        }
        if (!keyInfo.getRefStructFieldList().isEmpty()) {
            Set<String> fieldNameSet = new HashSet<>();
            for (JceRefStructField structField : keyInfo.getRefStructFieldList()) {
                PsiElement resolve = Optional.ofNullable(structField.getReference())
                        .map(PsiReference::resolve)
                        .orElse(null);
                if (resolve == null) {
                    // 结构体字段
                    String structName = keyInfo.getRefStruct().getIdentifier().getText();
                    annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Unresolved field '" + structField.getIdentifier().getText() + "' in struct '" + structName + "'")
                            .range(structField)
                            .create();
                } else {
                    // 字段
                    JceNamedElement namedElement = (JceNamedElement) resolve;
                    if (fieldNameSet.contains(namedElement.getName())) {
                        // 重复字段
                        annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Duplicate key field")
                                .range(structField)
                                .create();
                    } else {
                        annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                .range(structField)
                                .textAttributes(JceSyntaxHighlighter.FIELD_REFERENCE)
                                .create();
                        fieldNameSet.add(namedElement.getName());
                    }
                }
            }
        }
    }
}
