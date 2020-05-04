package com.tencent.jceplugin.language.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.tencent.jceplugin.language.JceSyntaxHighlighter;
import com.tencent.jceplugin.language.psi.JceFunctionInfo;
import com.tencent.jceplugin.language.psi.JceInterfaceInfo;
import com.tencent.jceplugin.language.psi.JceNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JceInterfaceAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceInterfaceInfo)) return;
        JceInterfaceInfo interfaceInfo = (JceInterfaceInfo) element;
        //标出identifier
        PsiElement nameIdentifier = interfaceInfo.getNameIdentifier();
        if (nameIdentifier == null) {
            //语法错误
            return;
        }
        Annotation structNameAnnotation = annotationHolder.createInfoAnnotation(nameIdentifier, null);
        structNameAnnotation.setTextAttributes(JceSyntaxHighlighter.INTERFACE_NAME);
        List<JceFunctionInfo> functionInfoList = interfaceInfo.getFunctionInfoList();
        List<JceNamedElement> namedElementList = new ArrayList<>(functionInfoList);
        if (!namedElementList.isEmpty()) {
            Set<String> fieldNameSet = new HashSet<>();
            for (JceNamedElement fieldType : namedElementList) {
                PsiElement identifier = fieldType.getNameIdentifier();
                if (identifier == null) {
                    //语法错误
                    continue;
                }
                //region 检查名称是否重复
                if (fieldNameSet.contains(fieldType.getName())) {
                    //已存在同样名字的字段
                    Annotation errorAnnotation = annotationHolder.createErrorAnnotation(identifier, "Duplicate function name '" + fieldType.getName() + "' in interface '" + interfaceInfo.getName() + "'");
                }
                fieldNameSet.add(fieldType.getName());
                //endregion
            }
        }
    }
}
