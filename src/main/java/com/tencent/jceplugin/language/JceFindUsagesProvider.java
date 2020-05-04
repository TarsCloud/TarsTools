package com.tencent.jceplugin.language;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JceFindUsagesProvider implements FindUsagesProvider {
    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return null;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        if (psiElement instanceof JceNamedElement) {
            return ((JceNamedElement) psiElement).getName();
        }
        return "help id";
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement psiElement) {
        if (psiElement instanceof JceModuleInfo) {
            return "Module";
        } else if (psiElement instanceof JceStructType) {
            return "Struct";
        } else if (psiElement instanceof JceEnumType) {
            return "Enum";
        } else if (psiElement instanceof JceEnumMember) {
            return "Enum member";
        } else if (psiElement instanceof JceFunctionParam) {
            return "Parameter";
        }
        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement psiElement) {
        // return ElementDescriptionUtil.getElementDescription(psiElement, UsageViewLongNameLocation.INSTANCE);
        String name = "";
        if (psiElement instanceof PsiNamedElement) {
            name = Optional.of((PsiNamedElement) psiElement).map(PsiNamedElement::getName).orElse("");
        }
        if (name.isEmpty()) {
            return "";
        }
        PsiElement parent = psiElement.getParent();
        if (parent instanceof JceModuleInfo) {
            name = ((JceModuleInfo) parent).getName() + "::" + name;
        }
        return name;
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement psiElement, boolean useFullName) {
        String name = "";
        if (psiElement instanceof PsiNamedElement) {
            name = Optional.of((PsiNamedElement) psiElement).map(PsiNamedElement::getName).orElse("");
        }
        if (name.isEmpty()) {
            return "";
        }
        if (useFullName) {
            PsiElement parent = psiElement.getParent();
            if (parent instanceof JceModuleInfo) {
                name = ((JceModuleInfo) parent).getName() + "::" + name;
            }
        }
        return name;
    }
}
