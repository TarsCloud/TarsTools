package com.tencent.jceplugin.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.tencent.jceplugin.JceFileType;
import com.tencent.jceplugin.JceLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
        return "Simple File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
