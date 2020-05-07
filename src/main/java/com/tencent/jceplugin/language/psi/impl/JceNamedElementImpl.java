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

package com.tencent.jceplugin.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.tencent.jceplugin.language.psi.JceNamedElement;

public abstract class JceNamedElementImpl extends ASTWrapperPsiElement implements JceNamedElement {
    public JceNamedElementImpl(ASTNode node) {
        super(node);
    }

    @Override
    public final int getTextOffset() {
        PsiElement nameElement = getNameIdentifier();
        if (nameElement == null) {
            return super.getTextOffset();
        }
        // return super.getTextOffset() + nameElement.getStartOffsetInParent();
        return nameElement.getTextOffset();
    }
}

