package com.yuewen.intellij.jce.language.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import icons.JceIcons;
import com.yuewen.intellij.jce.language.JceUtil;
import com.yuewen.intellij.jce.language.psi.JceModuleInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JceModuleReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String key;

    public JceModuleReference(@NotNull PsiElement element) {
        super(element, TextRange.from(0, element.getTextRange().getLength()));
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        PsiFile file = myElement.getContainingFile();
        final List<JceModuleInfo> modules = JceUtil.findModules(file, key, true, false);
        List<ResolveResult> results = new ArrayList<>();
        for (JceModuleInfo module : modules) {
            if (incompleteCode || myElement.getText().equals(module.getName())) {
                results.add(new PsiElementResolveResult(module));
            }
        }
        return results.toArray(new ResolveResult[0]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        PsiFile file = myElement.getContainingFile();
        List<JceModuleInfo> properties = JceUtil.findModules(file);
        List<LookupElement> variants = new ArrayList<>();
        for (final JceModuleInfo property : properties) {
            if (property.getName() != null && property.getName().length() > 0) {
                variants.add(LookupElementBuilder
                        .create(property).withIcon(JceIcons.MODULE)
                        .withTypeText(property.getContainingFile().getName())
                );
            }
        }
        return variants.toArray();
    }
}
