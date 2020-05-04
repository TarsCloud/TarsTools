package com.yuewen.intellij.jce.language.presentation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.yuewen.intellij.jce.language.psi.JceFunctionInfo;
import com.yuewen.intellij.jce.language.psi.JceInterfaceInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JceFunctionPresentationProvider implements ItemPresentationProvider<JceFunctionInfo> {

    @Override
    public ItemPresentation getPresentation(@NotNull JceFunctionInfo element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiElement module = PsiTreeUtil.findFirstParent(element, e -> e instanceof JceInterfaceInfo);
                if (module == null) {
                    return null;
                }
                return ((JceInterfaceInfo) module).getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.FUNCTION_ICON;
            }
        };
    }
}
