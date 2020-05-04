package com.tencent.jceplugin.language.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.tencent.jceplugin.language.JceSyntaxHighlighter;
import com.tencent.jceplugin.language.psi.JceFunctionInfo;
import com.tencent.jceplugin.language.psi.JceFunctionParam;
import com.tencent.jceplugin.language.psi.JceFunctionParamList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class JceFunctionAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceFunctionInfo)) return;
        JceFunctionInfo functionInfo = (JceFunctionInfo) element;
        PsiElement identifier = functionInfo.getIdentifier();
        if (identifier == null) {
            //语法错误
            return;
        }
        //标出identifier
        Annotation annotation = annotationHolder.createInfoAnnotation(identifier, null);
        //字段
        annotation.setTextAttributes(JceSyntaxHighlighter.FUNCTION_DECLARATION);
        JceFunctionParamList functionParamList = functionInfo.getFunctionParamList();
        if (functionParamList != null) {
            Set<String> fieldNameSet = new HashSet<>();
            for (JceFunctionParam jceFunctionParam : functionParamList.getFunctionParamList()) {
                //标出identifier
                PsiElement paramIdentifier = jceFunctionParam.getIdentifier();
                if (paramIdentifier == null) {
                    //语法错误
                    continue;
                }
                if (jceFunctionParam.getName() == null || jceFunctionParam.getName().isEmpty()) {
                    //语法错误
                    continue;
                }
                if (fieldNameSet.contains(jceFunctionParam.getName())) {
                    //命名重复
                    Annotation errorAnnotation = annotationHolder.createErrorAnnotation(paramIdentifier, "Duplicate parameter name '" + jceFunctionParam.getName() + "' in function '" + functionInfo.getName() + "'");
                }
                Annotation paramAnnotation = annotationHolder.createInfoAnnotation(paramIdentifier, null);
                //参数名
                paramAnnotation.setTextAttributes(JceSyntaxHighlighter.PARAMETER);
                fieldNameSet.add(jceFunctionParam.getName());
            }
        }
    }
}
