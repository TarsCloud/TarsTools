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

package com.tencent.jceplugin.language.breadcrumbs;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider;
import com.tencent.jceplugin.language.JceLanguage;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JceBreadcrumbsProvider implements BreadcrumbsProvider {
    private static final Language[] LANGUAGES = new Language[]{JceLanguage.INSTANCE};

    @Override
    public Language[] getLanguages() {
        return LANGUAGES;
    }

    @Override
    public boolean acceptElement(@NotNull PsiElement e) {
        return e instanceof JceModuleInfo || e instanceof JceConstType || e instanceof JceStructType || e instanceof JceEnumType
                || e instanceof JceKeyInfo || e instanceof JceInterfaceInfo || e instanceof JceFunctionInfo || e instanceof JceIncludeInfo;
    }

    @NotNull
    @Override
    public String getElementInfo(@NotNull PsiElement e) {
        if (e instanceof JceNamedElement) {
            return Optional.ofNullable(((JceNamedElement) e).getName()).orElse("");
        }
        return "";
    }

    @Nullable
    @Override
    public String getElementTooltip(@NotNull PsiElement e) {
        if (e instanceof JceNamedElement) {
            return Optional.ofNullable(((JceNamedElement) e).getName()).orElse("");
        }
        return "";
    }

    @NotNull
    @Override
    public List<? extends Action> getContextActions(@NotNull PsiElement element) {
        // JsonQualifiedNameKind[] values = JsonQualifiedNameKind.values();
        List<Action> actions = new ArrayList<>();
        // for (JsonQualifiedNameKind kind : values) {
        //     actions.add(new AbstractAction("Copy reference path") {
        //         @Override
        //         public void actionPerformed(ActionEvent e) {
        //             CopyPasteManager.getInstance().setContents(new StringSelection(JsonQualifiedNameProvider.generateQualifiedName(element, kind)));
        //         }
        //     });
        // }
        return actions;
    }
}
