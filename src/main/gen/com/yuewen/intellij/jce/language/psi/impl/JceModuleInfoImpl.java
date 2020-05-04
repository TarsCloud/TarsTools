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

public class JceModuleInfoImpl extends JceNamedElementImpl implements JceModuleInfo {

  public JceModuleInfoImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JceVisitor visitor) {
    visitor.visitModuleInfo(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JceVisitor) accept((JceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<JceConstType> getConstTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JceConstType.class);
  }

  @Override
  @NotNull
  public List<JceEnumType> getEnumTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JceEnumType.class);
  }

  @Override
  @NotNull
  public List<JceInterfaceInfo> getInterfaceInfoList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JceInterfaceInfo.class);
  }

  @Override
  @NotNull
  public List<JceKeyInfo> getKeyInfoList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JceKeyInfo.class);
  }

  @Override
  @NotNull
  public List<JceStructType> getStructTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JceStructType.class);
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
