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

public class JceFieldInfoImpl extends JceNamedElementImpl implements JceFieldInfo {

  public JceFieldInfoImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitFieldInfo(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceVisitor) accept((JceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public JceFieldDefaultAssignment getFieldDefaultAssignment() {
    return findChildByClass(JceFieldDefaultAssignment.class);
  }

  @Override
  @Nullable
  public JceFieldLabel getFieldLabel() {
    return findChildByClass(JceFieldLabel.class);
  }

  @Override
  @NotNull
  public JceFieldTag getFieldTag() {
    return findNotNullChildByClass(JceFieldTag.class);
  }

  @Override
  @Nullable
  public JceFieldType getFieldType() {
    return findChildByClass(JceFieldType.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  public String getName() {
    return JcePsiImplUtil.getName(this);
  }

  @Override
  public PsiElement getNameIdentifier() {
    return JcePsiImplUtil.getNameIdentifier(this);
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
