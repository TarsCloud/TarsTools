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
        return "Jce Language File";
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
