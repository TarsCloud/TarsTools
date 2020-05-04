package com.tencent.jceplugin.language.jcedoc.psi;

import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.language.jcedoc.JceDocLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JceDocTokenType extends IElementType {
    public JceDocTokenType(@NotNull @NonNls String debugName) {
        super(debugName, JceDocLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "JceDocTokenType." + super.toString();
    }
}