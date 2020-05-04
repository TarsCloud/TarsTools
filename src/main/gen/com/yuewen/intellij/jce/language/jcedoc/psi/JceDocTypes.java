// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.jcedoc.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.yuewen.intellij.jce.language.jcedoc.psi.impl.*;

public interface JceDocTypes {

  IElementType COMMENT = new JceDocElementType("COMMENT");

  IElementType ALPHA = new JceDocTokenType("ALPHA");
  IElementType DIGIT = new JceDocTokenType("DIGIT");
  IElementType DOC_COMMENT_BAD_CHARACTER = new JceDocTokenType("DOC_COMMENT_BAD_CHARACTER");
  IElementType DOC_COMMENT_DATA = new JceDocTokenType("DOC_COMMENT_DATA");
  IElementType DOC_COMMENT_END = new JceDocTokenType("*/");
  IElementType DOC_COMMENT_LEADING_ASTRISK = new JceDocTokenType("*");
  IElementType DOC_COMMENT_START = new JceDocTokenType("/**");
  IElementType DOC_INLINE_TAG_END = new JceDocTokenType("}");
  IElementType DOC_INLINE_TAG_START = new JceDocTokenType("{");
  IElementType DOC_SPACE = new JceDocTokenType("DOC_SPACE");
  IElementType DOC_TAG_NAME = new JceDocTokenType("DOC_TAG_NAME");
  IElementType DOC_TAG_VALUE_COMMA = new JceDocTokenType(",");
  IElementType DOC_TAG_VALUE_GT = new JceDocTokenType(">");
  IElementType DOC_TAG_VALUE_LPAREN = new JceDocTokenType("DOC_TAG_VALUE_LPAREN");
  IElementType DOC_TAG_VALUE_LT = new JceDocTokenType("<");
  IElementType DOC_TAG_VALUE_RPAREN = new JceDocTokenType("DOC_TAG_VALUE_RPAREN");
  IElementType DOC_TAG_VALUE_SHARP_TOKEN = new JceDocTokenType("DOC_TAG_VALUE_SHARP_TOKEN");
  IElementType DOC_TAG_VALUE_TOKEN = new JceDocTokenType("DOC_TAG_VALUE_TOKEN");
  IElementType IDENTIFIER = new JceDocTokenType("IDENTIFIER");
  IElementType INLINE_TAG_IDENTIFIER = new JceDocTokenType("INLINE_TAG_IDENTIFIER");
  IElementType TAG_DOC_SPACE = new JceDocTokenType("TAG_DOC_SPACE");
  IElementType TAG_IDENTIFIER = new JceDocTokenType("TAG_IDENTIFIER");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == COMMENT) {
        return new JceDocCommentImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
