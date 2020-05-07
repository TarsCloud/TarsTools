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

package com.tencent.jceplugin.language.jcedoc.psi;

import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.language.jcedoc.JceDocLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JceDocTokenType extends IElementType {
    public JceDocTokenType(@NotNull @NonNls String debugName) {
        super(debugName, JceDocLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "JceDocTokenType." + super.toString();
    }
}