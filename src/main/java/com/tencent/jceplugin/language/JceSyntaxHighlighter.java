package com.tencent.jceplugin.language;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.language.psi.JceElementType;
import com.tencent.jceplugin.language.psi.JceTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class JceSyntaxHighlighter extends SyntaxHighlighterBase {
    //等号
    public static final TextAttributesKey OPERATOR =
            createTextAttributesKey("JCE_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    //标识符
    public static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("JCE_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    //中括号、尖括号
    public static final TextAttributesKey BRACES =
            createTextAttributesKey("JCE_BRACES", DefaultLanguageHighlighterColors.BRACES);
    //大括号
    public static final TextAttributesKey BRACKETS =
            createTextAttributesKey("JCE_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    //小括号
    public static final TextAttributesKey PARENTHESES =
            createTextAttributesKey("JCE_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    //逗号
    public static final TextAttributesKey COMMA =
            createTextAttributesKey("JCE_COMMA", DefaultLanguageHighlighterColors.COMMA);
    //分号
    public static final TextAttributesKey SEMICOLON =
            createTextAttributesKey("JCE_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    //常量
    public static final TextAttributesKey CONSTANT =
            createTextAttributesKey("JCE_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT);
    //方法
    public static final TextAttributesKey FUNCTION_DECLARATION =
            createTextAttributesKey("JCE_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    //结构体、枚举名
    public static final TextAttributesKey CLASS_NAME =
            createTextAttributesKey("JCE_CLASS_NAME", DefaultLanguageHighlighterColors.CLASS_NAME);
    //interface名
    public static final TextAttributesKey INTERFACE_NAME =
            createTextAttributesKey("JCE_INTERFACE_NAME", DefaultLanguageHighlighterColors.INTERFACE_NAME);
    //结构体、枚举名引用
    public static final TextAttributesKey CLASS_REFERENCE =
            createTextAttributesKey("JCE_CLASS_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    //结构体字段
    public static final TextAttributesKey FIELD_REFERENCE =
            createTextAttributesKey("JCE_FIELD_REFERENCE", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    public static final TextAttributesKey FIELD_DECLARATION =
            createTextAttributesKey("JCE_FIELD_DECLARATION", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    //方法参数
    public static final TextAttributesKey PARAMETER =
            createTextAttributesKey("JCE_PARAMETER", DefaultLanguageHighlighterColors.PARAMETER);
    //jce关键字：const struct enum key taf_identifier module #include out byte long int short vector map unsigned
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("JCE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    //数字
    public static final TextAttributesKey NUMBER =
            createTextAttributesKey("JCE_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    //字符串字面量
    public static final TextAttributesKey STRING =
            createTextAttributesKey("JCE_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey LINE_COMMENT =
            createTextAttributesKey("JCE_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT =
            createTextAttributesKey("JCE_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("JCE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{OPERATOR};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] LINE_COMMENT_KEYS = new TextAttributesKey[]{LINE_COMMENT};
    private static final TextAttributesKey[] BLOCK_COMMENT_KEYS = new TextAttributesKey[]{BLOCK_COMMENT};
    private static final TextAttributesKey[] CLASS_REFERENCE_KEYS = new TextAttributesKey[]{CLASS_REFERENCE};
    private static final TextAttributesKey[] NUMBER_KEY = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] IDENTIFIER_KEY = new TextAttributesKey[]{IDENTIFIER};
    private static final TextAttributesKey[] BRACES_KEY = new TextAttributesKey[]{BRACES};
    private static final TextAttributesKey[] BRACKETS_KEY = new TextAttributesKey[]{BRACKETS};
    private static final TextAttributesKey[] PARENTHESES_KEY = new TextAttributesKey[]{PARENTHESES};
    private static final TextAttributesKey[] SEMICOLON_KEY = new TextAttributesKey[]{SEMICOLON};
    private static final TextAttributesKey[] COMMA_KEY = new TextAttributesKey[]{COMMA};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new JceLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(JceTypes.EQUAL)
                || tokenType.equals(JceTypes.DOUBLE_COLON)) {
            return OPERATOR_KEYS;
        } else if (tokenType.equals(JceTypes.KEY)
                || tokenType.equals(JceTypes.INCLUDE)
                || tokenType.equals(JceTypes.MODULE)
                || tokenType.equals(JceTypes.CONST)
                || tokenType.equals(JceTypes.STRUCT)
                || tokenType.equals(JceTypes.ENUM)
                || tokenType.equals(JceTypes.STRING)
                || tokenType.equals(JceTypes.INTERFACE)
                || tokenType.equals(JceTypes.OUT)
                || tokenType.equals(JceTypes.BYTE)
                || tokenType.equals(JceTypes.BOOL)
                || tokenType.equals(JceTypes.UNSIGNED)
                || tokenType.equals(JceTypes.SHORT)
                || tokenType.equals(JceTypes.FLOAT)
                || tokenType.equals(JceTypes.DOUBLE)
                || tokenType.equals(JceTypes.INT)
                || tokenType.equals(JceTypes.LONG)
                || tokenType.equals(JceTypes.MAP)
                || tokenType.equals(JceTypes.VECTOR)
                || tokenType.equals(JceTypes.OPTIONAL)
                || tokenType.equals(JceTypes.REQUIRE)
                || tokenType.equals(JceTypes.VOID)
                || tokenType.equals(JceTypes.TRUE)
                || tokenType.equals(JceTypes.FALSE)
                || tokenType.equals(JceTypes.ROUTEKEY)
                || tokenType.equals(JceTypes.TAF_IDENTIFIER)
        ) {
            return KEYWORD_KEYS;
        } else if (tokenType.equals(JceTypes.STRING_LITERAL) || tokenType.equals(JceTypes.WRONG_STRING_LITERAL)) {
            return STRING_KEYS;
        } else if (tokenType.equals(JceTypes.IDENTIFIER)) {
            return IDENTIFIER_KEY;
        } else if (tokenType.equals(JceTypes.NUM_INT)
                || tokenType.equals(JceTypes.NUM_DOUBLE)
                || tokenType.equals(JceTypes.HEX_INT)) {
            return NUMBER_KEY;
        } else if (tokenType.equals(JceTypes.BLOCK_COMMENT) || tokenType.equals(JceTypes.DOC_COMMENT) || tokenType.equals(JceElementType.DOC_COMMENT)) {
            return BLOCK_COMMENT_KEYS;
        } else if (tokenType.equals(JceTypes.LINE_COMMENT)) {
            return LINE_COMMENT_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else if (tokenType.equals(JceTypes.OPEN_BRACE)
                || tokenType.equals(JceTypes.CLOSE_BRACE)) {
            return BRACES_KEY;
        } else if (tokenType.equals(JceTypes.OPEN_BLOCK)
                || tokenType.equals(JceTypes.CLOSE_BLOCK)) {
            return BRACKETS_KEY;
        } else if (tokenType.equals(JceTypes.OPEN_PARENTHESIS)
                || tokenType.equals(JceTypes.CLOSE_PARENTHESIS)) {
            return PARENTHESES_KEY;
        } else if (tokenType.equals(JceTypes.LESS_THAN)
                || tokenType.equals(JceTypes.GREATER_THAN)) {
            return BRACES_KEY;
        } else if (tokenType.equals(JceTypes.SEMICOLON)) {
            return SEMICOLON_KEY;
        } else if (tokenType.equals(JceTypes.COMMA)) {
            return COMMA_KEY;
        } else {
            return EMPTY_KEYS;
        }
    }
}
