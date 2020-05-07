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
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import icons.JceIcons;
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
