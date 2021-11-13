/**
 * Tencent is pleased to support the open source community by making Tars available.
 * <p>
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 * <p>
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * https://opensource.org/licenses/BSD-3-Clause
 * <p>
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
import com.tencent.jceplugin.language.psi.JceEnumMember;
import com.tencent.jceplugin.language.psi.JceEnumType;
import com.tencent.jceplugin.language.psi.JceFieldDefaultAssignment;
import com.tencent.jceplugin.language.psi.JceFieldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JceFieldDefaultAssignmentReference extends PsiReferenceBase<JceFieldDefaultAssignment> implements PsiPolyVariantReference, PsiQualifiedReference {
    @Nullable
    private final JceEnumType enumType;
    @Nullable
    private final String enumMemberName;

    public JceFieldDefaultAssignmentReference(@NotNull JceFieldDefaultAssignment element, TextRange rangeInElement) {
        super(element, rangeInElement);
        if (element.getStringLiterals() != null && element.getStringLiterals().getIdentifier() != null) {
            enumMemberName = element.getStringLiterals().getIdentifier().getText();
        } else {
            enumMemberName = null;
        }
        PsiElement parent = PsiTreeUtil.findFirstParent(element, e -> e instanceof JceFieldInfo);
        if (parent instanceof JceFieldInfo) {
            JceFieldInfo fieldInfo = (JceFieldInfo) parent;
            if (fieldInfo.getFieldType() != null && fieldInfo.getIdentifier() != null && fieldInfo.getFieldType().getReference() != null) {
                PsiElement resolve = fieldInfo.getFieldType().getReference().resolve();
                if (resolve instanceof JceEnumType) {
                    enumType = (JceEnumType) resolve;
                } else {
                    enumType = null;
                }
            } else {
                enumType = null;
            }
        } else {
            enumType = null;
        }
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        if (enumType == null || enumMemberName == null || enumMemberName.isEmpty()) {
            return new ResolveResult[0];
        }
        final List<JceEnumMember> enumMemberList = enumType.getEnumMemberList();
        List<ResolveResult> results = new ArrayList<>();
        for (JceEnumMember enumMember : enumMemberList) {
            if (incompleteCode || enumMemberName.equals(enumMember.getName())) {
                results.add(new PsiElementResolveResult(enumMember));
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
        if (enumType == null || enumMemberName == null || enumMemberName.isEmpty()) {
            return new LookupElement[0];
        }
        List<JceEnumMember> properties = enumType.getEnumMemberList();
        List<LookupElement> variants = new ArrayList<>();
        for (final JceEnumMember property : properties) {
            if (property.getName() != null && property.getName().length() > 0) {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder
                        .create(property)
                        .withIcon(PlatformIcons.ENUM_ICON)
                        .withTypeText(enumType.getName());
                if (property.getNumInt() != null) {
                    lookupElementBuilder = lookupElementBuilder.withTailText("=" + property.getNumInt().getText());
                }
                variants.add(lookupElementBuilder);
            }
        }
        return variants.toArray();
    }

    @Nullable
    @Override
    public PsiElement getQualifier() {
        if (enumType == null) {
            return null;
        }
        return enumType.getNameIdentifier();
    }

    @Nullable
    @Override
    public String getReferenceName() {
        return enumMemberName;
    }
}
