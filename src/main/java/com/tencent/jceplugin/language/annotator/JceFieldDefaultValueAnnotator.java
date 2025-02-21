/**
 * Tencent is pleased to support the open source community by making Tars available.
 * <p>
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 * <p>
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * https://opensource.org/licenses/BSD-3-Clause
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language.annotator;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.tencent.jceplugin.language.JceSyntaxHighlighter;
import com.tencent.jceplugin.language.psi.JceEnumType;
import com.tencent.jceplugin.language.psi.JceFieldDefaultAssignment;
import com.tencent.jceplugin.language.psi.JceFieldInfo;
import com.tencent.jceplugin.language.psi.JceFieldType;
import com.tencent.jceplugin.language.psi.JceTypes;
import com.tencent.jceplugin.language.quickfix.RemoveFieldDefaultValueQuickFix;
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
        if (!(element instanceof JceFieldInfo fieldInfo)) return;
        JceFieldDefaultAssignment defaultAssignment = fieldInfo.getFieldDefaultAssignment();
        if (defaultAssignment == null || defaultAssignment.getStringLiterals() == null) {
            // 没默认值，不检查
            return;
        }
        if (defaultAssignment.getStringLiterals().getWrongStringLiteral() != null) {
            // 是错误的字符串
            return;
        }
        JceFieldType fieldType = fieldInfo.getFieldType();
        if (fieldType == null) {
            // 语法错误
            return;
        }
        if (fieldType.getMapType() != null || fieldType.getVectorType() != null) {
            // map/vector类型，不能有默认值
            if (defaultAssignment.getStringLiterals() != null) {
                annotationHolder.newAnnotation(HighlightSeverity.ERROR, "No default value allowed for map")
                        .range(defaultAssignment.getStringLiterals())
                        .create();
            }
        } else if (fieldType.getBuiltInTypes() != null) {
            // 内建类型
            String baseType = fieldType.getBuiltInTypes().getText();
            // int,string,byte,short,long,double,float,bool
            boolean isUnsigned = baseType.startsWith("unsigned");
            // 去掉unsigned
            baseType = baseType.replace("unsigned ", "");
            if (isUnsigned && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                // 无符号数，却有了负号
                annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Negative value for unsigned type")
                        .range(defaultAssignment.getStringLiterals())
                        .create();
                return;
            }
            switch (baseType) {
                case "bool":
                    if (!defaultAssignment.getStringLiterals().getText().equals("true") && !defaultAssignment.getStringLiterals().getText().equals("false")) {
                        // 布尔型，默认值却不是true/false
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid default value for bool, true/false expected")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    }
                    break;
                case "string":
                    if (defaultAssignment.getStringLiterals().getStringLiteral() == null) {
                        // 默认值不是字符串
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid default value for string, string expected")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    }
                    break;
                case "byte": {
                    // 1字节
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null
                            && defaultAssignment.getStringLiterals().getStringLiteral() == null) {
                        // 不是整数且也不是字符串
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for byte")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    if (defaultAssignment.getStringLiterals().getStringLiteral() != null) {
                        if (defaultAssignment.getStringLiterals().getStringLiteral().getText().length() > 3) {
                            // 去掉2个引号后的长度大于1字节
                            annotationHolder.newAnnotation(HighlightSeverity.ERROR, "String " + defaultAssignment.getStringLiterals().getText() + " is too long for byte")
                                    .range(defaultAssignment.getStringLiterals())
                                    .create();
                        }
                        // 1字符，没问题
                        return;
                    }
                    // 解析整数
                    Short shortValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        shortValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Short.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        shortValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Short.class);
                    }
                    if (shortValue == null) {
                        // 不是数字
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for byte")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    if (isUnsigned) {
                        // 无符号
                        if (shortValue > Byte.MAX_VALUE - Byte.MIN_VALUE) {
                            // 超过范围
                            annotationHolder.newAnnotation(HighlightSeverity.ERROR, defaultAssignment.getStringLiterals().getText() + "(" + shortValue.intValue() + ") is out of range for byte")
                                    .range(defaultAssignment.getStringLiterals())
                                    .create();
                        }
                    } else {
                        // 有符号
                        if (shortValue < Byte.MIN_VALUE || shortValue > Byte.MAX_VALUE) {
                            // 超过范围
                            annotationHolder.newAnnotation(HighlightSeverity.ERROR, defaultAssignment.getStringLiterals().getText() + "(" + shortValue.intValue() + ") is out of range for byte")
                                    .range(defaultAssignment.getStringLiterals())
                                    .create();
                        }
                    }
                    if (shortValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Negative zero, are you insane?")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    }
                    break;
                }
                case "short": {
                    // 判断整型
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null) {
                        // 不是整数
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for short")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    // 解析整数
                    Integer shortValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        shortValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Integer.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        shortValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Integer.class);
                    }
                    if (shortValue == null) {
                        // 不是数字
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for short")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    if (isUnsigned) {
                        // 无符号
                        if (shortValue > Short.MAX_VALUE - Short.MIN_VALUE) {
                            // 超过范围
                            annotationHolder.newAnnotation(HighlightSeverity.ERROR, defaultAssignment.getStringLiterals().getText() + "(" + shortValue + ") is out of range for short")
                                    .range(defaultAssignment.getStringLiterals())
                                    .create();
                        }
                    } else {
                        // 有符号
                        if (shortValue < Short.MIN_VALUE || shortValue > Short.MAX_VALUE) {
                            // 超过范围
                            annotationHolder.newAnnotation(HighlightSeverity.ERROR, defaultAssignment.getStringLiterals().getText() + "(" + shortValue + ") is out of range for short")
                                    .range(defaultAssignment.getStringLiterals())
                                    .create();
                        }
                    }
                    if (shortValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Negative zero, are you insane?")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    }
                    break;
                }
                case "int":
                    // 判断整型
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null) {
                        // 不是整数
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for int")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    // 解析整数
                    Long intValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        intValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Long.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        intValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Long.class);
                    }
                    if (intValue == null) {
                        // 不是数字
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for int")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    if (isUnsigned) {
                        // 无符号
                        if (intValue > (long) Integer.MAX_VALUE - Integer.MIN_VALUE) {
                            // 超过范围
                            annotationHolder.newAnnotation(HighlightSeverity.ERROR, defaultAssignment.getStringLiterals().getText() + "(" + intValue + ") is out of range for int")
                                    .range(defaultAssignment.getStringLiterals())
                                    .create();
                        }
                    } else {
                        // 有符号
                        if (intValue < Integer.MIN_VALUE || intValue > Integer.MAX_VALUE) {
                            // 超过范围
                            annotationHolder.newAnnotation(HighlightSeverity.ERROR, defaultAssignment.getStringLiterals().getText() + "(" + intValue + ") is out of range for int")
                                    .range(defaultAssignment.getStringLiterals())
                                    .create();
                        }
                    }
                    if (intValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Negative zero, are you insane?")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    }
                    break;
                case "long":
                    // 判断整型
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null) {
                        // 不是整数
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for long")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    // 解析整数
                    Long longValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        longValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Long.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        longValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Long.class);
                    }
                    if (longValue == null) {
                        // 不是数字
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for long")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    } else if (longValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Negative zero, are you insane?")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    }
                    break;
                case "double":
                case "float":
                    // 判断浮点
                    if (defaultAssignment.getStringLiterals().getNumInt() == null
                            && defaultAssignment.getStringLiterals().getHexInt() == null
                            && defaultAssignment.getStringLiterals().getNumDouble() == null) {
                        // 不是数字
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid value " + defaultAssignment.getStringLiterals().getText() + " for " + baseType)
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    // 解析整数
                    Double doubleValue = null;
                    if (defaultAssignment.getStringLiterals().getHexInt() != null) {
                        doubleValue = getValue(defaultAssignment.getStringLiterals().getHexInt().getText(), 16, 0, Double.class);
                    } else if (defaultAssignment.getStringLiterals().getNumInt() != null) {
                        doubleValue = getValue(defaultAssignment.getStringLiterals().getNumInt().getText(), 10, 0, Double.class);
                    } else if (defaultAssignment.getStringLiterals().getNumDouble() != null) {
                        doubleValue = getValue(defaultAssignment.getStringLiterals().getNumDouble().getText(), 10, 0, Double.class);
                    }
                    if (doubleValue == null) {
                        // 不是数字
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for " + baseType)
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                        return;
                    }
                    if ("float".equals(baseType) && (doubleValue > Long.MAX_VALUE || doubleValue < Long.MIN_VALUE)) {
                        // float类型，但默认值超过float范围了
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid number " + defaultAssignment.getStringLiterals().getText() + " for " + baseType)
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    }
                    if (doubleValue == 0 && defaultAssignment.getStringLiterals().getText().startsWith("-")) {
                        //-0
                        annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Negative zero, are you insane?")
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    }
                    break;
            }
        } else {
            if (defaultAssignment.getStringLiterals() == null || defaultAssignment.getStringLiterals().getIdentifier() == null || defaultAssignment.getStringLiterals().getIdentifier().getText().isEmpty()) {
                // 语法错误
                if (defaultAssignment.getStringLiterals() != null) {
                    annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Invalid default value, identifier expected")
                            .range(defaultAssignment.getStringLiterals())
                            .create();
                }
                return;
            }
            PsiReference reference = fieldType.getReference();
            if (reference != null) {
                PsiElement fieldTypeElement = reference.resolve();
                if (fieldTypeElement == null) {
                    // 类型引用解析失败，有其他annotator来标注
                    return;
                }
                if (fieldTypeElement instanceof JceEnumType) {
                    // 枚举类型的字段，默认值必须是枚举成员
                    if (defaultAssignment.getReference() == null || defaultAssignment.getReference().resolve() == null) {
                        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Unresolved enum member " + defaultAssignment.getStringLiterals().getIdentifier().getText() + " in enum " + ((JceEnumType) fieldTypeElement).getName())
                                .range(defaultAssignment.getStringLiterals())
                                .create();
                    } else {
                        // 成功解析，标注一下高亮颜色
                        annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                .range(defaultAssignment.getStringLiterals().getIdentifier())
                                .textAttributes(JceSyntaxHighlighter.FIELD_REFERENCE)
                                .create();
                    }
                } else {
                    // 其他类型的字段不能有默认值，也就是struct类型
                    annotationHolder.newAnnotation(HighlightSeverity.ERROR, "No default value allowed for struct")
                            .range(defaultAssignment.getStringLiterals())
                            .create();
                }
            }
        }
        // require类型的标签不建议有默认值
        if (fieldInfo.getFieldLabel() != null) {
            ASTNode requireToken = fieldInfo.getFieldLabel().getNode().findChildByType(JceTypes.REQUIRE);
            if (requireToken != null) {
                // 是require，但是有默认值
                annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Default value for require field is not recommended")
                        .range(defaultAssignment)
                        .withFix(new RemoveFieldDefaultValueQuickFix(fieldInfo))
                        .create();
            }
        }
    }
}
