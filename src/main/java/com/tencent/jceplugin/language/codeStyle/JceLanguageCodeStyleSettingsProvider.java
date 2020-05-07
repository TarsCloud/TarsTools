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

import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import com.tencent.jceplugin.language.JceColorSettingsPage;
import com.tencent.jceplugin.language.JceLanguage;
import com.tencent.jceplugin.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class JceLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
    private static final String[] ALIGN_OPTIONS = Arrays.stream(JceCodeStyleSettings.PropertyAlignment.values())
            .map(JceCodeStyleSettings.PropertyAlignment::getDescription)
            .toArray(String[]::new);
    private static final int[] ALIGN_VALUES =
            ArrayUtil.toIntArray(
                    ContainerUtil.map(JceCodeStyleSettings.PropertyAlignment.values(), JceCodeStyleSettings.PropertyAlignment::getId));

    @NotNull
    @Override
    public Language getLanguage() {
        return JceLanguage.INSTANCE;
    }

    @Override
    public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showStandardOptions("SPACE_WITHIN_BRACKETS",
                    "SPACE_WITHIN_BRACES",
                    "SPACE_AFTER_COMMA",
                    "SPACE_BEFORE_COMMA");
        }
        if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
            consumer.showCustomOption(JceCodeStyleSettings.class, "FIELD_ALIGNMENT", "Align field in struct", null, ALIGN_OPTIONS, ALIGN_VALUES);
            consumer.showCustomOption(JceCodeStyleSettings.class, "ENUM_ALIGNMENT", "Align enum", null, ALIGN_OPTIONS, ALIGN_VALUES);
        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE");
        }
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        return Utils.readFile(JceColorSettingsPage.class, "/sample.jce");
    }
}