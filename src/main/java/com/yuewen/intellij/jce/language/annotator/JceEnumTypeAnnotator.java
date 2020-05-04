package com.yuewen.intellij.jce.language.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.yuewen.intellij.jce.language.JceSyntaxHighlighter;
import com.yuewen.intellij.jce.language.psi.JceEnumMember;
import com.yuewen.intellij.jce.language.psi.JceEnumType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JceEnumTypeAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceEnumType)) return;
        JceEnumType enumType = (JceEnumType) element;
        //标出identifier
        PsiElement nameIdentifier = enumType.getNameIdentifier();
        if (nameIdentifier == null) {
            //语法错误
            return;
        }
        Annotation structNameAnnotation = annotationHolder.createInfoAnnotation(nameIdentifier, null);
        structNameAnnotation.setTextAttributes(JceSyntaxHighlighter.CLASS_NAME);
        List<JceEnumMember> fieldInfoList = enumType.getEnumMemberList();
        if (!fieldInfoList.isEmpty()) {
            Set<String> fieldNameSet = new HashSet<>();
            //字段的起始tag
            Set<Integer> tagSet = new HashSet<>(fieldInfoList.size());
            for (JceEnumMember enumMember : fieldInfoList) {
                PsiElement identifier = enumMember.getIdentifier();
                //标出identifier
                Annotation annotation = annotationHolder.createInfoAnnotation(identifier, null);
                //字段
                annotation.setTextAttributes(JceSyntaxHighlighter.FIELD_DECLARATION);
                //region 检查名称是否重复
                if (fieldNameSet.contains(enumMember.getName())) {
                    //已存在同样名字的字段
                    annotationHolder.createErrorAnnotation(identifier, "Duplicate enum member name " + enumMember.getName() + " in enum " + enumType.getName() + "");
                }
                fieldNameSet.add(enumMember.getName());
                //endregion
                //region 检查enum成员值
                PsiElement enumMemberValue = enumMember.getNumInt();
                if (enumMemberValue != null) {
                    //检查enum成员值
                    int currentTag = 0;
                    try {
                        currentTag = Integer.parseInt(enumMemberValue.getText());
                        if (tagSet.contains(currentTag)) {
                            //重复tag
                            annotationHolder.createWarningAnnotation(enumMemberValue, "Duplicate enum member value " + currentTag);
                        }
                        tagSet.add(currentTag);
                    } catch (NumberFormatException e) {
                        //格式不对
                        annotationHolder.createErrorAnnotation(enumMemberValue, "Invalid enum member value " + currentTag);
                    }
                }
                //endregion
            }
        }
    }
}
