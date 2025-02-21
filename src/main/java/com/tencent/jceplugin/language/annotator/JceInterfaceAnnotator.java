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
import com.tencent.jceplugin.language.psi.JceInterfaceInfo;
import com.tencent.jceplugin.language.psi.JceNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JceInterfaceAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceInterfaceInfo interfaceInfo)) return;
        // 标出identifier
        PsiElement nameIdentifier = interfaceInfo.getNameIdentifier();
        if (nameIdentifier == null) {
            // 语法错误
            return;
        }
        annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(nameIdentifier)
                .textAttributes(JceSyntaxHighlighter.INTERFACE_NAME)
                .create();
        List<JceFunctionInfo> functionInfoList = interfaceInfo.getFunctionInfoList();
        List<JceNamedElement> namedElementList = new ArrayList<>(functionInfoList);
        if (!namedElementList.isEmpty()) {
            Set<String> fieldNameSet = new HashSet<>();
            for (JceNamedElement fieldType : namedElementList) {
                PsiElement identifier = fieldType.getNameIdentifier();
                if (identifier == null) {
                    // 语法错误
                    continue;
                }
                // region 检查名称是否重复
                if (fieldNameSet.contains(fieldType.getName())) {
                    // 已存在同样名字的字段
                    annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Duplicate function name '" + fieldType.getName() + "' in interface '" + interfaceInfo.getName() + "'")
                            .range(identifier)
                            .create();
                }
                fieldNameSet.add(fieldType.getName());
                // endregion
            }
        }
    }
}
