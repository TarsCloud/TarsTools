package com.yuewen.intellij.jce.language.annotator;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.yuewen.intellij.jce.language.JceSyntaxHighlighter;
import com.yuewen.intellij.jce.language.psi.*;
import com.yuewen.intellij.jce.language.quickfix.RemoveFieldDefaultValueQuickFix;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class JceFieldDefaultValueAnnotator implements Annotator {
    private static <T extends Number> T getValue(String text, int radix, long max, Class<T> clazz) {
        try {
            Method method = clazz.getMethod("valueOf", String.class, int.class);
            @SuppressWarnings("unchecked")
            T value = (T) method.invoke(null, text, radix);
            return max == 0 || (0 <= value.longValue() && value.longValue() <= max) ? value : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (!(element instanceof JceFieldInfo)) return;
        JceFieldInfo fieldInfo = (JceFieldInfo) element;
        JceFieldDefaultAssignment defaultAssignment = fieldInfo.getFieldDefaultAssignment();
        if (defaultAssignment == null || defaultAssignment.getStringLiterals() == null) {
            //没默认值，不检查
            return;
        }
        if (defaultAssignment.getStringLiterals().getWrongStringLiteral() != null) {
            //是错误的字符串
            return;
        }
        JceFieldType fieldType = fieldInfo.getFieldType();
        if (fieldType == null) {
            //语法错误
            return;
        }
        if (fieldType.getMapType() != null || fieldType.getVectorType() != null) {
            //map/vector类型，不能有默认值
            if (defaultAssignment.getStringLiterals() != null) {
                annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "No default value allowed for map");
            }
        } else if (fieldType.getBuiltInTypes() != null) {
            //内建类型
            String baseType = fieldType.getBuiltInTypes().getText();
            //int,string,byte,short,long,double,float,bool
            boolean isUnsigned = baseType.startsWith("unsigned");
            //去掉unsigned
            baseType = baseType.replace("unsigned ", "");
            if (isUnsigned && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                //无符号数，却有了负号
                annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "negative value for unsigned type");
                return;
            }
            switch (baseType) {
                case "bool":
                    if (!defaultAssignment.getStringLiterals().getText().equals("true") && !defaultAssignment.getStringLiterals().getText().equals("false")) {
                        //布尔型，默认值却不是true/false
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid default value for bool, true/false expected");
                    }
                    break;
                case "string":
                    if (defaultAssignment.getStringLiterals().getStringLiteral() == null) {
                        //默认值不是字符串
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid default value for string, string expected");
                    }
                    break;
                case "byte": {
                    //1字节
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null
                            && defaultAssignment.getStringLiterals().getStringLiteral() == null) {
                        //不是整数且也不是字符串
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for byte");
                        return;
                    }
                    if (defaultAssignment.getStringLiterals().getStringLiteral() != null) {
                        if (defaultAssignment.getStringLiterals().getStringLiteral().getText().length() > 3) {
                            //去掉2个引号后的长度大于1字节
                            annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "String " + defaultAssignment.getStringLiterals().getText() + " is too long for byte");
                        }
                        //1字符，没问题
                        return;
                    }
                    //解析整数
                    Short shortValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        shortValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Short.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        shortValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Short.class);
                    }
                    if (shortValue == null) {
                        //不是数字
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for byte");
                        return;
                    }
                    if (isUnsigned) {
                        //无符号
                        if (shortValue > Byte.MAX_VALUE - Byte.MIN_VALUE) {
                            //超过范围
                            annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), defaultAssignment.getStringLiterals().getText() + "(" + shortValue.intValue() + ") is out of range for byte");
                        }
                    } else {
                        //有符号
                        if (shortValue < Byte.MIN_VALUE || shortValue > Byte.MAX_VALUE) {
                            //超过范围
                            annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), defaultAssignment.getStringLiterals().getText() + "(" + shortValue.intValue() + ") is out of range for byte");
                        }
                    }
                    if (shortValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.createWarningAnnotation(defaultAssignment.getStringLiterals(), "negative zero, are you insane?");
                    }
                    break;
                }
                case "short": {
                    //判断整型
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null) {
                        //不是整数
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for short");
                        return;
                    }
                    //解析整数
                    Integer shortValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        shortValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Integer.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        shortValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Integer.class);
                    }
                    if (shortValue == null) {
                        //不是数字
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for short");
                        return;
                    }
                    if (isUnsigned) {
                        //无符号
                        if (shortValue > Short.MAX_VALUE - Short.MIN_VALUE) {
                            //超过范围
                            annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), defaultAssignment.getStringLiterals().getText() + "(" + shortValue + ") is out of range for short");
                        }
                    } else {
                        //有符号
                        if (shortValue < Short.MIN_VALUE || shortValue > Short.MAX_VALUE) {
                            //超过范围
                            annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), defaultAssignment.getStringLiterals().getText() + "(" + shortValue + ") is out of range for short");
                        }
                    }
                    if (shortValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.createWarningAnnotation(defaultAssignment.getStringLiterals(), "negative zero, are you insane?");
                    }
                    break;
                }
                case "int":
                    //判断整型
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null) {
                        //不是整数
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for int");
                        return;
                    }
                    //解析整数
                    Long intValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        intValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Long.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        intValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Long.class);
                    }
                    if (intValue == null) {
                        //不是数字
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for int");
                        return;
                    }
                    if (isUnsigned) {
                        //无符号
                        if (intValue > (long) Integer.MAX_VALUE - Integer.MIN_VALUE) {
                            //超过范围
                            annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), defaultAssignment.getStringLiterals().getText() + "(" + intValue + ") is out of range for int");
                        }
                    } else {
                        //有符号
                        if (intValue < Integer.MIN_VALUE || intValue > Integer.MAX_VALUE) {
                            //超过范围
                            annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), defaultAssignment.getStringLiterals().getText() + "(" + intValue + ") is out of range for int");
                        }
                    }
                    if (intValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.createWarningAnnotation(defaultAssignment.getStringLiterals(), "negative zero, are you insane?");
                    }
                    break;
                case "long":
                    //判断整型
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null) {
                        //不是整数
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for long");
                        return;
                    }
                    //解析整数
                    Long longValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        longValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Long.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        longValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Long.class);
                    }
                    if (longValue == null) {
                        //不是数字
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for long");
                    } else if (longValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.createWarningAnnotation(defaultAssignment.getStringLiterals(), "negative zero, are you insane?");
                    }
                    break;
                case "double":
                case "float":
                    //判断浮点
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null
                            && defaultAssignment.getStringLiterals().getNumDouble() == null) {
                        //不是数字
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for " + baseType);
                        return;
                    }
                    //解析整数
                    Double doubleValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        doubleValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Double.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        doubleValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Double.class);
                    } else if (defaultAssignment.getStringLiterals().getNumDouble() != null) {
                        doubleValue = getValue(defaultAssignment.getStringLiterals().getNumDouble().getText(), 10, 0, Double.class);
                    }
                    if (doubleValue == null) {
                        //不是数字
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for " + baseType);
                        return;
                    }
                    if ("float".equals(baseType) && (doubleValue > Long.MAX_VALUE || doubleValue < Long.MIN_VALUE)) {
                        //float类型，但默认值超过float范围了
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for " + baseType);
                    }
                    if (doubleValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.createWarningAnnotation(defaultAssignment.getStringLiterals(), "negative zero, are you insane?");
                    }
                    break;
            }
        } else {
            if (defaultAssignment.getStringLiterals() == null || defaultAssignment.getStringLiterals().getIdentifier() == null || defaultAssignment.getStringLiterals().getIdentifier().getText().isEmpty()) {
                //语法错误
                if (defaultAssignment.getStringLiterals() != null) {
                    annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "Invalid default value, identifier expected");
                }
                return;
            }
            PsiReference reference = fieldType.getReference();
            if (reference != null) {
                PsiElement fieldTypeElement = reference.resolve();
                if (fieldTypeElement == null) {
                    //类型引用解析失败，有其他annotator来标注
                    return;
                }
                if (fieldTypeElement instanceof JceEnumType) {
                    //枚举类型的字段，默认值必须是枚举成员
                    if (defaultAssignment.getReference() == null || defaultAssignment.getReference().resolve() == null) {
                        annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(),
                                "Unresolved enum member " + defaultAssignment.getStringLiterals().getIdentifier().getText() + " in enum " + ((JceEnumType) fieldTypeElement).getName() + "");
                    } else {
                        //成功解析，标注一下高亮颜色
                        Annotation infoAnnotation = annotationHolder.createInfoAnnotation(defaultAssignment.getStringLiterals().getIdentifier(), null);
                        infoAnnotation.setTextAttributes(JceSyntaxHighlighter.FIELD_REFERENCE);
                    }
                } else {
                    //其他类型的字段不能有默认值，也就是struct类型
                    annotationHolder.createErrorAnnotation(defaultAssignment.getStringLiterals(), "No default value allowed for struct");
                }
            }
        }
        //require类型的标签不建议有默认值
        if (fieldInfo.getFieldLabel() != null) {
            ASTNode requireToken = fieldInfo.getFieldLabel().getNode().findChildByType(JceTypes.REQUIRE);
            if (requireToken != null) {
                //是require，但是有默认值
                Annotation warningAnnotation = annotationHolder.createWarningAnnotation(defaultAssignment, "Default value for require field is not recommended");
                warningAnnotation.registerFix(new RemoveFieldDefaultValueQuickFix(fieldInfo));
            }
        }
    }
}
