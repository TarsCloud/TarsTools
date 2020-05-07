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

package com.tencent.jceplugin.language.quickfix;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.tencent.jceplugin.language.psi.JceElementFactory;
import com.tencent.jceplugin.language.psi.JceFieldDefaultAssignment;
import com.tencent.jceplugin.language.psi.JceFieldInfo;
import com.tencent.jceplugin.language.psi.JceStructType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * 快速修复字段tag问题
 */
public class ResetStructFieldTagQuickFix extends BaseIntentionAction {
    private final JceStructType myStructType;

    public ResetStructFieldTagQuickFix(JceStructType structType) {
        myStructType = structType;
    }

    @NotNull
    @Override
    public String getText() {
        return "Reset field tag in " + myStructType.getName();
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Jce field";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        List<JceFieldInfo> fieldInfoList = myStructType.getFieldInfoList();
        for (int i = 0; i < fieldInfoList.size(); i++) {
            JceFieldInfo fieldInfo = fieldInfoList.get(i);
            fieldInfo.replace(JceElementFactory.createField(project, i,
                    //字段optional还是required
                    Optional.ofNullable(fieldInfo.getFieldLabel()).map(PsiElement::getText).orElse("optional"),
                    //字段类型
                    Optional.ofNullable(fieldInfo.getFieldType()).map(PsiElement::getText).orElse("string"),
                    //字段名称
                    Optional.ofNullable(fieldInfo.getName()).orElse("fieldName"),
                    //默认值
                    Optional.ofNullable(fieldInfo.getFieldDefaultAssignment()).map(JceFieldDefaultAssignment::getStringLiterals).map(PsiElement::getText).orElse("")));
        }
    }
}
