// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class JceVisitor extends PsiElementVisitor {

  public void visitBuiltInTypes(@NotNull JceBuiltInTypes o) {
    visitPsiElement(o);
  }

  public void visitConstAssignment(@NotNull JceConstAssignment o) {
    visitPsiElement(o);
  }

  public void visitConstType(@NotNull JceConstType o) {
    visitNamedElement(o);
  }

  public void visitEnumMember(@NotNull JceEnumMember o) {
    visitNamedElement(o);
  }

  public void visitEnumType(@NotNull JceEnumType o) {
    visitNamedElement(o);
  }

  public void visitFieldDefaultAssignment(@NotNull JceFieldDefaultAssignment o) {
    visitPsiElement(o);
  }

  public void visitFieldInfo(@NotNull JceFieldInfo o) {
    visitNamedElement(o);
  }

  public void visitFieldLabel(@NotNull JceFieldLabel o) {
    visitPsiElement(o);
  }

  public void visitFieldTag(@NotNull JceFieldTag o) {
    visitPsiElement(o);
  }

  public void visitFieldType(@NotNull JceFieldType o) {
    visitPsiElement(o);
  }

  public void visitFieldTypeModifier(@NotNull JceFieldTypeModifier o) {
    visitPsiElement(o);
  }

  public void visitFunctionInfo(@NotNull JceFunctionInfo o) {
    visitNamedElement(o);
  }

  public void visitFunctionParam(@NotNull JceFunctionParam o) {
    visitNamedElement(o);
  }

  public void visitFunctionParamList(@NotNull JceFunctionParamList o) {
    visitPsiElement(o);
  }

  public void visitIncludeFilename(@NotNull JceIncludeFilename o) {
    visitPsiElement(o);
  }

  public void visitIncludeInfo(@NotNull JceIncludeInfo o) {
    visitPsiElement(o);
  }

  public void visitInterfaceInfo(@NotNull JceInterfaceInfo o) {
    visitNamedElement(o);
  }

  public void visitKeyInfo(@NotNull JceKeyInfo o) {
    visitPsiElement(o);
  }

  public void visitKeyWords(@NotNull JceKeyWords o) {
    visitPsiElement(o);
  }

  public void visitMapType(@NotNull JceMapType o) {
    visitPsiElement(o);
  }

  public void visitModuleInfo(@NotNull JceModuleInfo o) {
    visitNamedElement(o);
  }

  public void visitRefModule(@NotNull JceRefModule o) {
    visitPsiElement(o);
  }

  public void visitRefStruct(@NotNull JceRefStruct o) {
    visitPsiElement(o);
  }

  public void visitRefStructField(@NotNull JceRefStructField o) {
    visitPsiElement(o);
  }

  public void visitReturnType(@NotNull JceReturnType o) {
    visitPsiElement(o);
  }

  public void visitStringLiterals(@NotNull JceStringLiterals o) {
    visitPsiElement(o);
  }

  public void visitStructType(@NotNull JceStructType o) {
    visitNamedElement(o);
  }

  public void visitVectorType(@NotNull JceVectorType o) {
    visitPsiElement(o);
  }

  public void visitNamedElement(@NotNull JceNamedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
