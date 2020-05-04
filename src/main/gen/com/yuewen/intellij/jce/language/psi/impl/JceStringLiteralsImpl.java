// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.yuewen.intellij.jce.language.psi.JceTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.yuewen.intellij.jce.language.psi.*;

public class JceStringLiteralsImpl extends ASTWrapperPsiElement implements JceStringLiterals {

  public JceStringLiteralsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitStringLiterals(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceVisitor) accept((JceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getHexInt() {
    return findChildByType(HEX_INT);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @Nullable
  public PsiElement getNumDouble() {
    return findChildByType(NUM_DOUBLE);
  }

  @Override
  @Nullable
  public PsiElement getNumInt() {
    return findChildByType(NUM_INT);
  }

  @Override
  @Nullable
  public PsiElement getStringLiteral() {
    return findChildByType(STRING_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getWrongStringLiteral() {
    return findChildByType(WRONG_STRING_LITERAL);
  }

}
