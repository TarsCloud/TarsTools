package com.tencent.jceplugin.language.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SmartList;
import com.tencent.jceplugin.language.psi.JceFieldDefaultAssignment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 字段默认值，枚举成员
 * 0 optional enumType abc = enumMember
 */
public class JceFieldDefaultAssignmentReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(JceFieldDefaultAssignment.class), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                         @NotNull ProcessingContext context) {
                if (!(element instanceof JceFieldDefaultAssignment)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                List<PsiReference> psiReferences = new SmartList<>();
                JceFieldDefaultAssignment fieldDefaultAssignment = (JceFieldDefaultAssignment) element;
                if (fieldDefaultAssignment.getStringLiterals() != null
                        && fieldDefaultAssignment.getStringLiterals().getIdentifier() != null
                        && !fieldDefaultAssignment.getStringLiterals().getIdentifier().getText().isEmpty()) {
                    psiReferences.add(new JceFieldDefaultAssignmentReference(fieldDefaultAssignment, fieldDefaultAssignment.getStringLiterals().getTextRangeInParent()));
                }
                return psiReferences.toArray(new PsiReference[0]);
            }
        });
    }
}
