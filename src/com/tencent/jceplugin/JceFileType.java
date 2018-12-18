package com.tencent.jceplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JceFileType extends LanguageFileType {

    public static final JceFileType INSTANCE = new JceFileType();

    private JceFileType() {
        super(JceLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Jce file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Jce language file";
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
