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

package com.tencent.jceplugin.language.presentation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import icons.JceIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JceModulePresentationProvider implements ItemPresentationProvider<JceModuleInfo> {

    @Override
    public ItemPresentation getPresentation(@NotNull JceModuleInfo moduleInfo) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getVirtualFile().getPath();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return JceIcons.MODULE;
            }
        };
    }
}
