package com.tencent.jceplugin.language.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.tencent.jceplugin.language.psi.JceFieldInfo;
import com.tencent.jceplugin.language.psi.JceKeyInfo;
import com.tencent.jceplugin.language.psi.JceRefStructField;
import com.tencent.jceplugin.language.psi.JceStructType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 结构体字段引用
 * key [CommonInParam, field1, field2]
 */
public class JceKeyInfoStructFieldReference extends PsiReferenceBase<JceRefStructField> implements PsiPolyVariantReference {
    private final JceStructType structType;
    private final String fieldName;

    public JceKeyInfoStructFieldReference(JceRefStructField element, TextRange textRange) {
        super(element, textRange);
        //在当前module内找到结构体
        JceKeyInfo moduleInfo = (JceKeyInfo) PsiTreeUtil.findFirstParent(element, e -> e instanceof JceKeyInfo);
        if (moduleInfo != null) {
            if (moduleInfo.getRefStruct() != null && moduleInfo.getRefStruct().getReference() != null) {
                PsiElement jceStructType = moduleInfo.getRefStruct().getReference().resolve();
                if (jceStructType instanceof JceStructType) {
                    structType = (JceStructType) jceStructType;
                } else {
                    structType = null;
                }
            } else {
                structType = null;
            }
        } else {
            structType = null;
        }
        fieldName = element.getIdentifier().getText();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        List<ResolveResult> results = new ArrayList<>();
        if (structType != null) {
            //找到结构体
            for (JceFieldInfo jceFieldInfo : structType.getFieldInfoList()) {
                if (!incompleteCode) {
                    if (fieldName.equals(jceFieldInfo.getName())) {
                        results.add(new PsiElementResolveResult(jceFieldInfo));
                    }
                } else {
                    results.add(new PsiElementResolveResult(jceFieldInfo));
                }
            }
        }
        return results.toArray(new ResolveResult[0]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length >= 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        List<LookupElement> variants = new ArrayList<>();
        if (structType != null) {
            for (JceFieldInfo jceFieldInfo : structType.getFieldInfoList()) {
                variants.add(LookupElementBuilder
                        .create(jceFieldInfo).withIcon(PlatformIcons.FIELD_ICON)
                        .withTypeText(structType.getName()));
            }
        }
        return variants.toArray();
    }
}
