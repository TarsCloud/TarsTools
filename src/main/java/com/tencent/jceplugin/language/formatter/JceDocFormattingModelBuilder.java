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

package com.tencent.jceplugin.language.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.alignment.AlignmentStrategy;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.tencent.jceplugin.language.JceLanguage;
import com.tencent.jceplugin.language.jcedoc.psi.JceDocTypes;
import org.jetbrains.annotations.NotNull;

/**
 * asdasds
 */
public class JceDocFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        PsiElement element = formattingContext.getPsiElement();
        CodeStyleSettings settings = formattingContext.getCodeStyleSettings();
        Block block = new JceDocBlock(
                element.getNode(),
                null,
                AlignmentStrategy.getNullStrategy().getAlignment(element.getNode().getElementType()),
                Indent.getAbsoluteNoneIndent(),
                settings,
                createSpacingBuilder(settings));

        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
    }

    @NotNull
    private static SpacingBuilder createSpacingBuilder(@NotNull CodeStyleSettings settings) {
        return new SpacingBuilder(settings, JceLanguage.INSTANCE)
                .before(JceDocTypes.DOC_COMMENT_LEADING_ASTRISK).spaces(1)
                .after(JceDocTypes.DOC_COMMENT_LEADING_ASTRISK).spaces(1);
    }
}
