package com.tencent.jceplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.JceLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JceElementType extends IElementType {

    public JceElementType(@NotNull @NonNls String debugName) {
        super(debugName, JceLanguage.INSTANCE);
    }
}
