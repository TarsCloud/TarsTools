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
