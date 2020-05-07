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

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.tencent.jceplugin.language.JceParserDefinition;
import com.tencent.jceplugin.language.codeStyle.JceCodeStyleSettings;
import com.tencent.jceplugin.language.jcedoc.psi.JceDocTypes;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 自动格式化上下文
 *
 * @author kongyuanyuan
 * @since 2020/03/01
 */
public class JceFormatContext {
    private final static Indent DIRECT_NORMAL_INDENT = Indent.getNormalIndent(true);
    private final static Indent SAME_AS_PARENT_INDENT = Indent.getSpaceIndent(0, true);
    private final static Indent SAME_AS_INDENTED_ANCESTOR_INDENT = Indent.getSpaceIndent(0);
    public final Alignment COMMENT_ALIGN = Alignment.createAlignment(true);
    public final Alignment EQUAL_ALIGN = Alignment.createAlignment(true);
    public final Alignment LABEL_ALIGN = Alignment.createAlignment(true);
    public final Alignment TYPE_ALIGN = Alignment.createAlignment(true);
    public final Alignment NAME_ALIGN = Alignment.createAlignment(true);
    public final Alignment VALUE_ALIGN = Alignment.createAlignment(true);

    private final CodeStyleSettings mySettings;
    private final SpacingBuilder mySpacingBuilder;
    private final Map<ASTNode, Map<IElementType, Alignment>> alignmentMap = new HashMap<>();
    private final JceCodeStyleSettings myCustomSettings;

    public JceFormatContext(@NotNull CodeStyleSettings settings, @NotNull SpacingBuilder spacingBuilder) {
        mySettings = settings;
        myCustomSettings = settings.getCustomSettings(JceCodeStyleSettings.class);
        mySpacingBuilder = spacingBuilder;
    }

    public Alignment getAlignment(ASTNode node, IElementType elementType) {
        return Optional.ofNullable(alignmentMap.get(node)).map(m -> m.get(elementType)).orElse(null);
    }

    public Alignment putAlignment(ASTNode node, IElementType elementType, Alignment alignment) {
        return alignmentMap.computeIfAbsent(node, k -> new HashMap<>()).put(elementType, alignment);
    }

    @Nullable
    public Alignment computeAlignment(ASTNode node) {
        PsiElement firstParent = PsiTreeUtil.findFirstParent(node.getPsi(), e -> e instanceof JceStructType || e instanceof JceEnumType);
        if (firstParent == null) {
            return null;
        }
        if (myCustomSettings.ENUM_ALIGNMENT == JceCodeStyleSettings.DO_NOT_ALIGN && firstParent instanceof JceEnumType) {
            return null;
        }
        if (myCustomSettings.FIELD_ALIGNMENT == JceCodeStyleSettings.DO_NOT_ALIGN && firstParent instanceof JceStructType) {
            return null;
        }
        IElementType elementType = node.getElementType();
        TokenSet alignmentToken = TokenSet.create(JceTypes.FIELD_TAG, JceTypes.FIELD_LABEL, JceTypes.IDENTIFIER,
                JceTypes.FIELD_TYPE, JceTypes.EQUAL, JceTypes.STRING_LITERALS, JceTypes.STRING_LITERAL, JceTypes.NUM_INT,
                JceTypes.NUM_DOUBLE, JceTypes.HEX_INT, JceTypes.TRUE, JceTypes.FALSE, JceTypes.LINE_COMMENT,
                JceTypes.FIELD_DEFAULT_ASSIGNMENT, JceTypes.CONST_ASSIGNMENT);
        if (!alignmentToken.contains(elementType)) {
            return null;
        }
        IElementType parentElementType = node.getTreeParent().getElementType();
        if (parentElementType == JceTypes.STRING_LITERALS) {
            // = ""/ = 1/ = 1.1 这种等号后面的值当作一个整体来对齐
            return null;
        }
        if (parentElementType == JceTypes.FIELD_TYPE || parentElementType == JceTypes.REF_MODULE || parentElementType == JceTypes.FIELD_LABEL) {
            //字段类型/module引用里面的标识符不要对齐
            return null;
        }
        if (elementType == JceTypes.IDENTIFIER && (parentElementType == JceTypes.ENUM_TYPE || parentElementType == JceTypes.STRUCT_TYPE)) {
            // struct identifier/enum identifier 不能对齐
            return null;
        }
        Alignment alignment = getAlignment(firstParent.getNode(), elementType);
        if (alignment == null) {
            alignment = Alignment.createAlignment(true);
            putAlignment(firstParent.getNode(), elementType, alignment);
        }
        return alignment;
        // if (parentElementType == JceTypes.FIELD_INFO) {
        //     Alignment alignment = getAlignment(firstParent.getNode(), elementType);
        //     if (alignment == null) {
        //         alignment = Alignment.createAlignment(true);
        //         putAlignment(firstParent.getNode(), elementType, alignment);
        //     }
        //     return alignment;
        // } else if (parentElementType == JceTypes.ENUM_MEMBER) {
        //     Alignment alignment = getAlignment(firstParent.getNode(), elementType);
        //     if (alignment == null) {
        //         alignment = Alignment.createAlignment(true);
        //         putAlignment(firstParent.getNode(), elementType, alignment);
        //     }
        //     return alignment;
        // }
        // if (parentElementType == JceTypes.FIELD_DEFAULT_ASSIGNMENT
        //         && (elementType == JceTypes.EQUAL || elementType == JceTypes.STRING_LITERALS)) {
        //     Alignment alignment = getAlignment(firstParent.getNode(), elementType);
        //     if (alignment == null) {
        //         alignment = Alignment.createAlignment(true);
        //         putAlignment(firstParent.getNode(), elementType, alignment);
        //     }
        //     return alignment;
        // }
        // if (elementType == JceTypes.FIELD_DEFAULT_ASSIGNMENT) {
        //     return null;
        // }
        // if (node.getTreeParent() == null
        //         || node.getElementType() == JceTypes.SEMICOLON
        //         || node.getElementType() == JceTypes.COMMA
        //         || node.getElementType() == JceTypes.FIELD_INFO
        //         || node.getElementType() == JceTypes.DOUBLE_COLON) {
        //     return null;
        // }
        //struct identifier 不能对齐
        // if (elementType == JceTypes.IDENTIFIER) {
        //     if (parentElementType == JceTypes.STRUCT_TYPE
        //             || parentElementType == JceTypes.ENUM_TYPE
        //             || parentElementType == JceTypes.FIELD_TYPE) {
        //         return null;
        //     }
        // }
        // if (node.getElementType() == JceTypes.IDENTIFIER
        //         && node.getTreeParent().getElementType() == JceTypes.FIELD_TYPE) {
        //
        //     return null;
        // }
        // if (JceParserDefinition.ALIGNMENT_ELEMENT.contains(node.getElementType())
        //         || (node.getTreeParent().getElementType() == JceTypes.ENUM_MEMBER
        //         && (node.getElementType() == JceTypes.EQUAL
        //         || node.getElementType() == JceTypes.IDENTIFIER
        //         || node.getElementType() == JceTypes.NUM_INT))
        //         || (node.getTreeParent().getElementType() == JceTypes.FIELD_DEFAULT_ASSIGNMENT
        //         && (node.getElementType() == JceTypes.EQUAL
        //         || node.getElementType() == JceTypes.STRING_LITERALS))) {
        //     Alignment alignment = getAlignment(firstParent.getNode(), node.getElementType());
        //     if (alignment == null) {
        //         alignment = Alignment.createAlignment(true);
        //         putAlignment(firstParent.getNode(), node.getElementType(), alignment);
        //     }
        //     return alignment;
        // }
        // return null;
    }

