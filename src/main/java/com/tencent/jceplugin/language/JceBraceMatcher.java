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

package com.tencent.jceplugin.language;

import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.language.psi.JceElementType;
import com.tencent.jceplugin.language.psi.JceTypes;
import org.jetbrains.annotations.NotNull;

public class JceBraceMatcher extends PairedBraceMatcherAdapter {

    public JceBraceMatcher() {
        super(new MyPairedBraceMatcher(), JceLanguage.INSTANCE);
    }

    private static class MyPairedBraceMatcher implements PairedBraceMatcher {

        @NotNull
        @Override
        public BracePair[] getPairs() {
            return new BracePair[]{
                    new BracePair(JceTypes.LESS_THAN, JceTypes.GREATER_THAN, false),
                    new BracePair(JceTypes.OPEN_BRACE, JceTypes.CLOSE_BRACE, false),
                    new BracePair(JceTypes.OPEN_BLOCK, JceTypes.CLOSE_BLOCK, false),
                    new BracePair(JceTypes.OPEN_PARENTHESIS, JceTypes.CLOSE_PARENTHESIS, true)
            };
        }

        @Override
        public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, IElementType type) {
            return type == null ||
                    type == TokenType.WHITE_SPACE ||
                    type == JceTypes.CLOSE_BLOCK ||
                    type == JceTypes.CLOSE_BRACE ||
                    type == JceTypes.GREATER_THAN ||
                    type == JceTypes.CLOSE_PARENTHESIS ||
                    type == JceTypes.LINE_COMMENT ||
                    type == JceTypes.COMMA ||
                    type == JceTypes.SEMICOLON ||
                    type == JceTypes.BLOCK_COMMENT ||
                    type == JceElementType.DOC_COMMENT;
        }

        @Override
        public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
            return openingBraceOffset;
        }
    }
}
