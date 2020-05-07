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

package com.tencent.jceplugin.language.psi;

import com.intellij.navigation.ItemPresentation;

import javax.swing.*;

/**
 * Created by UnightSun on 2017/2/23.
 */
public class JceItemPresentation implements ItemPresentation {
    private Icon icon;
    private String name;

    public JceItemPresentation(Icon icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Override
    public String getPresentableText() {
        return name;
    }

    @Override
    public String getLocationString() {
        return null;
    }

    @Override
    public Icon getIcon(final boolean open) {
        return icon;
    }
};
