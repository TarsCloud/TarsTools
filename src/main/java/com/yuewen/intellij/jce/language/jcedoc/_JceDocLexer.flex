/* It's an automatically generated code. Do not modify it. */
package com.yuewen.intellij.jce.language.jcedoc;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import static com.intellij.psi.TokenType.*;
import static com.yuewen.intellij.jce.language.jcedoc.psi.JceDocTypes.*;

@SuppressWarnings("ALL")
%%

%{
  public JceDocCommentLexer() {
    this((java.io.Reader)null);
  }

  public boolean checkAhead(char c) {
    if (zzMarkedPos >= zzBuffer.length()) return false;
    return zzBuffer.charAt(zzMarkedPos) == c;
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzAtEOF = false;
  }
%}

%public
%class JceDocCommentLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%state COMMENT_DATA_START
%state COMMENT_DATA
%state TAG_DOC_SPACE
%state PARAM_TAG_SPACE
%state DOC_TAG_VALUE
%state DOC_TAG_VALUE_IN_PAREN
%state DOC_TAG_VALUE_IN_LTGT
%state INLINE_TAG_NAME
%state CODE_TAG
%state CODE_TAG_SPACE
%state COMMENT_DATA_LINE_START
%state COMMENT_DATA_LINE_LEADING

WHITE_DOC_SPACE_CHAR=[\ \t\f\n\r]
WHITE_DOC_SPACE_NO_LR=[\ \t\f]
DIGIT=[0-9]
ALPHA=[:jletter:]
IDENTIFIER={ALPHA}({ALPHA}|{DIGIT}|[":.-"])*
TAG_IDENTIFIER=[^\ \t\f\n\r]+
INLINE_TAG_IDENTIFIER=[^\ \t\f\n\r\}]+

%%

<YYINITIAL> "/**" { yybegin(COMMENT_DATA_START); return DOC_COMMENT_START; }
<COMMENT_DATA_START> {WHITE_DOC_SPACE_CHAR}+ { return DOC_SPACE; }
<COMMENT_DATA> {WHITE_DOC_SPACE_NO_LR}+ { return DOC_COMMENT_DATA; }
<COMMENT_DATA, COMMENT_DATA_LINE_START, COMMENT_DATA_LINE_LEADING> {WHITE_DOC_SPACE_CHAR}*[\n\r]+{WHITE_DOC_SPACE_CHAR}* { yybegin(COMMENT_DATA_LINE_START); return DOC_SPACE; }
<COMMENT_DATA_LINE_START> "*" { yybegin(COMMENT_DATA_LINE_LEADING); return DOC_COMMENT_LEADING_ASTRISK; }
<COMMENT_DATA_LINE_LEADING> {WHITE_DOC_SPACE_NO_LR}+ { yybegin(COMMENT_DATA); return DOC_SPACE; }

<DOC_TAG_VALUE> {WHITE_DOC_SPACE_CHAR}+ { yybegin(COMMENT_DATA); return DOC_SPACE; }
<DOC_TAG_VALUE, DOC_TAG_VALUE_IN_PAREN> ({ALPHA}|[_0-9\."$"\[\]])+ { return DOC_TAG_VALUE_TOKEN; }
<DOC_TAG_VALUE> [\(] { yybegin(DOC_TAG_VALUE_IN_PAREN); return DOC_TAG_VALUE_LPAREN; }
<DOC_TAG_VALUE_IN_PAREN> [\)] { yybegin(DOC_TAG_VALUE); return DOC_TAG_VALUE_RPAREN; }
<DOC_TAG_VALUE> [#] { return DOC_TAG_VALUE_SHARP_TOKEN; }
<DOC_TAG_VALUE, DOC_TAG_VALUE_IN_PAREN> [,] { return DOC_TAG_VALUE_COMMA; }
<DOC_TAG_VALUE_IN_PAREN> {WHITE_DOC_SPACE_CHAR}+ { return DOC_SPACE; }

<INLINE_TAG_NAME, COMMENT_DATA_START, COMMENT_DATA_LINE_START> "@param" { yybegin(PARAM_TAG_SPACE); return DOC_TAG_NAME; }
<PARAM_TAG_SPACE> {WHITE_DOC_SPACE_CHAR}+ {yybegin(DOC_TAG_VALUE); return DOC_SPACE; }
<DOC_TAG_VALUE> [\<] {
    yybegin(DOC_TAG_VALUE_IN_LTGT);
    return DOC_TAG_VALUE_LT;
}
<DOC_TAG_VALUE_IN_LTGT> {IDENTIFIER} { return DOC_TAG_VALUE_TOKEN; }
<DOC_TAG_VALUE_IN_LTGT> {IDENTIFIER} { return DOC_TAG_VALUE_TOKEN; }
<DOC_TAG_VALUE_IN_LTGT> [\>] { yybegin(COMMENT_DATA); return DOC_TAG_VALUE_GT; }

<COMMENT_DATA_START, COMMENT_DATA, CODE_TAG, COMMENT_DATA_LINE_START> "{" {
  if (checkAhead('@')) {
    yybegin(INLINE_TAG_NAME);
    return DOC_INLINE_TAG_START;
  }
  else {
    yybegin(COMMENT_DATA);
    return DOC_INLINE_TAG_START;
  }
}

<INLINE_TAG_NAME> "@code" { yybegin(CODE_TAG_SPACE); return DOC_TAG_NAME; }
<INLINE_TAG_NAME> "@literal" { yybegin(CODE_TAG_SPACE); return DOC_TAG_NAME; }
<INLINE_TAG_NAME> "@"{INLINE_TAG_IDENTIFIER} { yybegin(TAG_DOC_SPACE); return DOC_TAG_NAME; }
<COMMENT_DATA_START, COMMENT_DATA_LINE_START, COMMENT_DATA, TAG_DOC_SPACE, DOC_TAG_VALUE, CODE_TAG, CODE_TAG_SPACE> "}" { yybegin(COMMENT_DATA); return DOC_INLINE_TAG_END; }

<COMMENT_DATA_START, COMMENT_DATA_LINE_START, COMMENT_DATA, DOC_TAG_VALUE> . { yybegin(COMMENT_DATA); return DOC_COMMENT_DATA; }
<CODE_TAG, CODE_TAG_SPACE> . { yybegin(CODE_TAG); return DOC_COMMENT_DATA; }
<COMMENT_DATA_START> "@"{TAG_IDENTIFIER} { yybegin(TAG_DOC_SPACE); return DOC_TAG_NAME; }

<TAG_DOC_SPACE> {WHITE_DOC_SPACE_CHAR}+ {
  if (checkAhead('<') || checkAhead('\"')) yybegin(COMMENT_DATA);
  else if (checkAhead('\u007b')) yybegin(COMMENT_DATA);  // lbrace - there's a error in JLex when typing lbrace directly
  else yybegin(DOC_TAG_VALUE);
  return DOC_SPACE;
}

<CODE_TAG, CODE_TAG_SPACE> {WHITE_DOC_SPACE_CHAR}+ { yybegin(CODE_TAG); return DOC_SPACE; }

"*"+"/" { return DOC_COMMENT_END; }
[^] { return DOC_COMMENT_BAD_CHARACTER; }
