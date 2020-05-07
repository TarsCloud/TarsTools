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

package com.tencent.jceplugin.language.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
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
import java.util.Set;

public class JceKeyInfoAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceKeyInfo)) return;
        JceKeyInfo keyInfo = (JceKeyInfo) element;
        //引用的module
        JceModuleInfo parentModule = PsiTreeUtil.getParentOfType(keyInfo, JceModuleInfo.class);
        if (parentModule == null) {
            //语法错误
            return;
        }
        if (keyInfo.getRefStruct() != null) {
            PsiReference structReference = keyInfo.getRefStruct().getReference();
            if (structReference == null) {
                //结构体
                annotationHolder.createErrorAnnotation(keyInfo.getRefStruct(), "Unresolved struct '" + keyInfo.getRefStruct().getIdentifier().getText() + "' in module '" + parentModule.getName() + "'");
            } else {
                PsiElement struct = structReference.resolve();
                if (struct == null) {
                    annotationHolder.createErrorAnnotation(keyInfo.getRefStruct(), "Unresolved struct '" + keyInfo.getRefStruct().getIdentifier().getText() + "' in module '" + parentModule.getName() + "'");
                } else {
                    //结构体
                    Annotation annotation = annotationHolder.createInfoAnnotation(keyInfo.getRefStruct(), null);
                    annotation.setTextAttributes(JceSyntaxHighlighter.CLASS_NAME);
                }
            }
        }
        if (!keyInfo.getRefStructFieldList().isEmpty()) {
            Set<String> fieldNameSet = new HashSet<>();
            for (JceRefStructField structField : keyInfo.getRefStructFieldList()) {
                if (structField.getReference() == null) {
                    //结构体字段
                    String structName = keyInfo.getRefStruct().getIdentifier().getText();
                    annotationHolder.createErrorAnnotation(structField, "Unresolved field '" + structField.getIdentifier().getText() + "' in struct '" + structName + "'");
                } else {
                    PsiElement resolve = structField.getReference().resolve();
                    if (resolve == null) {
                        //结构体字段
                        String structName = keyInfo.getRefStruct().getIdentifier().getText();
                        annotationHolder.createErrorAnnotation(structField, "Unresolved field '" + structField.getIdentifier().getText() + "' in struct '" + structName + "'");
                    } else {
                        //字段
                        JceNamedElement namedElement = (JceNamedElement) resolve;
                        if (fieldNameSet.contains(namedElement.getName())) {
                            //重复字段
                            annotationHolder.createWarningAnnotation(structField, "Duplicate key field");
                        } else {
                            Annotation annotation = annotationHolder.createInfoAnnotation(structField, null);
                            annotation.setTextAttributes(JceSyntaxHighlighter.FIELD_REFERENCE);
                            fieldNameSet.add(namedElement.getName());
                        }
                    }
                }
            }
        }
    }
}
