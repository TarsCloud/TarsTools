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
