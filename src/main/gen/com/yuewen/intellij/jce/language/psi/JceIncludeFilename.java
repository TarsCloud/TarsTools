// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface JceIncludeFilename extends PsiElement {

  @NotNull
  PsiElement getStringLiteral();

  @NotNull
  String getFileName();

  JceIncludeFilename setName(String newName);

  @Nullable
  PsiReference getReference();

  @NotNull
  PsiReference[] getReferences();

}
