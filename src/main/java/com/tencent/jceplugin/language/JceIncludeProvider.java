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

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.include.FileIncludeInfo;
import com.intellij.psi.impl.include.FileIncludeProvider;
import com.intellij.util.Consumer;
import com.intellij.util.indexing.FileContent;
import com.tencent.jceplugin.language.psi.JceIncludeInfo;
import com.tencent.jceplugin.language.psi.JceVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class JceIncludeProvider extends FileIncludeProvider {
    @NotNull
    @Override
    public String getId() {
        return "Jce";
    }

    @Override
    public boolean acceptFile(VirtualFile virtualFile) {
        return virtualFile.getFileType() == JceFileType.INSTANCE;
    }

    @Override
    public void registerFileTypesUsedForIndexing(@NotNull Consumer<? super FileType> fileTypeSink) {
        fileTypeSink.consume(JceFileType.INSTANCE);
    }

    @NotNull
    @Override
    public FileIncludeInfo[] getIncludeInfos(FileContent content) {
        final ArrayList<FileIncludeInfo> infos;
        if (content.getFileType() == JceFileType.INSTANCE) {
            infos = new ArrayList<>();
            content.getPsiFile().acceptChildren(new JceVisitor() {
                @Override
                public void visitIncludeInfo(@NotNull JceIncludeInfo include) {
                    final String path = include.getIncludeFileName();
                    infos.add(new FileIncludeInfo(path));
                }

                @Override
                public void visitElement(@NotNull PsiElement element) {
                    element.acceptChildren(this);
                }
            });
        } else {
            return FileIncludeInfo.EMPTY;
        }
        return infos.toArray(FileIncludeInfo.EMPTY);
    }
}
