// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface JceFieldType extends PsiElement {

  @Nullable
  JceBuiltInTypes getBuiltInTypes();

  @Nullable
  JceMapType getMapType();

  @Nullable
  JceRefModule getRefModule();

  @Nullable
  JceVectorType getVectorType();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiReference[] getReferences();

  @Nullable
  PsiReference getReference();

  JceFieldType setName(String newName);

  JceFieldType setQualifierName(String newName);

}
