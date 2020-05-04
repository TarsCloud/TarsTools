package com.tencent.jceplugin.language.formatter;

import com.intellij.formatting.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import com.tencent.jceplugin.language.JceFileType;
import com.tencent.jceplugin.language.JceLanguage;
import com.tencent.jceplugin.language.JceParserDefinition;
import com.tencent.jceplugin.language.jcedoc.psi.JceDocTypes;
import com.tencent.jceplugin.language.psi.JceElementType;
import com.tencent.jceplugin.language.psi.JceTypes;
import org.jetbrains.annotations.NotNull;

public class JceFormattingModelBuilder implements CustomFormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        JceFormatContext context = new JceFormatContext(settings, createSpaceBuilder(settings));
        Block block = new JceBlock(context, element.getNode());
        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        CommonCodeStyleSettings commonSettings = settings.getCommonSettings(JceLanguage.INSTANCE);
        return new SpacingBuilder(settings, JceLanguage.INSTANCE)
                .beforeInside(JceTypes.INCLUDE_FILENAME, JceTypes.INCLUDE_INFO)
                .spaces(1)
                .before(JceTypes.ENUM_MEMBER)
                .blankLines(0)
                .before(JceTypes.FIELD_INFO)
                .blankLines(0)
                .before(JceTypes.FIELD_DEFAULT_ASSIGNMENT)
                .spaceIf(commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .before(JceTypes.CONST_ASSIGNMENT)
                .spaceIf(commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(JceTypes.EQUAL)
                .spaceIf(commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(JceTypes.DOUBLE_COLON)
                .spaceIf(commonSettings.SPACE_AROUND_METHOD_REF_DBL_COLON)
                .before(JceTypes.COMMA)
                .spaceIf(commonSettings.SPACE_BEFORE_COMMA)
                .after(JceTypes.COMMA)
                .spaceIf(commonSettings.SPACE_AFTER_COMMA)
                .before(JceTypes.SEMICOLON)
                .spaceIf(commonSettings.SPACE_BEFORE_SEMICOLON)
                .after(JceTypes.SEMICOLON)
                .spaceIf(commonSettings.SPACE_AFTER_SEMICOLON)
                //所有以;结尾的元素
                .after(TokenSet.create(JceTypes.FIELD_INFO, JceTypes.FUNCTION_INFO, JceTypes.MODULE_INFO, JceTypes.KEY_INFO,
                        JceTypes.STRUCT_TYPE, JceTypes.ENUM_TYPE, JceTypes.CONST_TYPE, JceTypes.INTERFACE_INFO))
                .spaceIf(commonSettings.SPACE_AFTER_SEMICOLON)
                .around(JceTypes.OPEN_PARENTHESIS)
                .lineBreakInCodeIf(false)
                .around(JceTypes.CLOSE_PARENTHESIS)
                .lineBreakInCodeIf(false)
                .before(JceTypes.OPEN_PARENTHESIS)
                .spaces(commonSettings.SPACE_BEFORE_METHOD_PARENTHESES ? 1 : 0)
                .before(JceTypes.OPEN_BLOCK)
                .blankLines(0)
                .after(TokenSet.create(JceTypes.DOC_COMMENT, JceTypes.BLOCK_COMMENT, JceElementType.DOC_COMMENT))
                .blankLines(0)
                .after(TokenSet.create(JceTypes.DOC_COMMENT, JceTypes.BLOCK_COMMENT, JceElementType.DOC_COMMENT))
                .blankLines(0)
                .around(JceParserDefinition.INDEPENDENT_LINE_DEFINITIONS)
                .blankLines(0)
                .around(JceParserDefinition.INDEPENDENT_LINE_DEFINITIONS)
                .lineBreakInCode()
                .after(JceTypes.UNSIGNED)
                .spaces(1)
                .beforeInside(JceTypes.FIELD_TYPE, JceTypes.FIELD_INFO)
                .spaces(1)
                .beforeInside(JceTypes.FIELD_LABEL, JceTypes.FIELD_INFO)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.KEY_INFO)
                .none()
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.CONST_TYPE)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.FIELD_INFO)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.FUNCTION_PARAM)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.FUNCTION_INFO)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.MODULE_INFO)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.CONST_TYPE)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.ENUM_TYPE)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.STRUCT_TYPE)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.MODULE_INFO)
                .spaces(1)
                .beforeInside(JceTypes.IDENTIFIER, JceTypes.INTERFACE_INFO)
                .spaces(1)
                .withinPair(JceTypes.OPEN_BRACE, JceTypes.CLOSE_BRACE)
                .spaceIf(commonSettings.SPACE_WITHIN_BRACKETS)
                .after(TokenSet.create(JceTypes.DOC_COMMENT, JceTypes.BLOCK_COMMENT, JceElementType.DOC_COMMENT))
                .lineBreakInCode()
                .before(JceTypes.FIELD_LABEL)
                .spaces(1)
                .before(JceTypes.BUILT_IN_TYPES)
                .spaces(1)
                .withinPair(JceTypes.OPEN_PARENTHESIS, JceTypes.CLOSE_PARENTHESIS)
                .spaceIf(commonSettings.SPACE_WITHIN_METHOD_PARENTHESES, true)
                .withinPair(JceTypes.OPEN_BRACE, JceTypes.CLOSE_BRACE)
                .spaceIf(commonSettings.SPACE_WITHIN_BRACES, true)
                .withinPair(JceTypes.LESS_THAN, JceTypes.GREATER_THAN)
                .spaceIf(commonSettings.SPACE_WITHIN_PARENTHESES, true)
                .beforeInside(JceDocTypes.DOC_COMMENT_LEADING_ASTRISK, JceElementType.DOC_COMMENT)
                .spaces(1)
                ;
    }

    @Override
    public boolean isEngagedToFormat(PsiElement psiElement) {
        PsiFile containingFile = psiElement.getContainingFile();
        if (containingFile == null) {
            return false;
        }
        return containingFile.getFileType() == JceFileType.INSTANCE && containingFile.getLanguage() == JceLanguage.INSTANCE;
    }
}
