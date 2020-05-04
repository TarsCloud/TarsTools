// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface JceRefModule extends PsiElement {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  PsiReference[] getReferences();

  @Nullable
  PsiReference getReference();

  JceRefModule setName(String newName);

}
