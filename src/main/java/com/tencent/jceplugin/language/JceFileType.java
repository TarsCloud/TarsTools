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

import com.intellij.openapi.fileTypes.LanguageFileType;
import icons.JceIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JceFileType extends LanguageFileType {
    public static final JceFileType INSTANCE = new JceFileType();

    protected JceFileType() {
        super(JceLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Jce File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Tars IDL language file.";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "jce";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return JceIcons.FILE;
    }
}
