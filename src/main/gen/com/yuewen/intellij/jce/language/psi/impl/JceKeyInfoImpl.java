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

public class JceKeyInfoImpl extends ASTWrapperPsiElement implements JceKeyInfo {

  public JceKeyInfoImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitKeyInfo(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceVisitor) accept((JceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public JceRefStruct getRefStruct() {
    return findChildByClass(JceRefStruct.class);
  }

  @Override
  @NotNull
  public List<JceRefStructField> getRefStructFieldList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JceRefStructField.class);
  }

}
