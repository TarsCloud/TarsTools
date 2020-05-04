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
import com.intellij.psi.PsiReference;

public class JceRefModuleImpl extends ASTWrapperPsiElement implements JceRefModule {

  public JceRefModuleImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitRefModule(this);
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
  @NotNull
  public PsiReference[] getReferences() {
    return JcePsiImplUtil.getReferences(this);
  }

  @Override
  @Nullable
  public PsiReference getReference() {
    return JcePsiImplUtil.getReference(this);
  }

  @Override
  public JceRefModule setName(String newName) {
    return JcePsiImplUtil.setName(this, newName);
  }

}
