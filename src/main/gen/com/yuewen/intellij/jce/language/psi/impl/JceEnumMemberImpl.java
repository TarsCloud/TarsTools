// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.yuewen.intellij.jce.language.psi.JceTypes.*;
import com.yuewen.intellij.jce.language.psi.*;
import com.intellij.navigation.ItemPresentation;

public class JceEnumMemberImpl extends JceNamedElementImpl implements JceEnumMember {

  public JceEnumMemberImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitEnumMember(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceVisitor) accept((JceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return findNotNullChildByType(IDENTIFIER);
  }

  @Override
  @Nullable
  public PsiElement getNumInt() {
    return findChildByType(NUM_INT);
  }

  @Override
  public PsiElement getNameIdentifier() {
    return JcePsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public String getName() {
    return JcePsiImplUtil.getName(this);
  }

  @Override
  public PsiElement setName(String newName) {
    return JcePsiImplUtil.setName(this, newName);
  }

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return JcePsiImplUtil.getPresentation(this);
  }

}
