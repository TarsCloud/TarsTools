// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.jcedoc.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.yuewen.intellij.jce.language.jcedoc.psi.JceDocTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class JceDocParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return file(b, l + 1);
  }

  /* ********************************************************** */
  // DOC_COMMENT_START (DOC_COMMENT_DATA | DOC_SPACE | DOC_TAG_VALUE_TOKEN | DOC_TAG_VALUE_LPAREN | DOC_TAG_VALUE_RPAREN
  // | DOC_TAG_VALUE_SHARP_TOKEN | DOC_TAG_VALUE_COMMA | DOC_TAG_NAME | IDENTIFIER | DOC_TAG_VALUE_GT | DOC_TAG_VALUE_LT | DOC_INLINE_TAG_START
  // TAG_DOC_SPACE | DOC_INLINE_TAG_END | DOC_COMMENT_LEADING_ASTRISK | DOC_COMMENT_BAD_CHARACTER)* DOC_COMMENT_END?
  public static boolean comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment")) return false;
    if (!nextTokenIs(b, DOC_COMMENT_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOC_COMMENT_START);
    r = r && comment_1(b, l + 1);
    r = r && comment_2(b, l + 1);
    exit_section_(b, m, COMMENT, r);
    return r;
  }

  // (DOC_COMMENT_DATA | DOC_SPACE | DOC_TAG_VALUE_TOKEN | DOC_TAG_VALUE_LPAREN | DOC_TAG_VALUE_RPAREN
  // | DOC_TAG_VALUE_SHARP_TOKEN | DOC_TAG_VALUE_COMMA | DOC_TAG_NAME | IDENTIFIER | DOC_TAG_VALUE_GT | DOC_TAG_VALUE_LT | DOC_INLINE_TAG_START
  // TAG_DOC_SPACE | DOC_INLINE_TAG_END | DOC_COMMENT_LEADING_ASTRISK | DOC_COMMENT_BAD_CHARACTER)*
  private static boolean comment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "comment_1", c)) break;
    }
    return true;
  }

  // DOC_COMMENT_DATA | DOC_SPACE | DOC_TAG_VALUE_TOKEN | DOC_TAG_VALUE_LPAREN | DOC_TAG_VALUE_RPAREN
  // | DOC_TAG_VALUE_SHARP_TOKEN | DOC_TAG_VALUE_COMMA | DOC_TAG_NAME | IDENTIFIER | DOC_TAG_VALUE_GT | DOC_TAG_VALUE_LT | DOC_INLINE_TAG_START
  // TAG_DOC_SPACE | DOC_INLINE_TAG_END | DOC_COMMENT_LEADING_ASTRISK | DOC_COMMENT_BAD_CHARACTER
  private static boolean comment_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOC_COMMENT_DATA);
    if (!r) r = consumeToken(b, DOC_SPACE);
    if (!r) r = consumeToken(b, DOC_TAG_VALUE_TOKEN);
    if (!r) r = consumeToken(b, DOC_TAG_VALUE_LPAREN);
    if (!r) r = consumeToken(b, DOC_TAG_VALUE_RPAREN);
    if (!r) r = consumeToken(b, DOC_TAG_VALUE_SHARP_TOKEN);
    if (!r) r = consumeToken(b, DOC_TAG_VALUE_COMMA);
    if (!r) r = consumeToken(b, DOC_TAG_NAME);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, DOC_TAG_VALUE_GT);
    if (!r) r = consumeToken(b, DOC_TAG_VALUE_LT);
    if (!r) r = parseTokens(b, 0, DOC_INLINE_TAG_START, TAG_DOC_SPACE);
    if (!r) r = consumeToken(b, DOC_INLINE_TAG_END);
    if (!r) r = consumeToken(b, DOC_COMMENT_LEADING_ASTRISK);
    if (!r) r = consumeToken(b, DOC_COMMENT_BAD_CHARACTER);
    exit_section_(b, m, null, r);
    return r;
  }

  // DOC_COMMENT_END?
  private static boolean comment_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_2")) return false;
    consumeToken(b, DOC_COMMENT_END);
    return true;
  }

  /* ********************************************************** */
  // comment
  static boolean file(PsiBuilder b, int l) {
    return comment(b, l + 1);
  }

}
