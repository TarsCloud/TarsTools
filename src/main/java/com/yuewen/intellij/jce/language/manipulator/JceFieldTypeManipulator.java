package com.yuewen.intellij.jce.language.manipulator;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.yuewen.intellij.jce.language.psi.JceFieldType;
import org.jetbrains.annotations.NotNull;

public class JceFieldTypeManipulator extends AbstractElementManipulator<JceFieldType> {
    @Override
    public JceFieldType handleContentChange(@NotNull JceFieldType psi, @NotNull TextRange range, String newContent) {
        if (psi.getIdentifier() == null) {
            return psi;
        }
        return psi.setName(newContent);
    }

    @NotNull
    @Override
    public TextRange getRangeInElement(@NotNull JceFieldType element) {
        if (element.getIdentifier() == null) {
            return new TextRange(0, 0);
        }
        return element.getIdentifier().getTextRangeInParent();
    }
}
