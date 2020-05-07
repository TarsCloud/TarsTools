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

package com.tencent.jceplugin.language;

import com.intellij.codeInsight.generation.IndentedCommenter;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.language.jcedoc.psi.PsiJceDocElement;
import com.tencent.jceplugin.language.psi.JceElementType;
import com.tencent.jceplugin.language.psi.JceTypes;
import org.jetbrains.annotations.Nullable;

public class JceCommenter implements CodeDocumentationAwareCommenter, IndentedCommenter {
    @Nullable
    @Override
    public IElementType getLineCommentTokenType() {
        return JceTypes.LINE_COMMENT;
    }

    @Nullable
    @Override
    public IElementType getBlockCommentTokenType() {
        return JceTypes.BLOCK_COMMENT;
    }

    @Nullable
    @Override
    public IElementType getDocumentationCommentTokenType() {
        return JceElementType.DOC_COMMENT;
    }

    @Nullable
    @Override
    public String getDocumentationCommentPrefix() {
        return "/**";
    }

    @Nullable
    @Override
    public String getDocumentationCommentLinePrefix() {
        return "*";
    }

    @Nullable
    @Override
    public String getDocumentationCommentSuffix() {
        return "*/";
    }

    @Override
    public boolean isDocumentationComment(PsiComment psiComment) {
        return psiComment instanceof PsiJceDocElement;
    }

    @Nullable
    @Override
    public String getLineCommentPrefix() {
        return "//";
    }

    @Nullable
    @Override
    public String getBlockCommentPrefix() {
        return "/*";
    }

    @Nullable
    @Override
    public String getBlockCommentSuffix() {
        return "*/";
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentSuffix() {
        return null;
    }

    @Nullable
    @Override
    public Boolean forceIndentedLineComment() {
        return true;
    }

    @Nullable
    @Override
    public Boolean forceIndentedBlockComment() {
        return true;
    }
}
