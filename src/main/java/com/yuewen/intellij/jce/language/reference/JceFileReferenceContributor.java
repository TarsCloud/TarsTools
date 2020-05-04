package com.yuewen.intellij.jce.language.reference;

import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.util.ProcessingContext;
import com.yuewen.intellij.jce.language.JceFileType;
import com.yuewen.intellij.jce.language.psi.JceIncludeFilename;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class JceFileReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(psiElement(JceIncludeFilename.class),
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
