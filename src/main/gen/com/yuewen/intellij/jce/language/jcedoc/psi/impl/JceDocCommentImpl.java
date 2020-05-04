// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.jcedoc.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.yuewen.intellij.jce.language.jcedoc.psi.JceDocTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.yuewen.intellij.jce.language.jcedoc.psi.*;

public class JceDocCommentImpl extends ASTWrapperPsiElement implements JceDocComment {

  public JceDocCommentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceDocVisitor visitor) {
    visitor.visitComment(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceDocVisitor) accept((JceDocVisitor)visitor);
    else super.accept(visitor);
  }

}
