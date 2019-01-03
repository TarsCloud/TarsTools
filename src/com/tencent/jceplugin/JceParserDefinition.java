package com.tencent.jceplugin;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.tencent.jceplugin.parser.JceParser;
import com.tencent.jceplugin.psi.JceFile;
import com.tencent.jceplugin.psi.JceTokenType;
import com.tencent.jceplugin.psi.JceTypes;
import org.jetbrains.annotations.NotNull;

import static com.tencent.jceplugin.psi.JceTypes.*;

public class JceParserDefinition implements ParserDefinition {
    public static final IElementType LINE_COMMENT = new JceTokenType("JCE_LINE_COMMENT");
    public static final IElementType MULTILINE_COMMENT = new JceTokenType("JCE_MULTILINE_COMMENT");

    public static final IElementType WS = new JceTokenType("JCE_WHITESPACE");
    public static final IElementType NLS = new JceTokenType("JCE_WS_NEW_LINES");

    public static final TokenSet WHITESPACES = TokenSet.create(WS, NLS);
    public static final TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, MULTILINE_COMMENT);
    public static final TokenSet STRING_LITERALS = TokenSet.create(STRING);
    // todo
//    public static final TokenSet NUMBERS = TokenSet.create(INT, FLOAT, FLOATI, DECIMALI, FLOATI); // todo: HEX, OCT,
    public static final TokenSet NUMBERS = TokenSet.create(INT, FLOAT);
    public static final TokenSet KEYWORDS = TokenSet.create(CONST, INTERFACE, MAP, STRUCT);
    public static final TokenSet OPERATORS = TokenSet.create(ASSIGN);

    public static final IFileElementType FILE = new IFileElementType(JceLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new JceLexerAdapter();
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITESPACES;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return STRING_LITERALS;
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        return new JceParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new JceFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return JceTypes.Factory.createElement(node);
    }
}
