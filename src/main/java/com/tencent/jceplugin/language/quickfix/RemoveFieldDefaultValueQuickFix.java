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
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.tencent.jceplugin.language.psi.JceFieldDefaultAssignment;
import com.tencent.jceplugin.language.psi.JceFieldInfo;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * 移除字段默认值
 */
public class RemoveFieldDefaultValueQuickFix extends BaseIntentionAction {
    private final JceFieldInfo myStructType;

    public RemoveFieldDefaultValueQuickFix(JceFieldInfo structType) {
        myStructType = structType;
    }

    @NotNull
    @Override
    public String getText() {
        return "Remove default value";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Remove default value";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        JceFieldDefaultAssignment fieldDefaultAssignment = myStructType.getFieldDefaultAssignment();
        if (fieldDefaultAssignment != null) {
            myStructType.getNode().removeChild(fieldDefaultAssignment.getNode());
        }
    }
}
