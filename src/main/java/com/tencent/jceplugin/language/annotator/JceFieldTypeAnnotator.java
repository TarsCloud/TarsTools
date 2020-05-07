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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceParserDefinition;
import com.tencent.jceplugin.language.JceSyntaxHighlighter;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.*;
import com.tencent.jceplugin.language.quickfix.ChangeFieldTypeNameToQuickFix;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JceFieldTypeAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceFieldType)) return;
        JceFieldType fieldType = (JceFieldType) element;
        if (fieldType.getVectorType() != null && fieldType.getVectorType().getFieldType() != null) {
            //vector
            annotate(fieldType.getVectorType().getFieldType(), annotationHolder);
        }
        if (fieldType.getMapType() != null && !fieldType.getMapType().getFieldTypeList().isEmpty()) {
            //map
            fieldType.getMapType().getFieldTypeList().forEach(subFieldType -> annotate(subFieldType, annotationHolder));
            return;
        }
        if (fieldType.getBuiltInTypes() != null) {
            //内建基础类型
            return;
        }
        //引用的module
        JceModuleInfo parentModule = PsiTreeUtil.getParentOfType(fieldType, JceModuleInfo.class);
        if (parentModule == null) {
            //语法错误
            return;
        }
        JceRefModule refModule = fieldType.getRefModule();
        if (refModule != null) {
            if (refModule.getReference() == null || (parentModule = (JceModuleInfo) refModule.getReference().resolve()) == null) {
                //module没找到
                TextRange moduleRefRange = TextRange.create(refModule.getTextRange().getStartOffset(), refModule.getTextRange().getEndOffset());
                annotationHolder.createErrorAnnotation(moduleRefRange, "Unresolved module '" + refModule.getText() + "'");
                return;
            }
        }
        PsiElement identifier = fieldType.getIdentifier();
        if (identifier == null) {
            //语法错误
            return;
        }
        TextRange identifierRange = TextRange.create(identifier.getTextRange().getStartOffset(), identifier.getTextRange().getEndOffset());
        String name = identifier.getText();
        //名字可能是Integer/String/Byte/Short/Unsigned等等保留字
        if (JceParserDefinition.reservedBuiltinType.contains(name.toLowerCase())) {
            //是保留字
            Annotation annotation = annotationHolder.createWarningAnnotation(identifierRange, "Capitalized base type '" + name.toLowerCase() + "'");
            annotation.registerFix(new ChangeFieldTypeNameToQuickFix(fieldType, name.toLowerCase()));
            return;
        }
        if ("boolean".equalsIgnoreCase(name)) {
            //是bool
            Annotation annotation = annotationHolder.createWarningAnnotation(identifierRange, "Nonstandard base type '" + name.toLowerCase() + "'");
            annotation.registerFix(new ChangeFieldTypeNameToQuickFix(fieldType, "bool"));
            return;
        }
        List<JceStructType> structList = JceUtil.findStruct(parentModule, name);
        List<JceEnumType> enumList = JceUtil.findEnum(parentModule, name);
        if (structList.isEmpty() && enumList.isEmpty()) {
            annotationHolder.createErrorAnnotation(identifierRange, "Unresolved symbol '" + name + "' in module '" + parentModule.getName() + "'");
        } else {
            // Found at least one property
            Annotation annotation = annotationHolder.createInfoAnnotation(identifierRange, null);
            annotation.setTextAttributes(JceSyntaxHighlighter.CLASS_NAME);
        }
    }
}
