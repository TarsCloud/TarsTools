package com.tencent.jceplugin;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;

import com.tencent.jceplugin.psi.JceTypes;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.tencent.jceplugin.JceParserDefinition.*;

%%

%class JceLexer
%implements FlexLexer, JceTypes
%unicode
%public

%function advance
%type IElementType

%eof{  return;
%eof}

NL = \R
WS = [ \t\f]

LINE_COMMENT = "//" [^\r\n]*
MULTILINE_COMMENT = "/*" ( ([^"*"]|[\r\n])* ("*"+ [^"*""/"] )? )* ("*" | "*"+"/")?

LETTER = [:letter:] | "_"
DIGIT =  [:digit:]

HEX_DIGIT = [0-9A-Fa-f]
INT_DIGIT = [0-9]
OCT_DIGIT = [0-7]

NUM_INT = "0" | ([1-9] {INT_DIGIT}*)
NUM_HEX = ("0x" | "0X") {HEX_DIGIT}+
NUM_OCT = "0" {OCT_DIGIT}+

FLOAT_EXPONENT = [eE] [+-]? {DIGIT}+
NUM_FLOAT = ( ( ({DIGIT}+ "." {DIGIT}*) | ({DIGIT}* "." {DIGIT}+) ) {FLOAT_EXPONENT}?) | ({DIGIT}+ {FLOAT_EXPONENT})

IDENT = {LETTER} ({LETTER} | {DIGIT} )*

STR =      "\""
STRING = {STR} ( [^\"\\\n\r] | "\\" ("\\" | {STR} | {ESCAPES} | [0-8xuU] ) )* {STR}?
ESCAPES = [abfnrtv]

%state MAYBE_SEMICOLON

%%

<YYINITIAL> {
{WS}                                      { return WS; }
{NL}+                                     { return NLS; }

{LINE_COMMENT}                            { return LINE_COMMENT; }
{MULTILINE_COMMENT}                       { return MULTILINE_COMMENT; }

{STRING}                                  { yybegin(MAYBE_SEMICOLON); return STRING; }

"{"                                       { return LBRACE; }
"}"                                       { yybegin(MAYBE_SEMICOLON); return RBRACE; }
"["                                       { return LBRACK; }
"]"                                       { yybegin(MAYBE_SEMICOLON); return RBRACK; }
"("                                       { return LPAREN; }
")"                                       { yybegin(MAYBE_SEMICOLON); return RPAREN; }
";"                                       { return SEMICOLON; }
","                                       { return COMMA; }
"="                                       { return ASSIGN; }

"void"                                    { return VOID; }
"struct"                                  { return STRUCT; }
"bool"                                    { return BOOL; }
"byte"                                    { return BYTE; }
"short"                                   { return SHORT; }
"int"                                     { return INT; }
"double"                                  { return DOUBLE; }
"float"                                   { return FLOAT; }
"long"                                    { return LONG; }
"string"                                  { return STRING; }
"vector"                                  { return VECTOR; }
"map"                                     { return MAP; }
"key"                                     { return KEY; }
"routekey"                                { return ROUTE_KEY; }
"module"                                  { return MODULE; }
"interface"                               { return INTERFACE; }
"out"                                     { return OUT; }
"require"                                 { return REQUIRE; }
"optional"                                { return OPTIONAL; }
"false"                                   { return FALSE; }
"true"                                    { return TRUE; }
"enum"                                    { return ENUM; }
"const"                                   { return CONST; }

"unsigned"                                { return UNSIGNED; }
"::"                                      { return SCOPE_DELIMITER; }

{IDENT}                                   { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }
{NUM_INT}                                 { yybegin(MAYBE_SEMICOLON); return INT; }
{NUM_FLOAT}                               { yybegin(MAYBE_SEMICOLON); return FLOAT; }

//{NUM_FLOAT}"i"                            { yybegin(MAYBE_SEMICOLON); return FLOATI; }
//{DIGIT}+"i"                               { yybegin(MAYBE_SEMICOLON); return DECIMALI; }
//{NUM_OCT}                                 { yybegin(MAYBE_SEMICOLON); return OCT; }
//{NUM_HEX}                                 { yybegin(MAYBE_SEMICOLON); return HEX; }

.                                         { return BAD_CHARACTER; }
}

<MAYBE_SEMICOLON> {
{WS}                                      { return WS; }
{NL}                                      { yybegin(YYINITIAL); yypushback(yytext().length()); return SEMICOLON_SYNTHETIC; }
{LINE_COMMENT}                            { return LINE_COMMENT; }
{MULTILINE_COMMENT}                       { return MULTILINE_COMMENT; }
.                                         { yybegin(YYINITIAL); yypushback(yytext().length()); }
}