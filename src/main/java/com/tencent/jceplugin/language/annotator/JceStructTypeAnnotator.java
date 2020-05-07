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
import com.tencent.jceplugin.language.JceSyntaxHighlighter;
import com.tencent.jceplugin.language.psi.JceFieldInfo;
import com.tencent.jceplugin.language.psi.JceFieldTag;
import com.tencent.jceplugin.language.psi.JceStructType;
import com.tencent.jceplugin.language.quickfix.FieldTagQuickFix;
import com.tencent.jceplugin.language.quickfix.ResetStructFieldTagQuickFix;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JceStructTypeAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceStructType)) return;
        JceStructType structType = (JceStructType) element;
        //标出identifier
        PsiElement nameIdentifier = structType.getNameIdentifier();
        if (nameIdentifier == null) {
            //语法错误
            return;
        }
        Annotation structNameAnnotation = annotationHolder.createInfoAnnotation(nameIdentifier, null);
        structNameAnnotation.setTextAttributes(JceSyntaxHighlighter.CLASS_NAME);
        List<JceFieldInfo> fieldInfoList = structType.getFieldInfoList();
        if (!fieldInfoList.isEmpty()) {
            Set<String> fieldNameSet = new HashSet<>();
            //字段的起始tag
            JceFieldInfo firstField = fieldInfoList.get(0);
            JceFieldTag firstFieldTag = firstField.getFieldTag();
            int startTag = Integer.parseInt(firstFieldTag.getNumInt().getText());
            if (startTag != 0) {
                //不是从0开始的
                Annotation warningAnnotation = annotationHolder.createWarningAnnotation(firstFieldTag, "Field tag isn't start with 0");
                warningAnnotation.registerFix(new ResetStructFieldTagQuickFix(structType));
                warningAnnotation.registerFix(new FieldTagQuickFix(firstField, 0));
                Annotation hasFieldTagProblem = annotationHolder.createWarningAnnotation(nameIdentifier, "Has field tag problem");
                hasFieldTagProblem.registerFix(new ResetStructFieldTagQuickFix(structType));
            }
            Set<Integer> tagSet = new HashSet<>(fieldInfoList.size());
            for (JceFieldInfo fieldType : fieldInfoList) {
                PsiElement identifier = fieldType.getIdentifier();
                if (identifier == null) {
                    //语法错误
                    return;
                }
                //标出identifier
                Annotation annotation = annotationHolder.createInfoAnnotation(identifier, null);
                //字段
                annotation.setTextAttributes(JceSyntaxHighlighter.FIELD_DECLARATION);
                //region 检查名称是否重复
                if (fieldNameSet.contains(fieldType.getName())) {
                    //已存在同样名字的字段
                    Annotation errorAnnotation = annotationHolder.createErrorAnnotation(identifier, "Duplicate field name '" + fieldType.getName() + "' in struct '" + structType.getName() + "'");
                    Annotation hasFieldNameProblem = annotationHolder.createWarningAnnotation(nameIdentifier, "Has field name problem");
                    hasFieldNameProblem.registerFix(new ResetStructFieldTagQuickFix(structType));
                }
                fieldNameSet.add(fieldType.getName());
                //endregion
                //region 检查tag是否有序
                int currentTag = Integer.parseInt(fieldType.getFieldTag().getNumInt().getText());
                if (currentTag != startTag) {
                    //tag顺序不一致
                    Annotation warningAnnotation = annotationHolder.createWarningAnnotation(fieldType.getFieldTag(), "Incorrect field tag " + currentTag + ", should be " + startTag);
                    warningAnnotation.registerFix(new ResetStructFieldTagQuickFix(structType));
                    warningAnnotation.registerFix(new FieldTagQuickFix(fieldType, startTag));
                    Annotation hasFieldTagProblem = annotationHolder.createWarningAnnotation(nameIdentifier, "Has field tag problem");
                    hasFieldTagProblem.registerFix(new ResetStructFieldTagQuickFix(structType));
                }
                if (tagSet.contains(currentTag)) {
                    //重复tag
                    Annotation errorAnnotation = annotationHolder.createErrorAnnotation(fieldType.getFieldTag(), "Duplicate field tag " + currentTag);
                    errorAnnotation.registerFix(new ResetStructFieldTagQuickFix(structType));
                    errorAnnotation.registerFix(new FieldTagQuickFix(fieldType, startTag));
                    Annotation hasFieldTagProblem = annotationHolder.createWarningAnnotation(nameIdentifier, "Has field tag problem");
                    hasFieldTagProblem.registerFix(new ResetStructFieldTagQuickFix(structType));
                }
                ++startTag;
                tagSet.add(currentTag);
                //endregion
            }
        }
    }
}
