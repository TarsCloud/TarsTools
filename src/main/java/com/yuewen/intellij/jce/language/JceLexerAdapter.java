package com.yuewen.intellij.jce.language;

import com.intellij.lexer.FlexAdapter;

public class JceLexerAdapter extends FlexAdapter {
    public JceLexerAdapter() {
        super(new JceLexer());
    }
}