    public boolean isIncomplete(ASTNode node) {
        ASTNode lastChild = node.getLastChildNode();
        while (lastChild != null && isSkippedElement(lastChild)) {
            lastChild = lastChild.getTreePrev();
        }
        return lastChild != null && (lastChild.getPsi() instanceof PsiErrorElement || isIncomplete(lastChild));
    }

    private static boolean isSkippedElement(ASTNode lastChild) {
        return JceTypes.LINE_COMMENT.equals(lastChild.getElementType())
                || JceTypes.BLOCK_COMMENT.equals(lastChild.getElementType())
                || JceElementType.DOC_COMMENT.equals(lastChild.getElementType())
                || TokenType.WHITE_SPACE.equals(lastChild.getElementType())
                || JceDocTypes.DOC_SPACE.equals(lastChild.getElementType());
    }

    public Spacing computeSpacing(@NotNull Block parent, @Nullable Block child1, @NotNull Block child2) {
        return mySpacingBuilder.getSpacing(parent, child1, child2);
    }

    public Indent computeBlockIndent(ASTNode node) {
        IElementType nodeType = PsiUtilCore.getElementType(node);
        IElementType parentType = PsiUtilCore.getElementType(node.getTreeParent());
        // if (JceParserDefinition.ALL_BRACES.contains(nodeType)
        //         || JceParserDefinition.TERMINATORS.contains(nodeType)) {
        //     return SAME_AS_INDENTED_ANCESTOR_INDENT;
        // } else if (JceParserDefinition.CONTAINER_MEMBERS.contains(nodeType)) {
        //     return Indent.getNormalIndent();
        // }
        if (node.getElementType() != JceElementType.DOC_COMMENT
                && PsiTreeUtil.findFirstParent(node.getPsi(), e -> e != null && e.getNode() != null && e.getNode().getElementType() == JceElementType.DOC_COMMENT) != null) {
            if (node.getElementType() == JceDocTypes.DOC_COMMENT_START) {
                return Indent.getNoneIndent();
            } else {
                return Indent.getSpaceIndent(1);
            }
        }
        if (node.getTreeParent() == null) {
            return Indent.getAbsoluteNoneIndent();
        }
        if (node.getTreeParent().getPsi() instanceof JceFile) {
            return Indent.getAbsoluteNoneIndent();
        }
        if (JceParserDefinition.ALL_BRACES.contains(nodeType)
                || JceParserDefinition.TERMINATORS.contains(nodeType)) {
            // 括号，分号不缩进
            // const a
            // {
            //     aaa = 1,
            // };
            return SAME_AS_PARENT_INDENT;
        }
        if (JceParserDefinition.CONTAINERS.contains(parentType)
                && JceParserDefinition.CONTAINER_MEMBERS.contains(nodeType)) {
            // 成员对象缩进一次
            return Indent.getNormalIndent();
        }
        return SAME_AS_PARENT_INDENT;
    }

    public Indent computeNewChildIndent(ASTNode node) {
        if (node.getPsi() instanceof JceFile) {
            return Indent.getAbsoluteNoneIndent();
        }
        if (node.getElementType() == JceElementType.DOC_COMMENT) {
            return Indent.getSpaceIndent(1);
        }
        return JceParserDefinition.CONTAINERS.contains(node.getElementType()) ? Indent.getNormalIndent() : SAME_AS_PARENT_INDENT;
    }

    public CodeStyleSettings getSettings() {
        return mySettings;
    }

    public SpacingBuilder getSpaceBuilder() {
        return mySpacingBuilder;
    }
}
