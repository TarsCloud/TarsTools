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

import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.util.ProcessingContext;
import com.tencent.jceplugin.language.JceFileType;
import com.tencent.jceplugin.language.psi.JceIncludeFilename;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JceFileReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(JceIncludeFilename.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                                 @NotNull ProcessingContext context) {
                        if (!(element instanceof JceIncludeFilename)) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        // PsiReference[] references = PathReferenceManager.getInstance().createReferences(element, false, false, true);
                        // return references;
                        // return FileReferenceUtil.restrict(PathReferenceManager.getInstance().createReferences(element, true, false, true, new FileType[]{JceFileType.INSTANCE}), FileReferenceUtil.byType(JceFileType.INSTANCE), true);
                        JceIncludeFilename includeFilename = (JceIncludeFilename) element;
                        return new FileReferenceSet(includeFilename) {
                            @Override
                            protected Condition<PsiFileSystemItem> getReferenceCompletionFilter() {
                                return e -> (e.getVirtualFile().getFileType() == JceFileType.INSTANCE
                                        || DIRECTORY_FILTER.value(e)) && !e.equals(getContainingFile());
                            }

                            @NotNull
                            @Override
                            public Collection<PsiFileSystemItem> computeDefaultContexts() {
                                final PsiFile containingFile = getContainingFile();
                                if (containingFile == null) {
                                    return Collections.emptyList();
                                }

                                final Set<VirtualFile> roots = new HashSet<>();
                                final VirtualFile parent = containingFile.getVirtualFile().getParent();
                                roots.add(parent);
                                return toFileSystemItems(roots);
                            }
                        }.getAllReferences();
                    }
                });
    }
}
