package com.yuewen.intellij.jce.language.codeStyle;

import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import com.yuewen.intellij.jce.language.JceColorSettingsPage;
import com.yuewen.intellij.jce.language.JceLanguage;
import com.yuewen.intellij.jce.util.Utils;
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