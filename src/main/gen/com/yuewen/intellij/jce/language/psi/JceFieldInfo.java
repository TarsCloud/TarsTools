// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface JceFieldInfo extends JceNamedElement {

  @Nullable
  JceFieldDefaultAssignment getFieldDefaultAssignment();

  @Nullable
  JceFieldLabel getFieldLabel();

  @NotNull
  JceFieldTag getFieldTag();

  @Nullable
  JceFieldType getFieldType();

  @Nullable
  PsiElement getIdentifier();

  String getName();

  PsiElement getNameIdentifier();

  PsiElement setName(String newName);

  @NotNull
  ItemPresentation getPresentation();

}
