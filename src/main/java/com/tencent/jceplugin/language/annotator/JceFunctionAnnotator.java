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
import com.tencent.jceplugin.language.psi.JceFunctionInfo;
import com.tencent.jceplugin.language.psi.JceFunctionParam;
import com.tencent.jceplugin.language.psi.JceFunctionParamList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class JceFunctionAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceFunctionInfo functionInfo)) return;
        PsiElement identifier = functionInfo.getIdentifier();
        if (identifier == null) {
            // 语法错误
            return;
        }
        // 标出identifier
        annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                // 字段
                .range(identifier)
                .textAttributes(JceSyntaxHighlighter.FUNCTION_DECLARATION)
                .create();
        JceFunctionParamList functionParamList = functionInfo.getFunctionParamList();
        if (functionParamList != null) {
            Set<String> fieldNameSet = new HashSet<>();
            for (JceFunctionParam jceFunctionParam : functionParamList.getFunctionParamList()) {
                // 标出identifier
                PsiElement paramIdentifier = jceFunctionParam.getIdentifier();
                if (paramIdentifier == null) {
                    // 语法错误
                    continue;
                }
                if (jceFunctionParam.getName() == null || jceFunctionParam.getName().isEmpty()) {
                    // 语法错误
                    continue;
                }
                if (fieldNameSet.contains(jceFunctionParam.getName())) {
                    // 命名重复
                    annotationHolder.newAnnotation(HighlightSeverity.ERROR,
                                    "Duplicate parameter name '" + jceFunctionParam.getName() + "' in function '" + functionInfo.getName() + "'")
                            .range(paramIdentifier)
                            .create();
                }
                annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(paramIdentifier)
                        .textAttributes(JceSyntaxHighlighter.PARAMETER)
                        .create();
                // 参数名
                fieldNameSet.add(jceFunctionParam.getName());
            }
        }
    }
}
