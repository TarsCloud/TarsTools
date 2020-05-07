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
import com.tencent.jceplugin.language.psi.JceElementFactory;
import com.tencent.jceplugin.language.psi.JceKeyInfo;
import org.jetbrains.annotations.NotNull;

public class JceKeyInfoManipulator extends AbstractElementManipulator<JceKeyInfo> {
    @Override
    public JceKeyInfo handleContentChange(@NotNull JceKeyInfo keyInfo, @NotNull TextRange rangeInParent, String newContent) {
        String text = keyInfo.getText();
        StringBuilder newKeyInfoCode = new StringBuilder();
        if (rangeInParent.getStartOffset() > 0) {
            newKeyInfoCode.append(text, 0, rangeInParent.getStartOffset());
        }
        newKeyInfoCode.append(newContent);
        newKeyInfoCode.append(text, rangeInParent.getEndOffset(), text.length());
        JceKeyInfo newKeyInfo = JceElementFactory.createKeyInfoByString(keyInfo.getProject(), newKeyInfoCode.toString());
        return (JceKeyInfo) keyInfo.replace(newKeyInfo);
    }
}
