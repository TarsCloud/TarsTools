// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.yuewen.intellij.jce.language.psi.impl.*;

public interface JceTypes {

  IElementType BUILT_IN_TYPES = new JceElementType("BUILT_IN_TYPES");
  IElementType CONST_ASSIGNMENT = new JceElementType("CONST_ASSIGNMENT");
  IElementType CONST_TYPE = new JceElementType("CONST_TYPE");
  IElementType ENUM_MEMBER = new JceElementType("ENUM_MEMBER");
  IElementType ENUM_TYPE = new JceElementType("ENUM_TYPE");
  IElementType FIELD_DEFAULT_ASSIGNMENT = new JceElementType("FIELD_DEFAULT_ASSIGNMENT");
  IElementType FIELD_INFO = new JceElementType("FIELD_INFO");
  IElementType FIELD_LABEL = new JceElementType("FIELD_LABEL");
  IElementType FIELD_TAG = new JceElementType("FIELD_TAG");
  IElementType FIELD_TYPE = new JceElementType("FIELD_TYPE");
  IElementType FIELD_TYPE_MODIFIER = new JceElementType("FIELD_TYPE_MODIFIER");
  IElementType FUNCTION_INFO = new JceElementType("FUNCTION_INFO");
  IElementType FUNCTION_PARAM = new JceElementType("FUNCTION_PARAM");
  IElementType FUNCTION_PARAM_LIST = new JceElementType("FUNCTION_PARAM_LIST");
  IElementType INCLUDE_FILENAME = new JceElementType("INCLUDE_FILENAME");
  IElementType INCLUDE_INFO = new JceElementType("INCLUDE_INFO");
  IElementType INTERFACE_INFO = new JceElementType("INTERFACE_INFO");
  IElementType KEY_INFO = new JceElementType("KEY_INFO");
  IElementType KEY_WORDS = new JceElementType("KEY_WORDS");
  IElementType MAP_TYPE = new JceElementType("MAP_TYPE");
  IElementType MODULE_INFO = new JceElementType("MODULE_INFO");
  IElementType REF_MODULE = new JceElementType("REF_MODULE");
  IElementType REF_STRUCT = new JceElementType("REF_STRUCT");
  IElementType REF_STRUCT_FIELD = new JceElementType("REF_STRUCT_FIELD");
  IElementType RETURN_TYPE = new JceElementType("RETURN_TYPE");
  IElementType STRING_LITERALS = new JceElementType("STRING_LITERALS");
  IElementType STRUCT_TYPE = new JceElementType("STRUCT_TYPE");
  IElementType VECTOR_TYPE = new JceElementType("VECTOR_TYPE");

