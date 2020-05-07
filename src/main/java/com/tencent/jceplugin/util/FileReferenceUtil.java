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

/*
 * Copyright 2007 Sascha Weinreuter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.jceplugin.util;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Condition;
import com.intellij.patterns.PsiFilePattern;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.util.NotNullFunction;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.StandardPatterns.string;
import static com.intellij.patterns.XmlPatterns.xmlFile;
import static com.intellij.patterns.XmlPatterns.xmlTag;

public class FileReferenceUtil {
    public static PsiReference[] restrict(FileReference[] references, final Condition<? super PsiFile> cond, final Boolean soft) {
        return ContainerUtil.map2Array(references, PsiReference.class, (NotNullFunction<FileReference, PsiReference>) fileReference -> new MyFileReference(fileReference, cond, soft));
    }

    public static PsiReference[] restrict(FileReferenceSet set, final Condition<? super PsiFile> cond) {
        return restrict(set, cond, null);
    }

    public static PsiReference[] restrict(FileReferenceSet set, final Condition<? super PsiFile> cond, final Boolean soft) {
        return restrict(set.getAllReferences(), cond, soft);
    }

    public static Condition<PsiFile> byType(FileType instance) {
        return new TypeCondition(instance);
    }

    public static Condition<PsiFile> byNamespace(String ns) {
        return new PatternCondition(xmlFile().withRootTag(xmlTag().withNamespace(string().equalTo(ns))));
    }

    public static PsiReference[] restrict(PsiReference[] references, Condition<PsiFile> cond, boolean soft) {
        return ContainerUtil.map2Array(references, PsiReference.class, (NotNullFunction<PsiReference, PsiReference>) fileReference -> new MyFileReference((FileReference) fileReference, cond, soft));
    }

    private static class TypeCondition implements Condition<PsiFile> {
        private final FileType myType;

        TypeCondition(FileType type) {
            myType = type;
        }

        @Override
        public boolean value(PsiFile file) {
            return file.getFileType() == myType;
        }
    }

    private static class PatternCondition implements Condition<PsiFile> {
        private final PsiFilePattern myPattern;

        PatternCondition(PsiFilePattern pattern) {
            myPattern = pattern;
        }

        @Override
        public boolean value(PsiFile o) {
            return myPattern.accepts(o);
        }
    }

    private static class MyFileReference extends FileReference {
        private final Condition<? super PsiFile> myCond;
        private final Boolean mySoft;

        MyFileReference(FileReference fileReference, Condition<? super PsiFile> cond, @Nullable Boolean soft) {
            super(fileReference.getFileReferenceSet(), fileReference.getRangeInElement(), fileReference.getIndex(), fileReference.getCanonicalText());
            myCond = cond;
            mySoft = soft;
        }

        @Override
        public boolean isSoft() {
            return mySoft == null ? super.isSoft() : mySoft;
        }

        @Override
        public PsiFileSystemItem resolve() {
            final PsiFileSystemItem result = super.resolve();
            if (result instanceof PsiFile) {
                if (!myCond.value((PsiFile) result)) {
                    return null;
                }
            }
            return result;
        }

        @NotNull
        @Override
        public Object[] getVariants() {
            final Object[] variants = super.getVariants();
            return ContainerUtil.findAll(variants, o -> match(o, myCond)).toArray();
        }

        private static boolean match(Object o, Condition<? super PsiFile> cond) {
            if (o instanceof LookupElement) {
                o = ((LookupElement) o).getObject();
            }
            return (o instanceof PsiFileSystemItem) &&
                    (((PsiFileSystemItem) o).isDirectory() ||
                            (o instanceof PsiFile && cond.value((PsiFile) o)));
        }
    }
}
