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
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiReference;

public class JceIncludeInfoImpl extends ASTWrapperPsiElement implements JceIncludeInfo {

  public JceIncludeInfoImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitIncludeInfo(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceVisitor) accept((JceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public JceIncludeFilename getIncludeFilename() {
    return findChildByClass(JceIncludeFilename.class);
  }

  @Override
  @NotNull
  public String getIncludeFileName() {
    return JcePsiImplUtil.getIncludeFileName(this);
  }

  @Override
  @Nullable
  public PsiReference getReference() {
    return JcePsiImplUtil.getReference(this);
  }

  @Override
  @NotNull
  public PsiReference[] getReferences() {
    return JcePsiImplUtil.getReferences(this);
  }

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return JcePsiImplUtil.getPresentation(this);
  }

}
