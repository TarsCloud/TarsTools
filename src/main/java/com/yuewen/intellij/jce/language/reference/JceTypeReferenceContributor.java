package com.yuewen.intellij.jce.language.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.yuewen.intellij.jce.language.psi.JceFieldType;
import com.yuewen.intellij.jce.language.psi.JceRefModule;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class JceTypeReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        /**
         * ModuleName::CommonInParam
         * 引用模块名
         */
        registrar.registerReferenceProvider(psiElement(JceRefModule.class), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                         @NotNull ProcessingContext context) {
                //提示常规的类型（基础类型、结构体、枚举）
                if (!(element instanceof JceRefModule)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                String text = ((JceRefModule) element).getIdentifier().getText();
                if (text != null) {
                    return new PsiReference[]{new JceModuleReference(element)};
                }
                return PsiReference.EMPTY_ARRAY;
            }
        });
        registrar.registerReferenceProvider(psiElement(JceFieldType.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                                 @NotNull ProcessingContext context) {
                        //提示常规的类型（基础类型、结构体、枚举）
                        if (!(element instanceof JceFieldType)) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        if (((JceFieldType) element).getBuiltInTypes() != null) {
                            //内建类型，不匹配
                            return PsiReference.EMPTY_ARRAY;
                        }
                        JceFieldType fieldType = (JceFieldType) element;
                        //引用名称
                        PsiElement value = fieldType.getIdentifier();
                        if (value == null) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        //找出不带前缀的引用名称起始位置
                        int startOffset = value.getStartOffsetInParent();
                        if (value.getText() != null) {
                            TextRange property = new TextRange(startOffset, startOffset + value.getText().length());
                            return new PsiReference[]{new JceFieldTypeReference(fieldType, property)};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
        // registrar.registerReferenceProvider(psiElement(JceFieldType.class).withChild(psiElement(JceRefModule.class)),
        //         new PsiReferenceProvider() {
        //             @NotNull
        //             @Override
        //             public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
        //                                                          @NotNull ProcessingContext context) {
        //                 //Module
        //                 if (!(element instanceof JceFieldType)) {
        //                     return PsiReference.EMPTY_ARRAY;
        //                 }
        //                 JceRefModule refModule = ((JceFieldType) element).getRefModule();
        //                 if (refModule != null) {
        //                     String value = refModule.getText();
        //                     if (value != null) {
        //                         TextRange property = new TextRange(0, value.length());
        //                         return new PsiReference[]{new JceModuleReference(element, property)};
        //                     }
        //                 }
        //                 return PsiReference.EMPTY_ARRAY;
        //             }
        //         });
    }
}
