package com.tencent.jceplugin.language.presentation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.tencent.jceplugin.language.psi.JceEnumMember;
import com.tencent.jceplugin.language.psi.JceEnumType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JceEnumMemberPresentationProvider implements ItemPresentationProvider<JceEnumMember> {

    @Override
    public ItemPresentation getPresentation(@NotNull JceEnumMember element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiElement module = PsiTreeUtil.findFirstParent(element, e -> e instanceof JceEnumType);
                if (module == null) {
                    return null;
                }
                return ((JceEnumType) module).getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.ENUM_ICON;
            }
        };
    }
}
