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

package com.tencent.jceplugin.language.codeStyle;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class JceCodeStyleSettings extends CustomCodeStyleSettings {
    public static final int DO_NOT_ALIGN = PropertyAlignment.DO_NOT_ALIGN.getId();
    public static final int ALIGN = PropertyAlignment.ALIGN.getId();

    protected JceCodeStyleSettings(CodeStyleSettings container) {
        super("JceCodeStyleSettings", container);
    }

    /**
     * Contains value of {@link PropertyAlignment#getId()}
     *
     * @see #DO_NOT_ALIGN
     * @see #ALIGN
     */
    public int FIELD_ALIGNMENT = PropertyAlignment.ALIGN.getId();
    /**
     * Contains value of {@link PropertyAlignment#getId()}
     *
     * @see #DO_NOT_ALIGN
     * @see #ALIGN
     */
    public int ENUM_ALIGNMENT = PropertyAlignment.ALIGN.getId();

    public enum PropertyAlignment {
        DO_NOT_ALIGN(0, "Do not align"),
        ALIGN(1, "Align");

        private final String myKey;
        private final int myId;

        PropertyAlignment(int id, @NotNull String key) {
            myKey = key;
            myId = id;
        }

        @NotNull
        public String getDescription() {
            return myKey;
        }

        public int getId() {
            return myId;
        }
    }
}
