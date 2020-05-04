package com.yuewen.intellij.jce.language.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.yuewen.intellij.jce.language.psi.JceFile;
import com.yuewen.intellij.jce.language.psi.JceIncludeFilename;
import com.yuewen.intellij.jce.language.psi.JceIncludeInfo;
import com.yuewen.intellij.jce.language.quickfix.RemoveIncludeQuickFix;
import org.jetbrains.annotations.NotNull;

public class JceIncludeFileAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceIncludeInfo)) {
            return;
        }
        JceIncludeFilename includeFilename = ((JceIncludeInfo) element).getIncludeFilename();
        if (includeFilename == null) {
            //语法错误
            return;
        }
        if (includeFilename.getReference() == null) {
            //引用错误
            return;
        }
        TextRange valueTextRange = ElementManipulators.getValueTextRange(includeFilename).shiftRight(includeFilename.getTextRange().getStartOffset());
        if (includeFilename.getFileName().isEmpty()) {
            //文件名错误
            //引用的不是jce文件
            annotationHolder.createErrorAnnotation(valueTextRange, "Jce file path is required");
            return;
        }
        PsiElement referenceJceFile = includeFilename.getReference().resolve();
        if (!(referenceJceFile instanceof JceFile)) {
            //引用的文件不是jce文件
            annotationHolder.createErrorAnnotation(valueTextRange, "Invalid Jce file '" + includeFilename.getFileName() + "'");
            return;
        }
        if (referenceJceFile == includeFilename.getContainingFile()) {
            //引用了当前文件
            annotationHolder.createErrorAnnotation(valueTextRange, "Can not include current jce file");
            return;
        }
        //检查重复引用
        JceFile jceFile = (JceFile) element.getContainingFile();
        for (JceIncludeInfo jceIncludeInfo : jceFile.getIncludeList()) {
            if (jceIncludeInfo == element) {
                continue;
            }
            if (jceIncludeInfo.getIncludeFilename() == null) {
                continue;
            }
            PsiReference otherIncludeReference = jceIncludeInfo.getIncludeFilename().getReference();
            if (otherIncludeReference == null) {
                continue;
            }
            PsiElement otherIncludeFile = otherIncludeReference.resolve();
            if (!(otherIncludeFile instanceof JceFile)) {
                continue;
            }
            if (referenceJceFile.equals(otherIncludeFile)) {
                //引用了重复的文件
                Annotation warningAnnotation = annotationHolder.createWarningAnnotation(valueTextRange, "Duplicate include Jce file '" + ((JceFile) referenceJceFile).getVirtualFile().getPath() + "'");
                warningAnnotation.registerFix(new RemoveIncludeQuickFix((JceIncludeInfo) element));
            }
        }
    }
}
