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

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SmartList;
import com.tencent.jceplugin.language.psi.JceRefStructField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * key [structName, field1, field2, ...]
 */
public class JceKeyInfoStructFieldReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(JceRefStructField.class), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                         @NotNull ProcessingContext context) {
                if (!(element instanceof JceRefStructField)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                JceRefStructField refStructField = (JceRefStructField) element;
                //struct
                List<PsiReference> psiReferences = new SmartList<>();
                psiReferences.add(new JceKeyInfoStructFieldReference(refStructField, new TextRange(0, refStructField.getTextRangeInParent().getLength())));
                return psiReferences.toArray(new PsiReference[0]);
            }
        });
    }
}
