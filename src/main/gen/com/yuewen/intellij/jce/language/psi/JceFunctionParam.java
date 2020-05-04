// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface JceFunctionParam extends JceNamedElement {

  @NotNull
  JceFieldType getFieldType();

  @Nullable
  JceFieldTypeModifier getFieldTypeModifier();

  @Nullable
  PsiElement getIdentifier();

  PsiElement getNameIdentifier();

  PsiElement setName(String newName);

  String getName();

  @NotNull
  ItemPresentation getPresentation();

}
