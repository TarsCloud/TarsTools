package com.tencent.jceplugin.language;

import com.intellij.lexer.FlexAdapter;

public class JceLexerAdapter extends FlexAdapter {
    public JceLexerAdapter() {
        super(new JceLexer());
    }
}
