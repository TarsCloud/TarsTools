package com.tencent.jceplugin.language.jcedoc.psi;

import com.intellij.psi.PsiDocCommentBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LazyParseablePsiElement;
import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.language.psi.JceElementType;
import org.jetbrains.annotations.Nullable;

public class PsiJceDocElement extends LazyParseablePsiElement implements PsiDocCommentBase {
    public PsiJceDocElement(CharSequence buffer) {
        super(JceElementType.DOC_COMMENT, buffer);
    }

    @Nullable
    @Override
    public PsiElement getOwner() {
        return null;
    }

    @Override
    public IElementType getTokenType() {
        return getElementType();
    }
}
