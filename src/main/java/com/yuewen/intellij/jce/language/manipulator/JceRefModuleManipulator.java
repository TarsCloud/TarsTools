package com.yuewen.intellij.jce.language.manipulator;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.yuewen.intellij.jce.language.psi.JceRefModule;
import org.jetbrains.annotations.NotNull;

public class JceRefModuleManipulator extends AbstractElementManipulator<JceRefModule> {
    @Override
    public JceRefModule handleContentChange(@NotNull JceRefModule psi, @NotNull TextRange range, String newContent) {
        return psi.setName(newContent);
    }

    @NotNull
    @Override
    public TextRange getRangeInElement(@NotNull JceRefModule element) {
        return element.getIdentifier().getTextRangeInParent();
    }
}
