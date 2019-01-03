package com.tencent.jceplugin.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.JceLexerAdapter;
import com.tencent.jceplugin.JceParserDefinition;
import com.tencent.jceplugin.psi.JceTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.BLOCK_COMMENT;
import static com.tencent.jceplugin.highlighting.JceSyntaxHighlightingColors.*;

public class JceSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

    static {
        fillMap(ATTRIBUTES, BLOCK_COMMENT, JceParserDefinition.MULTILINE_COMMENT);
        fillMap(ATTRIBUTES, PARENTHESES, JceTypes.LPAREN, JceTypes.RPAREN);
        fillMap(ATTRIBUTES, BRACES, JceTypes.LBRACE, JceTypes.RBRACE);
        fillMap(ATTRIBUTES, BRACKETS, JceTypes.LBRACK, JceTypes.RBRACK);
        fillMap(ATTRIBUTES, BAD_CHARACTER, TokenType.BAD_CHARACTER);
        fillMap(ATTRIBUTES, IDENTIFIER, JceTypes.IDENTIFIER);
//        fillMap(ATTRIBUTES, DOT, JceTypes.DOT, JceTypes.TRIPLE_DOT);
//        fillMap(ATTRIBUTES, COLON, JceTypes.COLON);
        fillMap(ATTRIBUTES, SEMICOLON, JceTypes.SEMICOLON);
        fillMap(ATTRIBUTES, COMMA, JceTypes.COMMA);
        fillMap(ATTRIBUTES, JceParserDefinition.OPERATORS, OPERATOR);
        fillMap(ATTRIBUTES, JceParserDefinition.KEYWORDS, KEYWORD);
        fillMap(ATTRIBUTES, JceParserDefinition.NUMBERS, NUMBER);
        fillMap(ATTRIBUTES, JceParserDefinition.STRING_LITERALS, STRING);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new JceLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(ATTRIBUTES.get(tokenType));
    }
}
