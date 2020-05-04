package com.tencent.jceplugin.language.jcedoc;

import com.intellij.lang.Language;

public class JceDocLanguage extends Language {
    public static final JceDocLanguage INSTANCE = new JceDocLanguage();

    private JceDocLanguage() {
        super("JceDoc");
    }
}
