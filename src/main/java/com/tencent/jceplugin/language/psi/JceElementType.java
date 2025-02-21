/**
 * Tencent is pleased to support the open source community by making Tars available.
 * <p>
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 * <p>
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * https://opensource.org/licenses/BSD-3-Clause
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.intellij.psi.tree.IReparseableElementType;
import com.tencent.jceplugin.language.JceLanguage;
import com.tencent.jceplugin.language.jcedoc.JceDocParserDefinition;
import com.tencent.jceplugin.language.jcedoc.psi.JceDocTypes;
import com.tencent.jceplugin.language.jcedoc.psi.PsiJceDocElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JceElementType extends IElementType {
    public JceElementType(@NotNull @NonNls String debugName) {
        super(debugName, JceLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "JceElementType:" + super.toString();
    }

    public static final ILazyParseableElementType DOC_COMMENT =
            new IReparseableElementType("DOC_COMMENT", JceLanguage.INSTANCE) {
                @Override
                public ASTNode createNode(final CharSequence text) {
                    return new PsiJceDocElement(text);
                }

                public ASTNode parseContents(ASTNode chameleon) {
                    PsiElement parentElement = chameleon.getTreeParent().getPsi();
                    assert parentElement != null : "parent psi is null: " + chameleon;
                    return doParseContents(chameleon, parentElement);
                }

                protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
                    Project project = psi.getProject();
                    JceDocParserDefinition parserDefinition = JceDocParserDefinition.INSTANCE;
                    PsiBuilder builder =
                            PsiBuilderFactory.getInstance().createBuilder(
                                    parserDefinition, parserDefinition.createLexer(project), chameleon.getChars());
                    PsiParser parser = parserDefinition.createParser(project);
                    ASTNode node = parser.parse(JceDocTypes.COMMENT, builder);
                    return node.getFirstChildNode();
                }

                @Override
                public boolean isReparseable(@NotNull ASTNode currentNode,
                                             @NotNull CharSequence newText,
                                             @NotNull Language fileLanguage,
                                             @NotNull Project project) {
                    if (!StringUtil.startsWith(newText, "/**") || !StringUtil.endsWith(newText, "*/")) {
                        return false;
                    }

                    // Prevent the parser from consuming non- doc comment tokens.
                    Lexer lexer = JceDocParserDefinition.INSTANCE.createLexer(project);
                    lexer.start(newText);
                    if (lexer.getTokenType() != DOC_COMMENT) {
                        return false;
                    }
                    lexer.advance();
                    return lexer.getTokenType() == null;
                }
            };
}
