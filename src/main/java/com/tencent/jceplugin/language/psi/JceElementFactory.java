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

package com.tencent.jceplugin.language.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JceElementFactory {
    public static JceModuleInfo createModule(Project project, String name) {
        final JceFile file = createFile(project, "module " + name + "{};");
        return PsiTreeUtil.findChildOfType(file, JceModuleInfo.class);
    }

    public static JceInterfaceInfo createInterface(Project project, String name) {
        final JceFile file = createFile(project, "module dummy {interface " + name + "{};};");
        return PsiTreeUtil.findChildOfType(file, JceInterfaceInfo.class);
    }

    public static JceStructType createStruct(Project project, String name) {
        final JceFile file = createFile(project, "module dummy {struct " + name + "{};};");

        return PsiTreeUtil.findChildOfType(file, JceStructType.class);
    }

    public static JceFieldInfo createField(Project project, @Nullable JceFieldInfo oldElement, String newName) {
        StringBuilder fileContentBuilder = new StringBuilder();
        fileContentBuilder.append("module dummy {");
        fileContentBuilder.append("struct dummyStruct {");
        if (oldElement != null) {
            // 0 optional int newName
            fileContentBuilder.append(String.format("%s %s %s %s",
                    oldElement.getFieldTag().getNumInt().getText(),
                    Optional.ofNullable(oldElement.getFieldLabel()).map(PsiElement::getText).orElse("optional"),
                    Optional.ofNullable(oldElement.getFieldType()).map(PsiElement::getText).orElse("int"),
                    newName));
            if (oldElement.getFieldDefaultAssignment() != null) {
                // 有默认值
                fileContentBuilder.append("=").append(oldElement.getFieldDefaultAssignment().getText());
            }
            // field end
            fileContentBuilder.append(";");
        }
        // struct end
        fileContentBuilder.append("};");
        // module end
        fileContentBuilder.append("};");
        return PsiTreeUtil.findChildOfType(createFile(project, fileContentBuilder.toString()).findChildByClass(JceModuleInfo.class), JceFieldInfo.class);
    }

    public static JceFieldInfo createField(Project project, int fieldTag, String fieldLabel, String fieldType, String fieldName, String defaultValue) {
        StringBuilder fileContentBuilder = new StringBuilder();
        fileContentBuilder.append("module dummy {");
        fileContentBuilder.append("struct dummyStruct {");
        // 0 optional int newName
        fileContentBuilder.append(String.format("%s %s %s %s",
                fieldTag,
                fieldLabel,
                fieldType,
                fieldName));
        if (defaultValue != null && !defaultValue.isEmpty()) {
            // 有默认值
            fileContentBuilder.append("=").append(defaultValue);
        }
        // field end
        fileContentBuilder.append(";");
        // struct end
        fileContentBuilder.append("};");
        // module end
        fileContentBuilder.append("};");
        return PsiTreeUtil.findChildOfType(createFile(project, fileContentBuilder.toString()).findChildByClass(JceModuleInfo.class), JceFieldInfo.class);
    }

    public static JceEnumType createEnum(Project project, @Nullable JceEnumType oldElement, String newName) {
        StringBuilder fileContentBuilder = new StringBuilder();
        fileContentBuilder.append("module dummy {");
        fileContentBuilder.append("enum ").append(newName).append(" {");
        if (oldElement != null) {
            // aaa=1, bbb=2, ccc, ddd
            oldElement.getEnumMemberList().forEach(enumMember -> {
                // aaa=1
                fileContentBuilder.append(enumMember.getIdentifier().getText());
                if (enumMember.getNumInt() != null) {
                    // 默认值
                    fileContentBuilder.append("=").append(enumMember.getNumInt().getText());
                }
                // enum member end
                fileContentBuilder.append(",");
            });
        }
        // enum end
        fileContentBuilder.append("};");
        // module end
        fileContentBuilder.append("};");

        return PsiTreeUtil.findChildOfType(createFile(project, fileContentBuilder.toString()), JceEnumType.class);
    }

    public static JceEnumMember createEnumMember(Project project, @Nullable JceEnumMember oldElement, String newName) {
        StringBuilder fileContentBuilder = new StringBuilder();
        fileContentBuilder.append("module dummy {");
        fileContentBuilder.append("enum dummyEnum {");
        if (oldElement != null) {
            // aaa=1
            fileContentBuilder.append(newName);
            if (oldElement.getNumInt() != null) {
                // 默认值
                fileContentBuilder.append("=").append(oldElement.getNumInt().getText());
            }
            // enum member end
            fileContentBuilder.append(",");
        }
        // enum end
        fileContentBuilder.append("};");
        // module end
        fileContentBuilder.append("};");

        return PsiTreeUtil.findChildOfType(createFile(project, fileContentBuilder.toString()), JceEnumMember.class);
    }

    public static JceConstType createConst(Project project, @Nullable JceConstType oldElement, String newName) {
        var fileContentBuilder = "module dummy {" +
                "const " +
                Optional.ofNullable(oldElement)
                        .map(JceConstType::getBuiltInTypes)
                        .map(PsiElement::getText)
                        .orElse("int") +
                " " +
                newName +
                ";" +
                // module end
                "};";

        return PsiTreeUtil.findChildOfType(createFile(project, fileContentBuilder), JceConstType.class);
    }

    public static JceFunctionInfo createFunction(Project project, @Nullable JceFunctionInfo oldElement, String newName) {
        StringBuilder fileContentBuilder = new StringBuilder();
        fileContentBuilder.append("module dummy {");
        fileContentBuilder.append("interface dummyInterface {");
        // int funcName(int a, int b, out int c);
        fileContentBuilder.append(String.format("%s %s(",
                // 返回值类型
                Optional.ofNullable(oldElement)
                        .map(JceFunctionInfo::getReturnType)
                        .map(PsiElement::getText)
                        .filter(StringUtil::isNotEmpty)
                        .orElse("void"),
                newName
        ));
        // 参数列表
        if (oldElement != null) {
            JceFunctionParamList functionParamList = oldElement.getFunctionParamList();
            if (functionParamList != null) {
                fileContentBuilder.append(functionParamList.getText());
            }
        }
        // function end
        fileContentBuilder.append(");");
        // interface end
        fileContentBuilder.append("};");
        // module end
        fileContentBuilder.append("};");
        return PsiTreeUtil.findChildOfType(createFile(project, fileContentBuilder.toString()), JceFunctionInfo.class);
    }

    public static JceFile createFile(Project project, String text) {
        String name = "dummy.jce";
        return (JceFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, JceFileType.INSTANCE, text);
    }

    public static JceFunctionParam createFunctionParam(Project project, JceFunctionParam oldElement, String newName) {
        var fileContentBuilder = "module dummy {" +
                "interface dummyInterface {" +
                // int funcName(int a, int b, out int c);
                "void dummyFunction(" +
                // 参数列表
                String.format("%s %s %s",
                        oldElement.getFieldTypeModifier() != null ? "out" : "",
                        oldElement.getFieldType().getText(),
                        newName
                ) +
                // function end
                ");" +
                // interface end
                "};" +
                // module end
                "};";
        return PsiTreeUtil.findChildOfType(createFile(project, fileContentBuilder), JceFunctionParam.class);
    }

    @NotNull
    public static JceIncludeInfo createIncludeFilename(Project project, String name) {
        final JceFile file = createFile(project, "#include \"" + name + "\"");
        return file.getIncludeList()[0];
    }

    public static JceKeyInfo createKeyInfoByString(Project project, String keyInfoCode) {
        var fileContentBuilder = "module dummy {" +
                // key [structName, field];
                keyInfoCode +
                // module end
                "};";
        return PsiTreeUtil.findChildOfType(createFile(project, fileContentBuilder), JceKeyInfo.class);
    }
}
