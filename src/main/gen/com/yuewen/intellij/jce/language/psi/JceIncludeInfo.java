// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiReference;

public interface JceIncludeInfo extends PsiElement {

  @Nullable
  JceIncludeFilename getIncludeFilename();

  @NotNull
  String getIncludeFileName();

  @Nullable
  PsiReference getReference();

  @NotNull
  PsiReference[] getReferences();

  @NotNull
  ItemPresentation getPresentation();

}
