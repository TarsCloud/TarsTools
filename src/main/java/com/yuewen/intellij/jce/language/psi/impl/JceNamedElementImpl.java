package com.yuewen.intellij.jce.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.yuewen.intellij.jce.language.psi.JceNamedElement;

public abstract class JceNamedElementImpl extends ASTWrapperPsiElement implements JceNamedElement {
    public JceNamedElementImpl(ASTNode node) {
        super(node);
    }

    @Override
    public final int getTextOffset() {
        PsiElement nameElement = getNameIdentifier();
        if (nameElement == null) {
            return super.getTextOffset();
        }
        // return super.getTextOffset() + nameElement.getStartOffsetInParent();
        return nameElement.getTextOffset();
    }
}

