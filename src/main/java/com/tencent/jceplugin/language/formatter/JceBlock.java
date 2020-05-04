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
