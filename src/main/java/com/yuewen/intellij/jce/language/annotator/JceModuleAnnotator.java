package com.yuewen.intellij.jce.language.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.yuewen.intellij.jce.language.JceSyntaxHighlighter;
import com.yuewen.intellij.jce.language.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JceModuleAnnotator implements Annotator {
    public static void findAndAnnotateDuplicateSymbol(Collection<JceNamedElement> namedElementCollection, JceModuleInfo moduleInfo, @NotNull AnnotationHolder annotationHolder) {
        if (!namedElementCollection.isEmpty()) {
            Map<String, JceNamedElement> nameSet = new HashMap<>(namedElementCollection.size());
            for (JceNamedElement namedElement : namedElementCollection) {
                PsiElement identifier = namedElement.getNameIdentifier();
                if (identifier == null) {
                    //语法错误
                    continue;
                }
                //region 检查名称是否重复
                JceNamedElement anotherNamedElement = nameSet.get(namedElement.getName());
                if (anotherNamedElement != null) {
                    //已存在同样名字的字段
                    Annotation errorAnnotation1 = annotationHolder.createErrorAnnotation(identifier, "Duplicate symbol name '" + namedElement.getName() + "' in module '" + moduleInfo.getName() + "'");
                    Annotation errorAnnotation = annotationHolder.createErrorAnnotation(Objects.requireNonNull(anotherNamedElement.getNameIdentifier()),
                            "Duplicate symbol name '" + namedElement.getName() + "' in module '" + moduleInfo.getName() + "'");
                }
                nameSet.put(namedElement.getName(), namedElement);
                //endregion
            }
        }
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceFile)) return;
        JceFile jceFile = (JceFile) element;
        Set<String> moduleNameSet = new HashSet<>(jceFile.getModuleList().length);
        for (JceModuleInfo moduleInfo : jceFile.getModuleList()) {
            //标出identifier
            PsiElement nameIdentifier = moduleInfo.getNameIdentifier();
            if (nameIdentifier == null) {
                //语法错误
                return;
            }
            if (moduleNameSet.contains(moduleInfo.getName())) {
                //module命名重复
                Annotation annotation = annotationHolder.createErrorAnnotation(moduleInfo.getNameIdentifier(), "Duplicate module name '" + moduleInfo.getName() + "'");
            }
            moduleNameSet.add(moduleInfo.getName());
            Annotation structNameAnnotation = annotationHolder.createInfoAnnotation(nameIdentifier, null);
            structNameAnnotation.setTextAttributes(JceSyntaxHighlighter.CLASS_NAME);
            Collection<JceNamedElement> namedElementCollection = PsiTreeUtil.getChildrenOfAnyType(moduleInfo, JceEnumType.class, JceStructType.class, JceConstType.class);
            findAndAnnotateDuplicateSymbol(namedElementCollection, moduleInfo, annotationHolder);
            namedElementCollection = PsiTreeUtil.getChildrenOfAnyType(moduleInfo, JceInterfaceInfo.class);
            findAndAnnotateDuplicateSymbol(namedElementCollection, moduleInfo, annotationHolder);
        }
    }
}
