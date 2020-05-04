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
    public void registerFileTypesUsedForIndexing(@NotNull Consumer<FileType> consumer) {
        consumer.consume(JceFileType.INSTANCE);
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
                    if (path != null) {
                        infos.add(new FileIncludeInfo(path));
                    }
                }

                @Override
                public void visitElement(PsiElement element) {
                    element.acceptChildren(this);
                }
            });
        } else {
            return FileIncludeInfo.EMPTY;
        }
        return infos.toArray(FileIncludeInfo.EMPTY);
    }
}
