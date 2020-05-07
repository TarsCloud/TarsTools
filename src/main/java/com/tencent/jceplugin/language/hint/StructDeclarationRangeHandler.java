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

package com.tencent.jceplugin.language.hint;

import com.intellij.codeInsight.hint.DeclarationRangeHandler;
import com.intellij.openapi.util.TextRange;
import com.tencent.jceplugin.language.psi.JceStructType;
import org.jetbrains.annotations.NotNull;

public class StructDeclarationRangeHandler implements DeclarationRangeHandler<JceStructType> {
    @Override
    @NotNull
    public TextRange getDeclarationRange(@NotNull final JceStructType container) {
        final TextRange textRange = container.getTextRange();
        int startOffset = textRange != null ? textRange.getStartOffset() : container.getTextOffset();
        int endOffset = startOffset + "struct".length();
        if (container.getNameIdentifier() != null) {
            endOffset = container.getNameIdentifier().getTextRange().getEndOffset();
        }
        return new TextRange(startOffset, endOffset);
    }
}