  IElementType BLOCK_COMMENT = new JceTokenType("BLOCK_COMMENT");
  IElementType BOOL = new JceTokenType("bool");
  IElementType BYTE = new JceTokenType("byte");
  IElementType CLOSE_BLOCK = new JceTokenType("}");
  IElementType CLOSE_BRACE = new JceTokenType("]");
  IElementType CLOSE_PARENTHESIS = new JceTokenType(")");
  IElementType COMMA = new JceTokenType(",");
  IElementType COMMENT = new JceTokenType("comment");
  IElementType CONST = new JceTokenType("const");
  IElementType DOC_COMMENT = new JceTokenType("DOC_COMMENT");
  IElementType DOUBLE = new JceTokenType("double");
  IElementType DOUBLE_COLON = new JceTokenType("::");
  IElementType ENUM = new JceTokenType("enum");
  IElementType EQUAL = new JceTokenType("=");
  IElementType FALSE = new JceTokenType("false");
  IElementType FLOAT = new JceTokenType("float");
  IElementType GREATER_THAN = new JceTokenType(">");
  IElementType HEX_INT = new JceTokenType("HEX_INT");
  IElementType IDENTIFIER = new JceTokenType("IDENTIFIER");
  IElementType INCLUDE = new JceTokenType("#include");
  IElementType INT = new JceTokenType("int");
  IElementType INTERFACE = new JceTokenType("interface");
  IElementType KEY = new JceTokenType("key");
  IElementType LESS_THAN = new JceTokenType("<");
  IElementType LINE_COMMENT = new JceTokenType("LINE_COMMENT");
  IElementType LONG = new JceTokenType("long");
  IElementType MAP = new JceTokenType("map");
  IElementType MINUS = new JceTokenType("-");
  IElementType MODULE = new JceTokenType("module");
  IElementType NUM_DOUBLE = new JceTokenType("NUM_DOUBLE");
  IElementType NUM_INT = new JceTokenType("NUM_INT");
  IElementType OPEN_BLOCK = new JceTokenType("{");
  IElementType OPEN_BRACE = new JceTokenType("[");
  IElementType OPEN_PARENTHESIS = new JceTokenType("(");
  IElementType OPTIONAL = new JceTokenType("optional");
  IElementType OUT = new JceTokenType("out");
  IElementType REQUIRE = new JceTokenType("require");
  IElementType ROUTEKEY = new JceTokenType("routekey");
  IElementType SEMICOLON = new JceTokenType(";");
  IElementType SHORT = new JceTokenType("short");
  IElementType STRING = new JceTokenType("string");
  IElementType STRING_LITERAL = new JceTokenType("STRING_LITERAL");
  IElementType STRUCT = new JceTokenType("struct");
  IElementType TAF_IDENTIFIER = new JceTokenType("TAF_IDENTIFIER");
  IElementType TRUE = new JceTokenType("true");
  IElementType UNSIGNED = new JceTokenType("unsigned");
  IElementType VECTOR = new JceTokenType("vector");
  IElementType VOID = new JceTokenType("void");
  IElementType WRONG_STRING_LITERAL = new JceTokenType("WRONG_STRING_LITERAL");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BUILT_IN_TYPES) {
        return new JceBuiltInTypesImpl(node);
      }
      else if (type == CONST_ASSIGNMENT) {
        return new JceConstAssignmentImpl(node);
      }
      else if (type == CONST_TYPE) {
        return new JceConstTypeImpl(node);
      }
      else if (type == ENUM_MEMBER) {
        return new JceEnumMemberImpl(node);
      }
      else if (type == ENUM_TYPE) {
        return new JceEnumTypeImpl(node);
      }
      else if (type == FIELD_DEFAULT_ASSIGNMENT) {
        return new JceFieldDefaultAssignmentImpl(node);
      }
      else if (type == FIELD_INFO) {
        return new JceFieldInfoImpl(node);
      }
      else if (type == FIELD_LABEL) {
        return new JceFieldLabelImpl(node);
      }
      else if (type == FIELD_TAG) {
        return new JceFieldTagImpl(node);
      }
      else if (type == FIELD_TYPE) {
        return new JceFieldTypeImpl(node);
      }
      else if (type == FIELD_TYPE_MODIFIER) {
        return new JceFieldTypeModifierImpl(node);
      }
      else if (type == FUNCTION_INFO) {
        return new JceFunctionInfoImpl(node);
      }
      else if (type == FUNCTION_PARAM) {
        return new JceFunctionParamImpl(node);
      }
      else if (type == FUNCTION_PARAM_LIST) {
        return new JceFunctionParamListImpl(node);
      }
      else if (type == INCLUDE_FILENAME) {
        return new JceIncludeFilenameImpl(node);
      }
      else if (type == INCLUDE_INFO) {
        return new JceIncludeInfoImpl(node);
      }
      else if (type == INTERFACE_INFO) {
        return new JceInterfaceInfoImpl(node);
      }
      else if (type == KEY_INFO) {
        return new JceKeyInfoImpl(node);
      }
      else if (type == KEY_WORDS) {
        return new JceKeyWordsImpl(node);
      }
      else if (type == MAP_TYPE) {
        return new JceMapTypeImpl(node);
      }
      else if (type == MODULE_INFO) {
        return new JceModuleInfoImpl(node);
      }
      else if (type == REF_MODULE) {
        return new JceRefModuleImpl(node);
      }
      else if (type == REF_STRUCT) {
        return new JceRefStructImpl(node);
      }
      else if (type == REF_STRUCT_FIELD) {
        return new JceRefStructFieldImpl(node);
      }
      else if (type == RETURN_TYPE) {
        return new JceReturnTypeImpl(node);
      }
      else if (type == STRING_LITERALS) {
        return new JceStringLiteralsImpl(node);
      }
      else if (type == STRUCT_TYPE) {
        return new JceStructTypeImpl(node);
      }
      else if (type == VECTOR_TYPE) {
        return new JceVectorTypeImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
