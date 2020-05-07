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

package com.tencent.jceplugin.language.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.tencent.jceplugin.language.JceFileType;
import com.tencent.jceplugin.language.JceLanguage;
import org.jetbrains.annotations.NotNull;

public class JceFile extends PsiFileBase {
    public JceFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, JceLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return JceFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Jce File";
    }

    public JceIncludeInfo[] getIncludeList() {
        return findChildrenByClass(JceIncludeInfo.class);
    }

    public JceModuleInfo[] getModuleList() {
        return findChildrenByClass(JceModuleInfo.class);
    }
}
