package com.yuewen.intellij.jce.language;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.yuewen.intellij.jce.language.parser.JceParser;
import com.yuewen.intellij.jce.language.psi.JceElementType;
import com.yuewen.intellij.jce.language.psi.JceFile;
import com.yuewen.intellij.jce.language.psi.JceTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JceParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(JceTypes.LINE_COMMENT, JceTypes.BLOCK_COMMENT, JceTypes.DOC_COMMENT, JceElementType.DOC_COMMENT);
    public static final TokenSet OPEN_BRACES = TokenSet.create(JceTypes.LESS_THAN, JceTypes.OPEN_BLOCK, JceTypes.OPEN_BRACE, JceTypes.OPEN_PARENTHESIS);
    public static final TokenSet CLOSE_BRACES = TokenSet.create(JceTypes.GREATER_THAN, JceTypes.CLOSE_BLOCK, JceTypes.CLOSE_BRACE, JceTypes.CLOSE_PARENTHESIS);
    public static final TokenSet ALL_BRACES = TokenSet.orSet(OPEN_BRACES, CLOSE_BRACES);
    public static final TokenSet TERMINATORS = TokenSet.create(JceTypes.COMMA, JceTypes.SEMICOLON);
    public static final TokenSet TOP_LEVELS = TokenSet.create(JceTypes.MODULE_INFO, JceTypes.INCLUDE_INFO);
    public static final TokenSet CONTAINERS = TokenSet.create(JceTypes.MODULE_INFO, JceTypes.STRUCT_TYPE, JceTypes.KEY_INFO,
            JceTypes.FUNCTION_INFO, JceTypes.ENUM_TYPE, JceTypes.INTERFACE_INFO, JceTypes.FUNCTION_PARAM_LIST, JceTypes.CONST_TYPE,
            JceTypes.FIELD_INFO);
    public static final TokenSet CONTAINER_MEMBERS = TokenSet.create(JceTypes.STRUCT_TYPE, JceTypes.FIELD_INFO, JceTypes.KEY_INFO,
            JceTypes.FUNCTION_INFO, JceTypes.ENUM_TYPE, JceTypes.ENUM_MEMBER, JceTypes.INTERFACE_INFO, JceTypes.FUNCTION_PARAM_LIST,
            JceTypes.FUNCTION_PARAM, JceTypes.LINE_COMMENT, JceTypes.DOC_COMMENT, JceTypes.BLOCK_COMMENT, JceElementType.DOC_COMMENT,
            JceTypes.CONST_TYPE, JceTypes.CONST_ASSIGNMENT, JceTypes.FIELD_DEFAULT_ASSIGNMENT);
    public static final TokenSet INDEPENDENT_LINE_DEFINITIONS = TokenSet.create(JceTypes.INCLUDE_INFO, JceTypes.MODULE_INFO,
            JceTypes.INTERFACE_INFO, JceTypes.FUNCTION_INFO, JceTypes.STRUCT_TYPE, JceTypes.ENUM_TYPE, JceTypes.CONST_TYPE,
            JceTypes.KEY_INFO);
    public static final TokenSet ALIGNMENT_ELEMENT = TokenSet.create(JceTypes.LINE_COMMENT, JceTypes.DOC_COMMENT,
            JceTypes.BLOCK_COMMENT, JceElementType.DOC_COMMENT, JceTypes.ENUM_MEMBER, JceTypes.FIELD_TAG,
            JceTypes.FIELD_LABEL, JceTypes.FIELD_TYPE, JceTypes.IDENTIFIER);

    public static final Set<String> reservedKeyword = new HashSet<>(Arrays.asList("byte", "int", "integer", "string", "short", "long", "float",
            "double", "true", "false", "boolean", "bool", "unsigned", "out", "function", "interface", "module", "const", "require", "optional",
            "struct", "enum", "map", "vector", "list"));

    public static final Set<String> reservedBuiltinType = new HashSet<>(Arrays.asList("byte", "int", "string", "short", "long", "float",
            "double", "bool", "unsigned int", "unsigned short", "unsigned byte", "unsigned long"));

    public static final IFileElementType FILE = new IFileElementType(JceLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new JceLexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new JceParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.create(JceTypes.STRING_LITERAL, JceTypes.WRONG_STRING_LITERAL);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode astNode) {
        return JceTypes.Factory.createElement(astNode);
    }

    @Override
    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new JceFile(fileViewProvider);
    }

    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
