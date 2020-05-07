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
