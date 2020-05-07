/**
 * Tencent is pleased to support the open source community by making Tars available.
 *
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformIcons;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.JceEnumType;
import com.tencent.jceplugin.language.psi.JceFieldType;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import com.tencent.jceplugin.language.psi.JceStructType;
import icons.JceIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JceFieldTypeReference extends PsiReferenceBase<JceFieldType> implements PsiPolyVariantReference, PsiQualifiedReference {
    private String key;

    public JceFieldTypeReference(@NotNull JceFieldType element, TextRange textRange) {
        super(element, textRange);
        assert element.getIdentifier() != null;
        key = element.getIdentifier().getText();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        JceModuleInfo moduleInfo = PsiTreeUtil.getParentOfType(myElement, JceModuleInfo.class);
        if (getQualifier() != null && getQualifier().getReference() != null) {
            moduleInfo = (JceModuleInfo) getQualifier().getReference().resolve();
        }
        List<ResolveResult> results = new ArrayList<>();
        final List<JceStructType> structList = JceUtil.findStruct(moduleInfo, incompleteCode ? null : key);
        for (JceStructType structType : structList) {
            results.add(new PsiElementResolveResult(structType));
        }
        final List<JceEnumType> enumList = JceUtil.findEnum(moduleInfo, incompleteCode ? null : key);
        for (JceEnumType enumType : enumList) {
            results.add(new PsiElementResolveResult(enumType));
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
        JceModuleInfo moduleInfo = PsiTreeUtil.getParentOfType(myElement, JceModuleInfo.class);
        if (getQualifier() != null && getQualifier().getReference() != null) {
            moduleInfo = (JceModuleInfo) getQualifier().getReference().resolve();
        }
        List<JceStructType> structTypes = JceUtil.findStruct(moduleInfo, null);
        for (final JceStructType structType : structTypes) {
            if (structType.getName() != null && structType.getName().length() > 0) {
                String typeText = Optional.ofNullable(moduleInfo).map(JceModuleInfo::getName).orElse("");
                variants.add(LookupElementBuilder
                        .create(structType).withIcon(JceIcons.STRUCT)
                        .withTypeText(typeText)
                );
            }
        }
        List<JceEnumType> enumTypes = JceUtil.findEnum(moduleInfo, null);
        for (final JceEnumType enumType : enumTypes) {
            if (enumType.getName() != null && enumType.getName().length() > 0) {
                String typeText = Optional.ofNullable(moduleInfo).map(JceModuleInfo::getName).orElse("");
                variants.add(LookupElementBuilder
                        .create(enumType).withIcon(PlatformIcons.ENUM_ICON)
                        .withTypeText(typeText)
                );
            }
        }
        // List<JceEnumMember> enumMemberTypes = JceUtil.findEnumMember(file);
        // for (final JceEnumMember enumMemberType : enumMemberTypes) {
        //     if (enumMemberType.getName() != null && enumMemberType.getName().length() > 0) {
        //         JceModuleInfo moduleInfo = PsiTreeUtil.getParentOfType(enumMemberType, JceModuleInfo.class);
        //         String typeText = Optional.ofNullable(moduleInfo).map(info -> info.getName() + "::").orElse("");
        //         JceEnumType enumType = PsiTreeUtil.getParentOfType(enumMemberType, JceEnumType.class);
        //         typeText += Optional.ofNullable(enumType).map(info -> info.getName() + "").orElse("");
        //         variants.add(LookupElementBuilder
        //                 .create(enumMemberType).withIcon(PlatformIcons.ENUM_ICON)
        //                 .withTypeText(typeText)
        //         );
        //     }
        // }
        return variants.toArray();
    }

    @Nullable
    @Override
    public PsiElement getQualifier() {
        return myElement.getRefModule();
    }

    @Nullable
    @Override
    public String getReferenceName() {
        return myElement.getIdentifier() != null ? myElement.getIdentifier().getText() : null;
    }
}
