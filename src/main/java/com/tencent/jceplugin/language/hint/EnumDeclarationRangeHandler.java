package com.tencent.jceplugin.language.hint;

import com.intellij.codeInsight.hint.DeclarationRangeHandler;
import com.intellij.openapi.util.TextRange;
import com.tencent.jceplugin.language.psi.JceEnumType;
import org.jetbrains.annotations.NotNull;

public class EnumDeclarationRangeHandler implements DeclarationRangeHandler<JceEnumType> {
    @Override
    @NotNull
    public TextRange getDeclarationRange(@NotNull final JceEnumType container) {
        final TextRange textRange = container.getTextRange();
        int startOffset = textRange != null ? textRange.getStartOffset() : container.getTextOffset();
        int endOffset = startOffset + "enum".length();
        if (container.getNameIdentifier() != null) {
            endOffset = container.getNameIdentifier().getTextRange().getEndOffset();
        }
        return new TextRange(startOffset, endOffset);
    }
}
