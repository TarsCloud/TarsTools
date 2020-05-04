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

public class JceFieldTypeImpl extends ASTWrapperPsiElement implements JceFieldType {

  public JceFieldTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitFieldType(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceVisitor) accept((JceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public JceBuiltInTypes getBuiltInTypes() {
    return findChildByClass(JceBuiltInTypes.class);
  }

  @Override
  @Nullable
  public JceMapType getMapType() {
    return findChildByClass(JceMapType.class);
  }

  @Override
  @Nullable
  public JceRefModule getRefModule() {
    return findChildByClass(JceRefModule.class);
  }

  @Override
  @Nullable
  public JceVectorType getVectorType() {
    return findChildByClass(JceVectorType.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
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
  public JceFieldType setName(String newName) {
    return JcePsiImplUtil.setName(this, newName);
  }

  @Override
  public JceFieldType setQualifierName(String newName) {
    return JcePsiImplUtil.setQualifierName(this, newName);
  }

}
