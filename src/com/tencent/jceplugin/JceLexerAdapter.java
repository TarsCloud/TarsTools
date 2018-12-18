package com.tencent.jceplugin;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class JceLexerAdapter extends FlexAdapter {

    public JceLexerAdapter() {
        super(new JceLexer((Reader) null));
    }
}
