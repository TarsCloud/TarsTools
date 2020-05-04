package com.tencent.jceplugin.language.psi;

import com.intellij.navigation.ItemPresentation;

import javax.swing.*;

/**
 * Created by UnightSun on 2017/2/23.
 */
public class JceItemPresentation implements ItemPresentation {
    private Icon icon;
    private String name;

    public JceItemPresentation(Icon icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Override
    public String getPresentableText() {
        return name;
    }

    @Override
    public String getLocationString() {
        return null;
    }

    @Override
    public Icon getIcon(final boolean open) {
        return icon;
    }
};
