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

public class JceFunctionParamImpl extends JceNamedElementImpl implements JceFunctionParam {

  public JceFunctionParamImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitFunctionParam(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceVisitor) accept((JceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public JceFieldType getFieldType() {
    return findNotNullChildByClass(JceFieldType.class);
  }

  @Override
  @Nullable
  public JceFieldTypeModifier getFieldTypeModifier() {
    return findChildByClass(JceFieldTypeModifier.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
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
  public String getName() {
    return JcePsiImplUtil.getName(this);
  }

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return JcePsiImplUtil.getPresentation(this);
  }

}
