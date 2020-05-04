package com.yuewen.intellij.jce.language.quickfix;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.yuewen.intellij.jce.language.psi.JceElementFactory;
import com.yuewen.intellij.jce.language.psi.JceFieldDefaultAssignment;
import com.yuewen.intellij.jce.language.psi.JceFieldInfo;
import com.yuewen.intellij.jce.language.psi.JceStructType;
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
