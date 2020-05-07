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
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import com.tencent.jceplugin.language.psi.JceRefStruct;
import com.tencent.jceplugin.language.psi.JceStructType;
import icons.JceIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 结构体引用
 * key [CommonInParam, field1, field2]
 */
public class JceKeyInfoStructReference extends PsiReferenceBase<JceRefStruct> implements PsiPolyVariantReference {
    private final String structName;
    private final JceModuleInfo moduleInfo;

    public JceKeyInfoStructReference(@NotNull JceRefStruct element, TextRange rangeInElement) {
        super(element, rangeInElement);
        structName = element.getIdentifier().getText();
        moduleInfo = (JceModuleInfo) PsiTreeUtil.findFirstParent(myElement, e -> e instanceof JceModuleInfo);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        List<ResolveResult> results = new ArrayList<>();
        if (moduleInfo != null && !structName.isEmpty()) {
            //找到module
            for (JceStructType jceStructType : JceUtil.findStruct(moduleInfo, incompleteCode ? null : structName)) {
                results.add(new PsiElementResolveResult(jceStructType));
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
        if (moduleInfo != null) {
            for (JceStructType jceStructType : JceUtil.findStruct(moduleInfo, null)) {
                variants.add(LookupElementBuilder
                        .create(jceStructType).withIcon(JceIcons.STRUCT)
                        .withTypeText(moduleInfo.getName()));
            }
        }
        return variants.toArray();
    }
}
