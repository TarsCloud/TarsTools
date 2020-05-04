package com.yuewen.intellij.jce.language.manipulator;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.yuewen.intellij.jce.language.psi.JceIncludeFilename;
import org.jetbrains.annotations.NotNull;

public class JceStringManipulator extends AbstractElementManipulator<JceIncludeFilename> {
    @Override
    public JceIncludeFilename handleContentChange(@NotNull JceIncludeFilename psi, @NotNull TextRange range, String newContent) {
        String oldText = psi.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText.substring(range.getEndOffset());
        return psi.setName(newText);
    }

    @NotNull
    @Override
    public TextRange getRangeInElement(@NotNull JceIncludeFilename element) {
        return getStringTokenRange(element);
    }

    public static TextRange getStringTokenRange(JceIncludeFilename element) {
        return TextRange.from(1, element.getTextLength() - 2);
    }
}
