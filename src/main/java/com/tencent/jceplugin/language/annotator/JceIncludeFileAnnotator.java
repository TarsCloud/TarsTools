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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.tencent.jceplugin.language.psi.JceFile;
import com.tencent.jceplugin.language.psi.JceIncludeFilename;
import com.tencent.jceplugin.language.psi.JceIncludeInfo;
import com.tencent.jceplugin.language.quickfix.RemoveIncludeQuickFix;
import org.jetbrains.annotations.NotNull;

public class JceIncludeFileAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceIncludeInfo)) {
            return;
        }
        JceIncludeFilename includeFilename = ((JceIncludeInfo) element).getIncludeFilename();
        if (includeFilename == null) {
            // 语法错误
            return;
        }
        if (includeFilename.getReference() == null) {
            // 引用错误
            return;
        }
        TextRange valueTextRange = ElementManipulators.getValueTextRange(includeFilename).shiftRight(includeFilename.getTextRange().getStartOffset());
        if (includeFilename.getFileName().isEmpty()) {
            // 文件名错误
            // 引用的不是jce文件
            annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Jce file path is required")
                    .range(valueTextRange)
                    .create();
            return;
        }
        PsiElement referenceJceFile = includeFilename.getReference().resolve();
        if (!(referenceJceFile instanceof JceFile)) {
            // 引用的文件不是jce文件
            annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid Jce file '" + includeFilename.getFileName() + "'")
                    .range(valueTextRange)
                    .create();
            return;
        }
        if (referenceJceFile == includeFilename.getContainingFile()) {
            // 引用了当前文件
            annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Can not include current jce file")
                    .range(valueTextRange)
                    .create();
            return;
        }
        // 检查重复引用
        JceFile jceFile = (JceFile) element.getContainingFile();
        for (JceIncludeInfo jceIncludeInfo : jceFile.getIncludeList()) {
            if (jceIncludeInfo == element) {
                continue;
            }
            if (jceIncludeInfo.getIncludeFilename() == null) {
                continue;
            }
            PsiReference otherIncludeReference = jceIncludeInfo.getIncludeFilename().getReference();
            if (otherIncludeReference == null) {
                continue;
            }
            PsiElement otherIncludeFile = otherIncludeReference.resolve();
            if (!(otherIncludeFile instanceof JceFile)) {
                continue;
            }
            if (referenceJceFile.equals(otherIncludeFile)) {
                // 引用了重复的文件
                annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Duplicate include Jce file '" + ((JceFile) referenceJceFile).getVirtualFile().getPath() + "'")
                        .range(valueTextRange)
                        .withFix(new RemoveIncludeQuickFix((JceIncludeInfo) element))
                        .create();
            }
        }
    }
}
