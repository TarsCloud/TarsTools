package com.yuewen.intellij.jce.language.presentation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.yuewen.intellij.jce.language.psi.JceEnumType;
import com.yuewen.intellij.jce.language.psi.JceModuleInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JceEnumPresentationProvider implements ItemPresentationProvider<JceEnumType> {

    @Override
    public ItemPresentation getPresentation(@NotNull JceEnumType element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiElement module = PsiTreeUtil.findFirstParent(element, e -> e instanceof JceModuleInfo);
                if (module == null) {
                    return null;
                }
                return ((JceModuleInfo) module).getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.ENUM_ICON;
            }
        };
    }
}
