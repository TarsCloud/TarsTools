package com.tencent.jceplugin.language;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.tencent.jceplugin.language.psi.JceTypes;

public class JceQuoteMatcher extends SimpleTokenSetQuoteHandler {
    public JceQuoteMatcher() {
        super(JceTypes.STRING_LITERAL, JceTypes.WRONG_STRING_LITERAL);
    }
}