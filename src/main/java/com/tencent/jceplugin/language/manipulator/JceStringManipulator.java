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

package com.tencent.jceplugin.language.manipulator;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.tencent.jceplugin.language.psi.JceIncludeFilename;
import org.jetbrains.annotations.NotNull;

public class JceStringManipulator extends AbstractElementManipulator<JceIncludeFilename> {
    @Override
    public JceIncludeFilename handleContentChange(@NotNull JceIncludeFilename psi, @NotNull TextRange range, String newContent) {
        String oldText = psi.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText.substring(range.getEndOffset());
        return psi.setName(newText);
    }

    @NotNull
    @Override
    public TextRange getRangeInElement(@NotNull JceIncludeFilename element) {
        return getStringTokenRange(element);
    }

    public static TextRange getStringTokenRange(JceIncludeFilename element) {
        return TextRange.from(1, element.getTextLength() - 2);
    }
}
