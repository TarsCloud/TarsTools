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

package com.tencent.jceplugin.language.smartEnter;

import com.intellij.lang.SmartEnterProcessorWithFixers;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;

public class JceSmartEnterProcessor extends SmartEnterProcessorWithFixers {
    // private boolean myShouldAddNewline = false;

    public JceSmartEnterProcessor() {
        addFixers(new JceSemicolonFixer());
        addEnterProcessors(new JceEnterProcessor());
    }
    //
    // @Override
    // protected void collectAdditionalElements(@NotNull PsiElement element, @NotNull List<PsiElement> result) {
    //     // include all parents as well
    //     PsiElement parent = element.getParent();
    //     while (parent != null && !(parent instanceof JceFile)) {
    //         result.add(parent);
    //         parent = parent.getParent();
    //     }
    // }

    private static class JceSemicolonFixer extends Fixer<JceSmartEnterProcessor> {
        @Override
        public void apply(@NotNull Editor editor, @NotNull JceSmartEnterProcessor smartEnterProcessorWithFixers, @NotNull PsiElement element) throws IncorrectOperationException {
            if (element.getNextSibling() == null && element.getParent() instanceof JceConstAssignment && (element.getNode().getElementType() == JceTypes.NUM_INT || element.getNode().getElementType() == JceTypes.HEX_INT)) {
                //常量赋值
                editor.getDocument().insertString(element.getParent().getTextRange().getEndOffset(), ";");
            } else if (element.getParent() instanceof JceConstType && element.getNode().getElementType() == JceTypes.IDENTIFIER && element.getNextSibling() instanceof PsiErrorElement) {
                //缺少常量赋值语句
                editor.getDocument().insertString(element.getTextRange().getEndOffset(), "=0;");
            } else if (element.getParent() instanceof JceFieldDefaultAssignment && !element.getParent().getParent().isValid()) {
                //字段默认值，缺少分号
                editor.getDocument().insertString(element.getParent().getTextRange().getEndOffset(), ";");
            } else if (element.getParent() instanceof JceFieldInfo && element.getNode().getElementType() == JceTypes.IDENTIFIER) {
                //字段，缺少分号
                if (element.getParent().getNode().findChildByType(JceTypes.SEMICOLON) == null) {
                    editor.getDocument().insertString(element.getTextRange().getEndOffset(), ";");
                }
            } else if (element.getParent() != null && element.getParent().getNode().getElementType() == JceTypes.STRING_LITERALS && element.getParent().getParent().getNode().getElementType() == JceTypes.FIELD_DEFAULT_ASSIGNMENT) {
                //字段，缺少分号
                if (element.getParent().getParent().getParent().getNode().findChildByType(JceTypes.SEMICOLON) == null) {
                    editor.getDocument().insertString(element.getParent().getParent().getTextRange().getEndOffset(), ";");
                }
            } else if (element instanceof JceFieldDefaultAssignment) {
                //字段默认值，缺少分号
                if (element.getParent().getNode().findChildByType(JceTypes.SEMICOLON) == null) {
                    editor.getDocument().insertString(element.getTextRange().getEndOffset(), ";");
                }
            } else if (element.getParent() instanceof JceEnumMember) {
                //枚举成员，缺少逗号
                if (element.getParent().getNextSibling() == null || element.getParent().getNextSibling().getNode().getElementType() != JceTypes.COMMA) {
                    editor.getDocument().insertString(element.getParent().getTextRange().getEndOffset(), ",");
                }
            }
            PsiElement parentElement = PsiTreeUtil.findFirstParent(element, e -> e instanceof JceModuleInfo
                    || e instanceof JceFieldInfo
                    || e instanceof JceStructType
                    || e instanceof JceEnumType);
            if (parentElement != null && element.getNode().getElementType() == JceTypes.CLOSE_BLOCK) {
                editor.getDocument().insertString(element.getParent().getTextRange().getEndOffset(), ";");
            } else if (parentElement != null && element.getNode().getElementType() == TokenType.WHITE_SPACE) {
                if (parentElement.getLastChild().getNode().getElementType() == JceTypes.LINE_COMMENT
                        || parentElement.getLastChild().getNode().getElementType() == JceTypes.BLOCK_COMMENT
                        || parentElement.getLastChild().getNode().getElementType() == JceTypes.DOC_COMMENT
                        || parentElement.getLastChild().getNode().getElementType() == JceElementType.DOC_COMMENT) {
                    editor.getDocument().insertString(parentElement.getLastChild().getTextRange().getEndOffset(), "\n");
                }
                if (parentElement.getNode().findChildByType(JceTypes.CLOSE_BLOCK) == null) {
                    editor.getDocument().insertString(parentElement.getLastChild().getTextRange().getEndOffset(), "};");
                }
            } else if ((element.getParent() instanceof JceFunctionInfo) && element.getNode().getElementType() == JceTypes.CLOSE_PARENTHESIS) {
                editor.getDocument().insertString(element.getParent().getTextRange().getEndOffset(), ";");
            } else if ((element.getParent() instanceof JceFunctionParam) && element.getNode().getElementType() == JceTypes.IDENTIFIER) {
                editor.getDocument().insertString(element.getParent().getTextRange().getEndOffset(), ");");
            } else if (element.getNode().getElementType() == JceTypes.WRONG_STRING_LITERAL) {
                editor.getDocument().insertString(element.getTextRange().getEndOffset(), "\"");
            } else if (element.getParent() instanceof JceKeyInfo) {
                if (element.getNode().getTreePrev().getElementType() == JceTypes.IDENTIFIER) {
                    editor.getDocument().insertString(element.getTextRange().getEndOffset(), "];");
                } else if (element.getNode().getTreePrev().getElementType() == JceTypes.CLOSE_BRACE) {
                    editor.getDocument().insertString(element.getTextRange().getEndOffset(), ";");
                }
            }
            commitDocument(editor);
        }
    }

    private static class JceEnterProcessor extends SmartEnterProcessorWithFixers.FixEnterProcessor {
        @Override
        public boolean doEnter(PsiElement atCaret, PsiFile file, @NotNull Editor editor, boolean modified) {
            plainEnter(editor);
            return true;
        }
    }
}