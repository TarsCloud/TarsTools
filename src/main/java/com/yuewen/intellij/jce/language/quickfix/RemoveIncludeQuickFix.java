package com.yuewen.intellij.jce.language.quickfix;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.yuewen.intellij.jce.language.psi.JceIncludeInfo;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * 移除字段默认值
 */
public class RemoveIncludeQuickFix extends BaseIntentionAction {
    private final JceIncludeInfo includeInfo;

    public RemoveIncludeQuickFix(JceIncludeInfo structType) {
        includeInfo = structType;
    }

    @NotNull
    @Override
    public String getText() {
        return "Remove include statement";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Remove include statement";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        if (includeInfo.getParent() == null) {
            return;
        }
        includeInfo.getParent().getNode().removeChild(includeInfo.getNode());
    }
}
