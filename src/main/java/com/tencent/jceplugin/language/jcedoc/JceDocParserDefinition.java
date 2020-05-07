/**
 * Tencent is pleased to support the open source community by making Tars available.
 *
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language.jcedoc;

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
import com.tencent.jceplugin.language.jcedoc.parser.JceDocParser;
import com.tencent.jceplugin.language.jcedoc.psi.JceDocTypes;
import org.jetbrains.annotations.NotNull;

public class JceDocParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, JceDocTypes.DOC_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create();

    public static JceDocParserDefinition INSTANCE = new JceDocParserDefinition();

    /**
     * Returns the lexer for lexing files in the specified project. This lexer does not need to support incremental relexing - it is always
     * called for the entire file.
     *
     * @param project the project to which the lexer is connected.
     * @return the lexer instance.
     */
    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new JceDocLexer();
    }

    /**
     * Returns the parser for parsing files in the specified project.
     *
     * @param project the project to which the parser is connected.
     * @return the parser instance.
     */
    @Override
    public PsiParser createParser(Project project) {
        return new JceDocParser();
    }

    /**
     * Returns the element type of the node describing a file in the specified language.
     *
     * @return the file node element type.
     */
    @Override
    public IFileElementType getFileNodeType() {
        return null;
    }

    /**
     * Returns the set of token types which are treated as whitespace by the PSI builder.
     * Tokens of those types are automatically skipped by PsiBuilder. Whitespace elements
     * on the bounds of nodes built by PsiBuilder are automatically excluded from the text
     * range of the nodes.
     * <p><strong>It is strongly advised you return TokenSet that only contains {@link TokenType#WHITE_SPACE},
     * which is suitable for all the languages unless you really need to use special whitespace token</strong>
     *
     * @return the set of whitespace token types.
     */
    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    /**
     * Returns the set of token types which are treated as comments by the PSI builder.
     * Tokens of those types are automatically skipped by PsiBuilder. Also, To Do patterns
     * are searched in the text of tokens of those types.
     * This token set shouldn't contain types of non-leaf comment inner elements.
     *
     * @return the set of comment token types.
     */
    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    /**
     * Returns the set of element types which are treated as string literals. "Search in strings"
     * option in refactorings is applied to the contents of such tokens.
     *
     * @return the set of string literal element types.
     */
    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    /**
     * Creates a PSI element for the specified AST node. The AST tree is a simple, semantic-free
     * tree of AST nodes which is built during the PsiBuilder parsing pass. The PSI tree is built
     * over the AST tree and includes elements of different types for different language constructs.
     * <p>
     * !!!WARNING!!! PSI element types should be unambiguously determined by AST node element types.
     * You can not produce different PSI elements from AST nodes of the same types (e.g. based on AST node content).
     * Typically, your code should be as simple as that:
     * <pre>{@code
     *   if (node.getElementType == MY_ELEMENT_TYPE) {
     *     return new MyPsiElement(node);
     *   }
     * }</pre>
     *
     * @param node the node for which the PSI element should be returned.
     * @return the PSI element matching the element type of the AST node.
     */
    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return JceDocTypes.Factory.createElement(node);
    }

    /**
     * Creates a PSI element for the specified virtual file.
     *
     * @param viewProvider virtual file.
     * @return the PSI file element.
     */
    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return null;
    }

    /**
     * Checks if the specified two token types need to be separated by a space according to the language grammar.
     * For example, in Java two keywords are always separated by a space; a keyword and an opening parenthesis may
     * be separated or not separated. This is used for automatic whitespace insertion during AST modification operations.
     *
     * @param left  the first token to check.
     * @param right the second token to check.
     * @return the spacing requirements.
     */
    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
