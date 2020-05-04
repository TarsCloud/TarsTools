// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface JceModuleInfo extends JceNamedElement {

  @NotNull
  List<JceConstType> getConstTypeList();

  @NotNull
  List<JceEnumType> getEnumTypeList();

  @NotNull
  List<JceInterfaceInfo> getInterfaceInfoList();

  @NotNull
  List<JceKeyInfo> getKeyInfoList();

  @NotNull
  List<JceStructType> getStructTypeList();

  @Nullable
  PsiElement getIdentifier();

  String getName();

  PsiElement getNameIdentifier();

  PsiElement setName(String newName);

  @NotNull
  ItemPresentation getPresentation();

}
