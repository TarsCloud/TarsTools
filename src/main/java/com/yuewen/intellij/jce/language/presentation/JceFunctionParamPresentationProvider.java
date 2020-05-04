package com.yuewen.intellij.jce.language.presentation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.yuewen.intellij.jce.language.psi.JceFunctionInfo;
import com.yuewen.intellij.jce.language.psi.JceFunctionParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JceFunctionParamPresentationProvider implements ItemPresentationProvider<JceFunctionParam> {

    @Override
    public ItemPresentation getPresentation(@NotNull JceFunctionParam element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiElement module = PsiTreeUtil.findFirstParent(element, e -> e instanceof JceFunctionInfo);
                if (module == null) {
                    return null;
                }
                return ((JceFunctionInfo) module).getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.PARAMETER_ICON;
            }
        };
    }
}
