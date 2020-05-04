package com.yuewen.intellij.jce.language.manipulator;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.yuewen.intellij.jce.language.psi.JceElementFactory;
import com.yuewen.intellij.jce.language.psi.JceKeyInfo;
import org.jetbrains.annotations.NotNull;

public class JceKeyInfoManipulator extends AbstractElementManipulator<JceKeyInfo> {
    @Override
    public JceKeyInfo handleContentChange(@NotNull JceKeyInfo keyInfo, @NotNull TextRange rangeInParent, String newContent) {
        String text = keyInfo.getText();
        StringBuilder newKeyInfoCode = new StringBuilder();
        if (rangeInParent.getStartOffset() > 0) {
            newKeyInfoCode.append(text, 0, rangeInParent.getStartOffset());
        }
        newKeyInfoCode.append(newContent);
        newKeyInfoCode.append(text, rangeInParent.getEndOffset(), text.length());
        JceKeyInfo newKeyInfo = JceElementFactory.createKeyInfoByString(keyInfo.getProject(), newKeyInfoCode.toString());
        return (JceKeyInfo) keyInfo.replace(newKeyInfo);
    }
}
