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
        if (!(element instanceof JceStructType structType)) return;
        // 标出identifier
        PsiElement nameIdentifier = structType.getNameIdentifier();
        if (nameIdentifier == null) {
            // 语法错误
            return;
        }
        annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(nameIdentifier)
                .textAttributes(JceSyntaxHighlighter.CLASS_NAME)
                .create();
        List<JceFieldInfo> fieldInfoList = structType.getFieldInfoList();
        if (fieldInfoList.isEmpty()) {
            return;
        }

        boolean hasStructLevelWarning = false;
        Set<String> fieldNameSet = new HashSet<>();
        // 字段的起始tag
        JceFieldInfo firstField = fieldInfoList.get(0);
        JceFieldTag firstFieldTag = firstField.getFieldTag();
        int startTag = Integer.parseInt(firstFieldTag.getNumInt().getText());
        ResetStructFieldTagQuickFix resetStructFieldTagQuickFix = new ResetStructFieldTagQuickFix(structType);
        if (startTag != 0) {
            // 不是从0开始的
            annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Field tag isn't start with 0")
                    .range(firstFieldTag)
                    .withFix(resetStructFieldTagQuickFix)
                    .withFix(new FieldTagQuickFix(firstField, 0))
                    .create();
            hasStructLevelWarning = true;
        }
        Set<Integer> tagSet = new HashSet<>(fieldInfoList.size());
        for (JceFieldInfo fieldType : fieldInfoList) {
            PsiElement identifier = fieldType.getIdentifier();
            if (identifier == null) {
                // 语法错误
                return;
            }
            // 标出identifier
            annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(identifier)
                    // 字段
                    .textAttributes(JceSyntaxHighlighter.FIELD_DECLARATION)
                    .create();
            // region 检查名称是否重复
            if (fieldNameSet.contains(fieldType.getName())) {
                // 已存在同样名字的字段
                annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Duplicate field name '" + fieldType.getName() + "' in struct '" + structType.getName() + "'")
                        .range(identifier)
                        .create();
                hasStructLevelWarning = true;
            }
            fieldNameSet.add(fieldType.getName());
            // endregion
            // region 检查tag是否有序
            int currentTag = Integer.parseInt(fieldType.getFieldTag().getNumInt().getText());
            if (currentTag != startTag) {
                // tag顺序不一致
                annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Incorrect field tag " + currentTag + ", should be " + startTag)
                        .range(fieldType.getFieldTag())
                        .withFix(resetStructFieldTagQuickFix)
                        .withFix(new FieldTagQuickFix(fieldType, startTag))
                        .create();
                hasStructLevelWarning = true;
                if (tagSet.contains(currentTag)) {
                    // 重复tag
                    // 且tag与预期不符时，才需要给出重设tag的快速修复
                    annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Duplicate field tag " + currentTag)
                            .range(fieldType.getFieldTag())
                            .withFix(resetStructFieldTagQuickFix)
                            .withFix(new FieldTagQuickFix(fieldType, startTag))
                            .create();
                }
            }
            ++startTag;
            tagSet.add(currentTag);
            // endregion
        }
        if (hasStructLevelWarning) {
            annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Has field tag problem")
                    .range(nameIdentifier)
                    .withFix(resetStructFieldTagQuickFix)
                    .create();
        }
    }
}
