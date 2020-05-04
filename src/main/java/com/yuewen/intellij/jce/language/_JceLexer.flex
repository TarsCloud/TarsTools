package com.yuewen.intellij.jce.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.yuewen.intellij.jce.language.psi.JceElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.yuewen.intellij.jce.language.psi.JceTypes.*;

%%

%{
  public JceLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class JceLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL = \r|\n|\r\n
WHITE_SPACE=\s+

DIGIT = [0-9]
HEX_DIGIT = [A-Fa-f]|{DIGIT}

IDENTIFIER = [:jletter:] [:jletterdigit:]*
TAF_IDENTIFIER = "taf_"({IDENTIFIER}|{DIGIT})*

LINE_COMMENT = "//"[^\r\n]*
//DOC_COMMENT = "/**"([^"*"]|{EOL}|("*"+([^"*""/"]|{EOL})))*"*"*"*/"
DOC_COMMENT="/*""*"+("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
BLOCK_COMMENT = "/*"([^"*"]|{EOL}|("*"+([^"*""/"]|{EOL})))*"*"*"*/"

HEX_INT = "0x"{HEX_DIGIT}+
NUM_INT = "-"?{DIGIT}{DIGIT}*
NUM_DOUBLE = "-"?({DIGIT}+(("."{DIGIT}+)|((e|E)"-"?{DIGIT}+)))

STRING_DOUBLE_QUOTED = \"(\\\"|[^\"\r\n])*(\")
WRONG_STRING_DOUBLE_QUOTED = \"[^\"\r\n]*

%state YYINITIAL
%state WAITING_VALUE

%%
{LINE_COMMENT}                      { yybegin(YYINITIAL); return LINE_COMMENT; }
{DOC_COMMENT}                       { yybegin(YYINITIAL); return JceElementType.DOC_COMMENT; }
{BLOCK_COMMENT}                     { yybegin(YYINITIAL); return BLOCK_COMMENT; }
{WHITE_SPACE}                       { yybegin(YYINITIAL); return WHITE_SPACE; }

{STRING_DOUBLE_QUOTED}              { yybegin(YYINITIAL); return STRING_LITERAL; }
{WRONG_STRING_DOUBLE_QUOTED}        { yybegin(YYINITIAL); return WRONG_STRING_LITERAL; }
//numbers
{NUM_INT}                           { yybegin(YYINITIAL); return NUM_INT; }
{HEX_INT}                           { yybegin(YYINITIAL);return HEX_INT; }
{NUM_DOUBLE}                        { yybegin(YYINITIAL); return NUM_DOUBLE; }
";"                                 { yybegin(YYINITIAL); return SEMICOLON; }
","                                 { yybegin(YYINITIAL); return COMMA; }

"<"                                 { yybegin(YYINITIAL); return LESS_THAN; }
"("                                 { yybegin(YYINITIAL); return OPEN_PARENTHESIS; }
"["                                 { yybegin(YYINITIAL); return OPEN_BRACE; }
"{"                                 { yybegin(YYINITIAL); return OPEN_BLOCK; }
">"                                 { yybegin(YYINITIAL); return GREATER_THAN; }
")"                                 { yybegin(YYINITIAL); return CLOSE_PARENTHESIS; }
"]"                                 { yybegin(YYINITIAL); return CLOSE_BRACE; }
"}"                                 { yybegin(YYINITIAL); return CLOSE_BLOCK; }
"true"                              { yybegin(YYINITIAL); return TRUE; }
"false"                             { yybegin(YYINITIAL); return FALSE; }
<YYINITIAL> {

  "="                                 { yybegin(WAITING_VALUE); return EQUAL; }
  "-"                                 { yybegin(WAITING_VALUE); return MINUS; }
  "::"                                { yybegin(WAITING_VALUE); return DOUBLE_COLON; }

  "#include"                          { yybegin(WAITING_VALUE); return INCLUDE; }
  "module"                            { yybegin(WAITING_VALUE); return MODULE; }
  "interface"                         { yybegin(WAITING_VALUE); return INTERFACE; }
  "key"                               { yybegin(WAITING_VALUE); return KEY; }
  "const"                             { yybegin(WAITING_VALUE); return CONST; }
  "enum"                              { yybegin(WAITING_VALUE); return ENUM; }
  "struct"                            { yybegin(WAITING_VALUE); return STRUCT; }

  "void"                              { yybegin(WAITING_VALUE); return VOID; }
  "out"                               { yybegin(WAITING_VALUE); return OUT; }

  "require"                           { yybegin(WAITING_VALUE); return REQUIRE; }
  "optional"                          { yybegin(WAITING_VALUE); return OPTIONAL; }

}
"bool"                              { yybegin(WAITING_VALUE); return BOOL; }
"byte"                              { yybegin(WAITING_VALUE); return BYTE; }
"short"                             { yybegin(WAITING_VALUE); return SHORT; }
"int"                               { yybegin(WAITING_VALUE); return INT; }
"long"                              { yybegin(WAITING_VALUE); return LONG; }
"float"                             { yybegin(WAITING_VALUE); return FLOAT; }
"double"                            { yybegin(WAITING_VALUE); return DOUBLE; }
"string"                            { yybegin(WAITING_VALUE); return STRING; }
"vector"                            { yybegin(WAITING_VALUE); return VECTOR; }
"map"                               { yybegin(WAITING_VALUE); return MAP; }

"unsigned"                          { yybegin(WAITING_VALUE); return UNSIGNED; }
"routekey"                          { yybegin(YYINITIAL); return ROUTEKEY; }

{TAF_IDENTIFIER}                    { yybegin(YYINITIAL); return TAF_IDENTIFIER; }
{IDENTIFIER}                        { yybegin(YYINITIAL); return IDENTIFIER; }

[^] { return BAD_CHARACTER; }
