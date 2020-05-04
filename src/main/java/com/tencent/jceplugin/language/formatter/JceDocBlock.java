package com.tencent.jceplugin.language.formatter;

import com.intellij.formatting.*;
import com.intellij.formatting.alignment.AlignmentStrategy;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.language.jcedoc.psi.JceDocTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on DocCommentBlock.
 */
public class JceDocBlock extends AbstractBlock {
    @NotNull
    protected final Indent indent;
    @NotNull
    protected final CodeStyleSettings settings;
    @NotNull
    protected final SpacingBuilder spacingBuilder;

    public JceDocBlock(ASTNode node,
                       Wrap wrap,
                       Alignment alignment,
                       Indent indent,
                       CodeStyleSettings settings,
                       SpacingBuilder spacingBuilder) {
        super(node, wrap, alignment);
        this.indent = indent;
        this.settings = settings;
        this.spacingBuilder = spacingBuilder;
    }

    @Override
    protected List<Block> buildChildren() {
        final List<Block> result = new ArrayList<>();

        ASTNode child = getNode().getFirstChildNode();
        while (child != null) {
            IElementType childType = child.getElementType();
            if (childType != TokenType.WHITE_SPACE && childType != JceDocTypes.DOC_SPACE
                    && !FormatterUtil.containsWhiteSpacesOnly(child) && !child.getText().trim().isEmpty()) {
                Indent childIndent;
                if (child.getElementType() == JceDocTypes.DOC_COMMENT_START) {
                    childIndent = Indent.getNoneIndent();
                } else {
                    childIndent = Indent.getSpaceIndent(1);
                }

                result.add(new JceDocBlock(
                        child,
                        null,
                        AlignmentStrategy.getNullStrategy().getAlignment(child.getElementType()),
                        childIndent,
                        settings,
                        spacingBuilder));
            }

            child = child.getTreeNext();
        }
        return result;
    }

    @Nullable
    protected Indent getChildIndent() {
        return Indent.getSpaceIndent(1);
    }

    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @NotNull
    @Override
    public Indent getIndent() {
        return indent;
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
