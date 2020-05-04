package com.yuewen.intellij.jce.language.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SmartList;
import com.yuewen.intellij.jce.language.psi.JceRefStruct;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * key [structName, field1, field2, ...]
 */
public class JceKeyInfoStructReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(psiElement(JceRefStruct.class), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                         @NotNull ProcessingContext context) {
                if (!(element instanceof JceRefStruct)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                JceRefStruct refStruct = (JceRefStruct) element;
                //struct
                List<PsiReference> psiReferences = new SmartList<>();
                psiReferences.add(new JceKeyInfoStructReference(refStruct, new TextRange(0, refStruct.getTextRangeInParent().getLength())));
                return psiReferences.toArray(new PsiReference[0]);
            }
        });
    }
}
