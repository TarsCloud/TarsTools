package com.tencent.jceplugin.language.quickfix;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.tencent.jceplugin.language.psi.JceElementFactory;
import com.tencent.jceplugin.language.psi.JceFieldInfo;
import com.tencent.jceplugin.language.psi.JceFieldType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * 快速修复字段tag问题
 */
public class ChangeFieldTypeNameToQuickFix extends BaseIntentionAction {
    private final JceFieldType myFieldType;
    private final String myExpectedType;

    public ChangeFieldTypeNameToQuickFix(JceFieldType fieldType, String expectedTypeString) {
        myFieldType = fieldType;
        myExpectedType = expectedTypeString;
    }

    @NotNull
    @Override
    public String getText() {
        return "Change to " + myExpectedType;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Change field type";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        JceFieldInfo newFieldInfo = JceElementFactory.createField(project, 0,
                //字段optional还是required
                "optional",
                //字段类型
                myExpectedType,
                //字段名称
                "fieldName", "");
        assert newFieldInfo.getFieldType() != null;
        myFieldType.replace(newFieldInfo.getFieldType());
    }
}
