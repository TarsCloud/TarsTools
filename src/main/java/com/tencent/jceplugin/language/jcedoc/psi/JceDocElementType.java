package com.tencent.jceplugin.language.jcedoc.psi;

import com.intellij.psi.tree.IElementType;
import com.tencent.jceplugin.language.jcedoc.JceDocLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JceDocElementType extends IElementType {
    public JceDocElementType(@NotNull @NonNls String debugName) {
        super(debugName, JceDocLanguage.INSTANCE);
    }
}