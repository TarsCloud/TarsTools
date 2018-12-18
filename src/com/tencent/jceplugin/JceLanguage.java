package com.tencent.jceplugin;

import com.intellij.lang.Language;

public class JceLanguage extends Language {

    public static final JceLanguage INSTANCE = new JceLanguage();

    public JceLanguage() {
        super("Jce");
    }
}
