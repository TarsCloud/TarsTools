// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface JceEnumMember extends JceNamedElement {

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  PsiElement getNumInt();

  PsiElement getNameIdentifier();

  String getName();

  PsiElement setName(String newName);

  @NotNull
  ItemPresentation getPresentation();

}
