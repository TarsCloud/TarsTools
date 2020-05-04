package com.tencent.jceplugin.language.hint;

import com.intellij.codeInsight.hint.DeclarationRangeHandler;
import com.intellij.openapi.util.TextRange;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import org.jetbrains.annotations.NotNull;

public class ModuleDeclarationRangeHandler implements DeclarationRangeHandler<JceModuleInfo> {
    @Override
    @NotNull
    public TextRange getDeclarationRange(@NotNull final JceModuleInfo container) {
        final TextRange textRange = container.getTextRange();
        int startOffset = textRange != null ? textRange.getStartOffset() : container.getTextOffset();
        int endOffset = startOffset + "module".length();
        if (container.getNameIdentifier() != null) {
            endOffset = container.getNameIdentifier().getTextRange().getEndOffset();
        }
        return new TextRange(startOffset, endOffset);
    }
}
