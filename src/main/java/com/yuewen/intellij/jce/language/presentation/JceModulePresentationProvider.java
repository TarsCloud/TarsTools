package com.yuewen.intellij.jce.language.presentation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.yuewen.intellij.jce.language.psi.JceModuleInfo;
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
