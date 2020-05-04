// This is a generated file. Not intended for manual editing.
package com.yuewen.intellij.jce.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.yuewen.intellij.jce.language.psi.JceTypes.*;
import static com.yuewen.intellij.jce.language.parser.JceParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class JceParser implements PsiParser, LightPsiParser {

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
    return JCE_FILE(b, l + 1);
  }

  /* ********************************************************** */
  // TRUE | FALSE
  static boolean BOOL_VALUES(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BOOL_VALUES")) return false;
    if (!nextTokenIs(b, "", FALSE, TRUE)) return false;
    boolean r;
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    return r;
  }

  /* ********************************************************** */
  // (UNSIGNED UNSIGNED_NUMS) | NUMBER_TYPES | BOOL | STRING
  public static boolean BUILT_IN_TYPES(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BUILT_IN_TYPES")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BUILT_IN_TYPES, "<built in types>");
    r = BUILT_IN_TYPES_0(b, l + 1);
    if (!r) r = NUMBER_TYPES(b, l + 1);
    if (!r) r = consumeToken(b, BOOL);
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // UNSIGNED UNSIGNED_NUMS
  private static boolean BUILT_IN_TYPES_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BUILT_IN_TYPES_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, UNSIGNED);
    r = r && UNSIGNED_NUMS(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EQUAL (BOOL_VALUES | STRING_LITERAL | NUMBERS)
  public static boolean CONST_ASSIGNMENT(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CONST_ASSIGNMENT")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_ASSIGNMENT, "<const assignment>");
    r = consumeToken(b, EQUAL);
    p = r; // pin = 1
    r = r && CONST_ASSIGNMENT_1(b, l + 1);
    exit_section_(b, l, m, r, p, CONST_ASSIGNMENT_RECOVERY_parser_);
    return r || p;
  }

  // BOOL_VALUES | STRING_LITERAL | NUMBERS
  private static boolean CONST_ASSIGNMENT_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CONST_ASSIGNMENT_1")) return false;
    boolean r;
    r = BOOL_VALUES(b, l + 1);
    if (!r) r = consumeToken(b, STRING_LITERAL);
    if (!r) r = NUMBERS(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // !(COMMA | SEMICOLON | CLOSE_BLOCK | STRUCT | ENUM | INTERFACE | CONST)
  static boolean CONST_ASSIGNMENT_RECOVERY(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CONST_ASSIGNMENT_RECOVERY")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !CONST_ASSIGNMENT_RECOVERY_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMMA | SEMICOLON | CLOSE_BLOCK | STRUCT | ENUM | INTERFACE | CONST
  private static boolean CONST_ASSIGNMENT_RECOVERY_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CONST_ASSIGNMENT_RECOVERY_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, CLOSE_BLOCK);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, ENUM);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, CONST);
    return r;
  }

  /* ********************************************************** */
  // CONST BUILT_IN_TYPES IDENTIFIER CONST_ASSIGNMENT SEMICOLON
  public static boolean CONST_TYPE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CONST_TYPE")) return false;
    if (!nextTokenIs(b, CONST)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_TYPE, null);
    r = consumeToken(b, CONST);
    p = r; // pin = 1
    r = r && report_error_(b, BUILT_IN_TYPES(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && report_error_(b, CONST_ASSIGNMENT(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IDENTIFIER (EQUAL NUM_INT)?
  public static boolean ENUM_MEMBER(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_MEMBER")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENUM_MEMBER, "<enum member>");
    r = consumeToken(b, IDENTIFIER);
    p = r; // pin = 1
    r = r && ENUM_MEMBER_1(b, l + 1);
    exit_section_(b, l, m, r, p, ENUM_MEMBER_RECOVERY_parser_);
    return r || p;
  }

  // (EQUAL NUM_INT)?
  private static boolean ENUM_MEMBER_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_MEMBER_1")) return false;
    ENUM_MEMBER_1_0(b, l + 1);
    return true;
  }

  // EQUAL NUM_INT
  private static boolean ENUM_MEMBER_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_MEMBER_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, EQUAL, NUM_INT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(COMMA | SEMICOLON | CLOSE_BLOCK)
  static boolean ENUM_MEMBER_RECOVERY(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_MEMBER_RECOVERY")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ENUM_MEMBER_RECOVERY_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMMA | SEMICOLON | CLOSE_BLOCK
  private static boolean ENUM_MEMBER_RECOVERY_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_MEMBER_RECOVERY_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, CLOSE_BLOCK);
    return r;
  }

  /* ********************************************************** */
  // ENUM IDENTIFIER OPEN_BLOCK (ENUM_MEMBER)?(COMMA ENUM_MEMBER)* (COMMA)? CLOSE_BLOCK SEMICOLON
  public static boolean ENUM_TYPE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_TYPE")) return false;
    if (!nextTokenIs(b, ENUM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENUM_TYPE, null);
    r = consumeTokens(b, 1, ENUM, IDENTIFIER, OPEN_BLOCK);
    p = r; // pin = 1
    r = r && report_error_(b, ENUM_TYPE_3(b, l + 1));
    r = p && report_error_(b, ENUM_TYPE_4(b, l + 1)) && r;
    r = p && report_error_(b, ENUM_TYPE_5(b, l + 1)) && r;
    r = p && report_error_(b, consumeTokens(b, -1, CLOSE_BLOCK, SEMICOLON)) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ENUM_MEMBER)?
  private static boolean ENUM_TYPE_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_TYPE_3")) return false;
    ENUM_TYPE_3_0(b, l + 1);
    return true;
  }

  // (ENUM_MEMBER)
  private static boolean ENUM_TYPE_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_TYPE_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ENUM_MEMBER(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA ENUM_MEMBER)*
  private static boolean ENUM_TYPE_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_TYPE_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ENUM_TYPE_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ENUM_TYPE_4", c)) break;
    }
    return true;
  }

  // COMMA ENUM_MEMBER
  private static boolean ENUM_TYPE_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_TYPE_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ENUM_MEMBER(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA)?
  private static boolean ENUM_TYPE_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ENUM_TYPE_5")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // EQUAL STRING_LITERALS
  public static boolean FIELD_DEFAULT_ASSIGNMENT(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_DEFAULT_ASSIGNMENT")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FIELD_DEFAULT_ASSIGNMENT, "<field default assignment>");
    r = consumeToken(b, EQUAL);
    p = r; // pin = 1
    r = r && STRING_LITERALS(b, l + 1);
    exit_section_(b, l, m, r, p, FIELD_INFO_RECOVERY__parser_);
    return r || p;
  }

  /* ********************************************************** */
  // FIELD_TAG FIELD_LABEL FIELD_TYPE IDENTIFIER FIELD_DEFAULT_ASSIGNMENT? SEMICOLON
  public static boolean FIELD_INFO(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_INFO")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FIELD_INFO, "<field info>");
    r = FIELD_TAG(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, FIELD_LABEL(b, l + 1));
    r = p && report_error_(b, FIELD_TYPE(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && report_error_(b, FIELD_INFO_4(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, FIELD_INFO_RECOVERY__parser_);
    return r || p;
  }

  // FIELD_DEFAULT_ASSIGNMENT?
  private static boolean FIELD_INFO_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_INFO_4")) return false;
    FIELD_DEFAULT_ASSIGNMENT(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !(SEMICOLON | FIELD_TAG (FIELD_LABEL | FIELD_TYPE) | CLOSE_BLOCK | STRUCT | ENUM | CONST | INTERFACE | KEY)
  static boolean FIELD_INFO_RECOVERY_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_INFO_RECOVERY_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !FIELD_INFO_RECOVERY__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON | FIELD_TAG (FIELD_LABEL | FIELD_TYPE) | CLOSE_BLOCK | STRUCT | ENUM | CONST | INTERFACE | KEY
  private static boolean FIELD_INFO_RECOVERY__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_INFO_RECOVERY__0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    if (!r) r = FIELD_INFO_RECOVERY__0_1(b, l + 1);
    if (!r) r = consumeToken(b, CLOSE_BLOCK);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, ENUM);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, KEY);
    exit_section_(b, m, null, r);
    return r;
  }

  // FIELD_TAG (FIELD_LABEL | FIELD_TYPE)
  private static boolean FIELD_INFO_RECOVERY__0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_INFO_RECOVERY__0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FIELD_TAG(b, l + 1);
    r = r && FIELD_INFO_RECOVERY__0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // FIELD_LABEL | FIELD_TYPE
  private static boolean FIELD_INFO_RECOVERY__0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_INFO_RECOVERY__0_1_1")) return false;
    boolean r;
    r = FIELD_LABEL(b, l + 1);
    if (!r) r = FIELD_TYPE(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // REQUIRE | OPTIONAL
  public static boolean FIELD_LABEL(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_LABEL")) return false;
    if (!nextTokenIs(b, "<require or optional>", OPTIONAL, REQUIRE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_LABEL, "<require or optional>");
    r = consumeToken(b, REQUIRE);
    if (!r) r = consumeToken(b, OPTIONAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // NUM_INT
  public static boolean FIELD_TAG(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_TAG")) return false;
    if (!nextTokenIs(b, "<field tag>", NUM_INT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_TAG, "<field tag>");
    r = consumeToken(b, NUM_INT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // BUILT_IN_TYPES | USER_DEFINE_TYPE | MAP_TYPE | VECTOR_TYPE
  public static boolean FIELD_TYPE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_TYPE")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_TYPE, "<field type>");
    r = BUILT_IN_TYPES(b, l + 1);
    if (!r) r = USER_DEFINE_TYPE(b, l + 1);
    if (!r) r = MAP_TYPE(b, l + 1);
    if (!r) r = VECTOR_TYPE(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // OUT
  public static boolean FIELD_TYPE_MODIFIER(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FIELD_TYPE_MODIFIER")) return false;
    if (!nextTokenIs(b, OUT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OUT);
    exit_section_(b, m, FIELD_TYPE_MODIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // RETURN_TYPE IDENTIFIER OPEN_PARENTHESIS FUNCTION_PARAM_LIST? CLOSE_PARENTHESIS SEMICOLON
  public static boolean FUNCTION_INFO(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_INFO")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_INFO, "<function info>");
    r = RETURN_TYPE(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeTokens(b, -1, IDENTIFIER, OPEN_PARENTHESIS));
    r = p && report_error_(b, FUNCTION_INFO_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeTokens(b, -1, CLOSE_PARENTHESIS, SEMICOLON)) && r;
    exit_section_(b, l, m, r, p, FUNCTION_INFO_RECOVERY_parser_);
    return r || p;
  }

  // FUNCTION_PARAM_LIST?
  private static boolean FUNCTION_INFO_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_INFO_3")) return false;
    FUNCTION_PARAM_LIST(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !(COMMA | RETURN_TYPE | CLOSE_BLOCK | SEMICOLON | CLOSE_PARENTHESIS)
  static boolean FUNCTION_INFO_RECOVERY(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_INFO_RECOVERY")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !FUNCTION_INFO_RECOVERY_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMMA | RETURN_TYPE | CLOSE_BLOCK | SEMICOLON | CLOSE_PARENTHESIS
  private static boolean FUNCTION_INFO_RECOVERY_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_INFO_RECOVERY_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = RETURN_TYPE(b, l + 1);
    if (!r) r = consumeToken(b, CLOSE_BLOCK);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, CLOSE_PARENTHESIS);
    return r;
  }

  /* ********************************************************** */
  // FIELD_TYPE_MODIFIER? FIELD_TYPE IDENTIFIER
  public static boolean FUNCTION_PARAM(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_PARAM")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_PARAM, "<function param>");
    r = FUNCTION_PARAM_0(b, l + 1);
    r = r && FIELD_TYPE(b, l + 1);
    p = r; // pin = 2
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, p, FUNCTION_PARAM_RECOVERY_parser_);
    return r || p;
  }

  // FIELD_TYPE_MODIFIER?
  private static boolean FUNCTION_PARAM_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_PARAM_0")) return false;
    FIELD_TYPE_MODIFIER(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // FUNCTION_PARAM (COMMA FUNCTION_PARAM)*
  public static boolean FUNCTION_PARAM_LIST(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_PARAM_LIST")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_PARAM_LIST, "<function param list>");
    r = FUNCTION_PARAM(b, l + 1);
    r = r && FUNCTION_PARAM_LIST_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA FUNCTION_PARAM)*
  private static boolean FUNCTION_PARAM_LIST_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_PARAM_LIST_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FUNCTION_PARAM_LIST_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FUNCTION_PARAM_LIST_1", c)) break;
    }
    return true;
  }

  // COMMA FUNCTION_PARAM
  private static boolean FUNCTION_PARAM_LIST_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_PARAM_LIST_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && FUNCTION_PARAM(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(COMMA | OUT | CLOSE_PARENTHESIS | CLOSE_BLOCK | SEMICOLON | FIELD_TYPE)
  static boolean FUNCTION_PARAM_RECOVERY(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_PARAM_RECOVERY")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !FUNCTION_PARAM_RECOVERY_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMMA | OUT | CLOSE_PARENTHESIS | CLOSE_BLOCK | SEMICOLON | FIELD_TYPE
  private static boolean FUNCTION_PARAM_RECOVERY_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FUNCTION_PARAM_RECOVERY_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, OUT);
    if (!r) r = consumeToken(b, CLOSE_PARENTHESIS);
    if (!r) r = consumeToken(b, CLOSE_BLOCK);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = FIELD_TYPE(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // STRING_LITERAL
  public static boolean INCLUDE_FILENAME(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "INCLUDE_FILENAME")) return false;
    if (!nextTokenIs(b, STRING_LITERAL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING_LITERAL);
    exit_section_(b, m, INCLUDE_FILENAME, r);
    return r;
  }

  /* ********************************************************** */
  // INCLUDE INCLUDE_FILENAME
  public static boolean INCLUDE_INFO(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "INCLUDE_INFO")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INCLUDE_INFO, "<include info>");
    r = consumeToken(b, INCLUDE);
    p = r; // pin = 1
    r = r && INCLUDE_FILENAME(b, l + 1);
    exit_section_(b, l, m, r, p, INCLUDE_RECOVERY__parser_);
    return r || p;
  }

  /* ********************************************************** */
  // !(MODULE | INCLUDE | IDENTIFIER)
  static boolean INCLUDE_RECOVERY_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "INCLUDE_RECOVERY_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !INCLUDE_RECOVERY__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // MODULE | INCLUDE | IDENTIFIER
  private static boolean INCLUDE_RECOVERY__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "INCLUDE_RECOVERY__0")) return false;
    boolean r;
    r = consumeToken(b, MODULE);
    if (!r) r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, IDENTIFIER);
    return r;
  }

  /* ********************************************************** */
  // INTERFACE IDENTIFIER OPEN_BLOCK (FUNCTION_INFO)* CLOSE_BLOCK SEMICOLON
  public static boolean INTERFACE_INFO(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "INTERFACE_INFO")) return false;
    if (!nextTokenIs(b, INTERFACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INTERFACE_INFO, null);
    r = consumeTokens(b, 1, INTERFACE, IDENTIFIER, OPEN_BLOCK);
    p = r; // pin = 1
    r = r && report_error_(b, INTERFACE_INFO_3(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, CLOSE_BLOCK, SEMICOLON)) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (FUNCTION_INFO)*
  private static boolean INTERFACE_INFO_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "INTERFACE_INFO_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!INTERFACE_INFO_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "INTERFACE_INFO_3", c)) break;
    }
    return true;
  }

  // (FUNCTION_INFO)
  private static boolean INTERFACE_INFO_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "INTERFACE_INFO_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FUNCTION_INFO(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ROOT_ITEMS*
  static boolean JCE_FILE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JCE_FILE")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ROOT_ITEMS(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "JCE_FILE", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // KEY OPEN_BRACE REF_STRUCT (COMMA REF_STRUCT_FIELD)* CLOSE_BRACE SEMICOLON
  public static boolean KEY_INFO(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KEY_INFO")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, KEY_INFO, null);
    r = consumeTokens(b, 1, KEY, OPEN_BRACE);
    p = r; // pin = 1
    r = r && report_error_(b, REF_STRUCT(b, l + 1));
    r = p && report_error_(b, KEY_INFO_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeTokens(b, -1, CLOSE_BRACE, SEMICOLON)) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA REF_STRUCT_FIELD)*
  private static boolean KEY_INFO_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KEY_INFO_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!KEY_INFO_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "KEY_INFO_3", c)) break;
    }
    return true;
  }

  // COMMA REF_STRUCT_FIELD
  private static boolean KEY_INFO_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KEY_INFO_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && REF_STRUCT_FIELD(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VOID | BYTE | SHORT | BOOL | INT | LONG | FLOAT | DOUBLE | STRING | MAP | VECTOR |
  //                         TRUE | FALSE | UNSIGNED | CONST | OUT | ROUTEKEY | TAF_IDENTIFIER | INCLUDE
  //                         STRUCT | KEY | MODULE | INTERFACE | ENUM | REQUIRE | OPTIONAL
  public static boolean KEY_WORDS(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KEY_WORDS")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, KEY_WORDS, "<key words>");
    r = consumeToken(b, VOID);
    if (!r) r = consumeToken(b, BYTE);
    if (!r) r = consumeToken(b, SHORT);
    if (!r) r = consumeToken(b, BOOL);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, LONG);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, DOUBLE);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, MAP);
    if (!r) r = consumeToken(b, VECTOR);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, UNSIGNED);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, OUT);
    if (!r) r = consumeToken(b, ROUTEKEY);
    if (!r) r = consumeToken(b, TAF_IDENTIFIER);
    if (!r) r = parseTokens(b, 0, INCLUDE, STRUCT);
    if (!r) r = consumeToken(b, KEY);
    if (!r) r = consumeToken(b, MODULE);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, ENUM);
    if (!r) r = consumeToken(b, REQUIRE);
    if (!r) r = consumeToken(b, OPTIONAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MAP LESS_THAN FIELD_TYPE COMMA FIELD_TYPE GREATER_THAN
  public static boolean MAP_TYPE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MAP_TYPE")) return false;
    if (!nextTokenIs(b, MAP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MAP_TYPE, null);
    r = consumeTokens(b, 1, MAP, LESS_THAN);
    p = r; // pin = 1
    r = r && report_error_(b, FIELD_TYPE(b, l + 1));
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, FIELD_TYPE(b, l + 1)) && r;
    r = p && consumeToken(b, GREATER_THAN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // MODULE IDENTIFIER OPEN_BLOCK (MODULE_MEMBER)* CLOSE_BLOCK SEMICOLON
  public static boolean MODULE_INFO(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MODULE_INFO")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODULE_INFO, "<module info>");
    r = consumeTokens(b, 1, MODULE, IDENTIFIER, OPEN_BLOCK);
    p = r; // pin = 1
    r = r && report_error_(b, MODULE_INFO_3(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, CLOSE_BLOCK, SEMICOLON)) && r;
    exit_section_(b, l, m, r, p, MODULE_INFO_RECOVERY__parser_);
    return r || p;
  }

  // (MODULE_MEMBER)*
  private static boolean MODULE_INFO_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MODULE_INFO_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MODULE_INFO_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MODULE_INFO_3", c)) break;
    }
    return true;
  }

  // (MODULE_MEMBER)
  private static boolean MODULE_INFO_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MODULE_INFO_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = MODULE_MEMBER(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(INCLUDE | MODULE)
  static boolean MODULE_INFO_RECOVERY_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MODULE_INFO_RECOVERY_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !MODULE_INFO_RECOVERY__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // INCLUDE | MODULE
  private static boolean MODULE_INFO_RECOVERY__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MODULE_INFO_RECOVERY__0")) return false;
    boolean r;
    r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, MODULE);
    return r;
  }

  /* ********************************************************** */
  // STRUCT_TYPE | KEY_INFO | ENUM_TYPE | CONST_TYPE | INTERFACE_INFO
  static boolean MODULE_MEMBER(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MODULE_MEMBER")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = STRUCT_TYPE(b, l + 1);
    if (!r) r = KEY_INFO(b, l + 1);
    if (!r) r = ENUM_TYPE(b, l + 1);
    if (!r) r = CONST_TYPE(b, l + 1);
    if (!r) r = INTERFACE_INFO(b, l + 1);
    exit_section_(b, l, m, r, false, MODULE_MEMBER_RECOVERY__parser_);
    return r;
  }

  /* ********************************************************** */
  // !(STRUCT | ENUM | CONST | INTERFACE | KEY | IDENTIFIER | CLOSE_BLOCK | INCLUDE | MODULE)
  static boolean MODULE_MEMBER_RECOVERY_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MODULE_MEMBER_RECOVERY_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !MODULE_MEMBER_RECOVERY__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // STRUCT | ENUM | CONST | INTERFACE | KEY | IDENTIFIER | CLOSE_BLOCK | INCLUDE | MODULE
  private static boolean MODULE_MEMBER_RECOVERY__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MODULE_MEMBER_RECOVERY__0")) return false;
    boolean r;
    r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, ENUM);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, KEY);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, CLOSE_BLOCK);
    if (!r) r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, MODULE);
    return r;
  }

  /* ********************************************************** */
  // NUM_INT | NUM_DOUBLE | HEX_INT
  static boolean NUMBERS(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NUMBERS")) return false;
    boolean r;
    r = consumeToken(b, NUM_INT);
    if (!r) r = consumeToken(b, NUM_DOUBLE);
    if (!r) r = consumeToken(b, HEX_INT);
    return r;
  }

  /* ********************************************************** */
  // BYTE | SHORT | INT | LONG | FLOAT | DOUBLE
  static boolean NUMBER_TYPES(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NUMBER_TYPES")) return false;
    boolean r;
    r = consumeToken(b, BYTE);
    if (!r) r = consumeToken(b, SHORT);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, LONG);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, DOUBLE);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean REF_MODULE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "REF_MODULE")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, REF_MODULE, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean REF_STRUCT(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "REF_STRUCT")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, REF_STRUCT, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean REF_STRUCT_FIELD(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "REF_STRUCT_FIELD")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, REF_STRUCT_FIELD, r);
    return r;
  }

  /* ********************************************************** */
  // VOID | FIELD_TYPE
  public static boolean RETURN_TYPE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RETURN_TYPE")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RETURN_TYPE, "<return type>");
    r = consumeToken(b, VOID);
    if (!r) r = FIELD_TYPE(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // INCLUDE_INFO | MODULE_INFO
  static boolean ROOT_ITEMS(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ROOT_ITEMS")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = INCLUDE_INFO(b, l + 1);
    if (!r) r = MODULE_INFO(b, l + 1);
    exit_section_(b, l, m, r, false, ROOT_ITEMS_RECOVERY__parser_);
    return r;
  }

  /* ********************************************************** */
  // !(INCLUDE | STRING_LITERALS | MODULE_INFO | INCLUDE_INFO | SEMICOLON | IDENTIFIER)
  static boolean ROOT_ITEMS_RECOVERY_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ROOT_ITEMS_RECOVERY_")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ROOT_ITEMS_RECOVERY__0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // INCLUDE | STRING_LITERALS | MODULE_INFO | INCLUDE_INFO | SEMICOLON | IDENTIFIER
  private static boolean ROOT_ITEMS_RECOVERY__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ROOT_ITEMS_RECOVERY__0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INCLUDE);
    if (!r) r = STRING_LITERALS(b, l + 1);
    if (!r) r = MODULE_INFO(b, l + 1);
    if (!r) r = INCLUDE_INFO(b, l + 1);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // NUMBERS | IDENTIFIER | STRING_LITERAL | BOOL_VALUES | WRONG_STRING_LITERAL {}
  public static boolean STRING_LITERALS(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "STRING_LITERALS")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING_LITERALS, "<string literals>");
    r = NUMBERS(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, STRING_LITERAL);
    if (!r) r = BOOL_VALUES(b, l + 1);
    if (!r) r = STRING_LITERALS_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // WRONG_STRING_LITERAL {}
  private static boolean STRING_LITERALS_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "STRING_LITERALS_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WRONG_STRING_LITERAL);
    r = r && STRING_LITERALS_4_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // {}
  private static boolean STRING_LITERALS_4_1(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // STRUCT IDENTIFIER OPEN_BLOCK (FIELD_INFO)* CLOSE_BLOCK SEMICOLON
  public static boolean STRUCT_TYPE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "STRUCT_TYPE")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_TYPE, null);
    r = consumeTokens(b, 1, STRUCT, IDENTIFIER, OPEN_BLOCK);
    p = r; // pin = 1
    r = r && report_error_(b, STRUCT_TYPE_3(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, CLOSE_BLOCK, SEMICOLON)) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (FIELD_INFO)*
  private static boolean STRUCT_TYPE_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "STRUCT_TYPE_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!STRUCT_TYPE_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "STRUCT_TYPE_3", c)) break;
    }
    return true;
  }

  // (FIELD_INFO)
  private static boolean STRUCT_TYPE_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "STRUCT_TYPE_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FIELD_INFO(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BYTE | SHORT | INT
  static boolean UNSIGNED_NUMS(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UNSIGNED_NUMS")) return false;
    boolean r;
    r = consumeToken(b, BYTE);
    if (!r) r = consumeToken(b, SHORT);
    if (!r) r = consumeToken(b, INT);
    return r;
  }

  /* ********************************************************** */
  // (REF_MODULE DOUBLE_COLON)? IDENTIFIER
  static boolean USER_DEFINE_TYPE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "USER_DEFINE_TYPE")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = USER_DEFINE_TYPE_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  // (REF_MODULE DOUBLE_COLON)?
  private static boolean USER_DEFINE_TYPE_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "USER_DEFINE_TYPE_0")) return false;
    USER_DEFINE_TYPE_0_0(b, l + 1);
    return true;
  }

  // REF_MODULE DOUBLE_COLON
  private static boolean USER_DEFINE_TYPE_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "USER_DEFINE_TYPE_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = REF_MODULE(b, l + 1);
    r = r && consumeToken(b, DOUBLE_COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VECTOR LESS_THAN FIELD_TYPE GREATER_THAN
  public static boolean VECTOR_TYPE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VECTOR_TYPE")) return false;
    if (!nextTokenIs(b, VECTOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VECTOR_TYPE, null);
    r = consumeTokens(b, 1, VECTOR, LESS_THAN);
    p = r; // pin = 1
    r = r && report_error_(b, FIELD_TYPE(b, l + 1));
    r = p && consumeToken(b, GREATER_THAN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  static final Parser CONST_ASSIGNMENT_RECOVERY_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return CONST_ASSIGNMENT_RECOVERY(b, l + 1);
    }
  };
  static final Parser ENUM_MEMBER_RECOVERY_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return ENUM_MEMBER_RECOVERY(b, l + 1);
    }
  };
  static final Parser FIELD_INFO_RECOVERY__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return FIELD_INFO_RECOVERY_(b, l + 1);
    }
  };
  static final Parser FUNCTION_INFO_RECOVERY_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return FUNCTION_INFO_RECOVERY(b, l + 1);
    }
  };
  static final Parser FUNCTION_PARAM_RECOVERY_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return FUNCTION_PARAM_RECOVERY(b, l + 1);
    }
  };
  static final Parser INCLUDE_RECOVERY__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return INCLUDE_RECOVERY_(b, l + 1);
    }
  };
  static final Parser MODULE_INFO_RECOVERY__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return MODULE_INFO_RECOVERY_(b, l + 1);
    }
  };
  static final Parser MODULE_MEMBER_RECOVERY__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return MODULE_MEMBER_RECOVERY_(b, l + 1);
    }
  };
  static final Parser ROOT_ITEMS_RECOVERY__parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return ROOT_ITEMS_RECOVERY_(b, l + 1);
    }
  };
}
