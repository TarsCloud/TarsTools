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

package com.tencent.jceplugin.language.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JceBlock extends AbstractBlock {
    private final JceFormatContext myContext;
    private final Indent myIndent;
    private final boolean myIsIncomplete;
    private final Indent myNewChildIndent;

    JceBlock(@NotNull final JceFormatContext context, @NotNull final ASTNode node) {
        super(node, null, context.computeAlignment(node));
        myContext = context;
        myIndent = myContext.computeBlockIndent(myNode);
        myIsIncomplete = myContext.isIncomplete(myNode);
        myNewChildIndent = myContext.computeNewChildIndent(myNode);
    }

    @Nullable
    @Override
    protected Indent getChildIndent() {
        return myNewChildIndent;
    }

    @Override
    protected List<Block> buildChildren() {
        return makeSubBlock(myContext, myNode);
    }

    @NotNull
    public static List<Block> makeSubBlock(@NotNull JceFormatContext context, @NotNull ASTNode node) {
        List<Block> res = new SmartList<>();
        for (ASTNode subNode = node.getFirstChildNode(); subNode != null; subNode = subNode.getTreeNext()) {
            IElementType subNodeType = PsiUtilCore.getElementType(subNode);
            if (TokenType.WHITE_SPACE != subNodeType/* && JceDocTypes.DOC_SPACE != subNodeType*/) {
                // if (node.getElementType() == JceElementType.DOC_COMMENT) {
                //     res.add(new JceBlock(context, subNode));
                // }
                res.add(new JceBlock(context, subNode));
            }
        }
        return res;
    }

    public Indent getIndent() {
        return myIndent;
    }

    public Spacing getSpacing(Block block1, @NotNull Block block2) {
        return myContext.computeSpacing(this, block1, block2);
    }

    public boolean isIncomplete() {
        return myIsIncomplete;
    }

    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
