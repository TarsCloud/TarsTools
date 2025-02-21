/*
 * Tencent is pleased to support the open source community by making Tars available.
 *
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language.actions;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.actions.common.ModulePsiParser;
import com.tencent.jceplugin.language.actions.common.element.JceBuiltInBaseType;
import com.tencent.jceplugin.language.actions.common.element.JceEnum;
import com.tencent.jceplugin.language.actions.common.element.JceEnumMember;
import com.tencent.jceplugin.language.actions.common.element.JceField;
import com.tencent.jceplugin.language.actions.common.element.JceFunction;
import com.tencent.jceplugin.language.actions.common.element.JceFunctionParameter;
import com.tencent.jceplugin.language.actions.common.element.JceInterface;
import com.tencent.jceplugin.language.actions.common.element.JceListType;
import com.tencent.jceplugin.language.actions.common.element.JceMapType;
import com.tencent.jceplugin.language.actions.common.element.JceModule;
import com.tencent.jceplugin.language.actions.common.element.JceStruct;
import com.tencent.jceplugin.language.actions.common.element.JceType;
import com.tencent.jceplugin.language.actions.dialogs.Notification;
import com.tencent.jceplugin.language.psi.JceFile;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class GenerateProtobufFileAction extends AnAction implements DumbAware {

    private static final DateTimeFormatter PROTO_SUFFIX_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Using the event, evaluate the context, and enable or disable the action.
        JceFile file = JceUtil.getJceFile(event);
        event.getPresentation().setEnabledAndVisible(file != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        // Using the event, implement an action. For example, create and show a dialog.
        // Using the event, create and show a dialog
        if (event.getProject() == null) {
            return;
        }
        PsiElement element = JceUtil.getPsiElement(event);
        JceModuleInfo parent = (JceModuleInfo) PsiTreeUtil.findFirstParent(element, JceModuleInfo.class::isInstance);
        if (parent == null) {
            JceFile jceFile = JceUtil.getJceFile(event);
            assert jceFile != null;
            parent = ArrayUtil.getFirstElement(jceFile.getModuleList());
        }
        assert parent != null;
        JceModule moduleStructure = parseModuleStructure(parent);
        String protoContent = generateProto(moduleStructure);
        String fileName = parent.getContainingFile().getVirtualFile().getNameWithoutExtension() + "-" + PROTO_SUFFIX_FORMATTER.format(LocalDateTime.now()) + "-generated.proto";
        JceModuleInfo finalJceModule = parent;
        WriteCommandAction.runWriteCommandAction(event.getProject(), () -> {
            try {
                VirtualFile childData = finalJceModule.getContainingFile().getVirtualFile().getParent()
                        .createChildData(event.getProject(), fileName);
                childData.setCharset(StandardCharsets.UTF_8);
                childData.setBinaryContent(protoContent.getBytes(StandardCharsets.UTF_8));
                FileEditorManager.getInstance(event.getProject()).openFile(childData, true);
            } catch (IOException e) {
                Notification.showTip("Convert jce/tars to proto", String.format("Create file %s failed. Reason: %s", fileName, e.getMessage()), NotificationType.ERROR);
                return;
            }
            Notification.showTip("Convert jce/tars to proto", String.format("Create file %s successfully", fileName), NotificationType.INFORMATION);
        });
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    private static String generateProto(@NotNull JceModule moduleStructure) {
        String moduleName = moduleStructure.getName().toLowerCase();
        StringBuilder protoSb = new StringBuilder(String.format("syntax = \"proto3\";\n" +
                "\n" +
                "package com.your.packagename.%s;\n" +
                "\n" +
                "option java_package = \"com.your.packagename.%s\";\n" +
                "option java_multiple_files = true;\n" +
                "option java_outer_classname = \"%s\";\n" +
                "\n" +
                "option go_package = \"your git path/git/path\";%n", moduleName, moduleName, moduleStructure.getName()) +
                "\n" +
                "// import \"your/other/common.proto\";" +
                "\n");
        for (JceEnum jceEnum : moduleStructure.getEnumList()) {
            writeEnum(protoSb, jceEnum);
        }
        for (JceStruct jceStruct : moduleStructure.getStructList()) {
            writeStruct(protoSb, jceStruct);
        }
        List<JceStruct> functionParamList = buildFunctionParamStruct(moduleStructure.getInterfaceList());
        for (JceStruct jceStruct : functionParamList) {
            writeStruct(protoSb, jceStruct);
        }
        for (JceInterface jceInterface : moduleStructure.getInterfaceList()) {
            writeInterface(protoSb, jceInterface);
        }
        return protoSb.toString();
    }

    private static List<JceStruct> buildFunctionParamStruct(List<JceInterface> interfaceList) {
        List<JceStruct> structList = new ArrayList<>(interfaceList.size());
        for (JceInterface jceInterface : interfaceList) {
            for (JceFunction jceFunction : jceInterface.getFunctionList()) {
                structList.add(new JceStruct(JceUtil.capitalize(jceFunction.getName()) + "Request",
                        buildStructListByFunctionParamList(jceFunction.getParameterList()), jceFunction.getName() + " 请求参数"));
                structList.add(new JceStruct(JceUtil.capitalize(jceFunction.getName()) + "Response",
                        buildFieldListByReturnTypeAndOutParam(jceFunction.getReturnType(), jceFunction.getParameterList()), jceFunction.getName() + " 响应参数"));
            }
        }
        return structList;
    }

    private static List<JceField> buildStructListByFunctionParamList(List<JceFunctionParameter> parameterList) {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        return parameterList.stream()
                .filter(parameter -> !parameter.isOut())
                .map(parameter -> new JceField(parameter.getName(), parameter.getType(), atomicInteger.incrementAndGet(), "", true))
                .collect(Collectors.toList());
    }

    private static List<JceField> buildFieldListByReturnTypeAndOutParam(JceType returnType, List<JceFunctionParameter> parameterList) {
        List<JceField> responseFieldList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        if (!(returnType instanceof JceBuiltInBaseType) || !"void".equals(((JceBuiltInBaseType) returnType).getType())) {
            responseFieldList.add(new JceField("functionReturnResult", returnType, atomicInteger.incrementAndGet(), "方法返回值", true));
        }
        parameterList.stream()
                .filter(JceFunctionParameter::isOut)
                .map(parameter -> new JceField(parameter.getName(), parameter.getType(), atomicInteger.incrementAndGet(), "", true))
                .forEach(responseFieldList::add);
        return responseFieldList;
    }

    private static void writeInterface(StringBuilder protoSb, JceInterface jceInterface) {
        protoSb.append('\n');
        if (!jceInterface.getComment().isEmpty()) {
            for (String comment : jceInterface.getComment().split("\n")) {
                protoSb.append("//").append(comment).append('\n');
            }
        }
        protoSb.append("service ").append(jceInterface.getName()).append(" {").append('\n');
        for (JceFunction jceFunction : jceInterface.getFunctionList()) {
            protoSb.append('\n');
            if (!jceFunction.getComment().isEmpty()) {
                for (String comment : jceFunction.getComment().split("\n")) {
                    protoSb.append("    // ").append(comment).append('\n');
                }
            }
            protoSb.append("    ");
            String functionName = JceUtil.capitalize(jceFunction.getName());
            protoSb.append("rpc")
                    .append(' ')
                    .append(functionName)
                    .append('(').append(functionName).append("Request").append(')')
                    .append(" returns (").append(functionName).append("Response").append(')')
                    .append(';');
            protoSb.append("\n");
        }
        protoSb.append("}").append('\n');
    }

    private static void writeStruct(StringBuilder protoSb, JceStruct jceStruct) {
        protoSb.append('\n');
        if (!jceStruct.getComment().isEmpty()) {
            for (String comment : jceStruct.getComment().split("\n")) {
                protoSb.append("//").append(comment).append('\n');
            }
        }
        protoSb.append("message ").append(jceStruct.getName()).append(" {").append('\n');
        // 字段序号从1开始，初始值0
        AtomicInteger atomicInteger = new AtomicInteger();
        for (JceField jceField : jceStruct.getFieldList()) {
            protoSb.append("    ");
            protoSb.append(getProtoType(jceField.getType()))
                    .append(' ')
                    .append(JceUtil.convertToSnakeCase(jceField.getName()))
                    .append(" = ").append(atomicInteger.incrementAndGet())
                    .append(';');
            writeComment(protoSb, jceField.getComment());
            protoSb.append("\n");
        }
        protoSb.append("}").append('\n');
    }

    private static void writeComment(StringBuilder sb, String comment2) {
        if (!comment2.isEmpty()) {
            String[] split = comment2.split("\n");
            for (int i = 0, splitLength = split.length; i < splitLength; i++) {
                String comment = split[i];
                sb.append(" // ").append(comment);
                if (i != splitLength - 1) {
                    sb.append('\n').append("   ");
                }
            }
        }
    }

    private static void writeEnum(StringBuilder protoSb, JceEnum jceEnum) {
        protoSb.append('\n');
        if (!jceEnum.getComment().isEmpty()) {
            for (String comment : jceEnum.getComment().split("\n")) {
                protoSb.append("//").append(comment).append('\n');
            }
        }
        protoSb.append("enum ").append(jceEnum.getName()).append(" {").append('\n');
        // 枚举序号从0开始
        if (jceEnum.getMemberList().stream().mapToInt(JceEnumMember::getOrdinal).noneMatch(ordinal -> ordinal == 0)) {
            // 没有序号为0的枚举，要手动造一个默认的，否则pb会报错
            writeEnumMember(protoSb, new JceEnumMember(JceUtil.convertToSnakeCase(jceEnum.getName()).toUpperCase() + "_UNSPECIFIED", 0, "枚举未定义值"));
        }
        for (JceEnumMember jceEnumMember : jceEnum.getMemberList()) {
            writeEnumMember(protoSb, jceEnumMember);
        }
        protoSb.append("}").append('\n');
    }

    private static void writeEnumMember(StringBuilder protoSb, JceEnumMember jceEnumMember) {
        protoSb.append("    ").append(jceEnumMember.getName().toUpperCase()).append(" = ").append(jceEnumMember.getOrdinal()).append(';');
        writeComment(protoSb, jceEnumMember.getComment());
        protoSb.append("\n");
    }

    private static String getProtoType(JceType type) {
        if (type instanceof JceBuiltInBaseType) {
            String builtInType = ((JceBuiltInBaseType) type).getType();
            switch (builtInType) {
                case "string":
                    return "string";
                case "int":
                case "short":
                case "byte":
                case "unsigned byte":
                case "unsigned short":
                    return "int32";
                case "unsigned int":
                    return "uint32";
                case "long":
                    return "int64";
                case "boolean":
                    return "bool";
                default:
                    return builtInType;
            }
        }
        if (type instanceof JceEnum) {
            return ((JceEnum) type).getName();
        }
        if (type instanceof JceStruct) {
            return ((JceStruct) type).getName();
        }
        if (type instanceof JceMapType) {
            return "map<" + getProtoType(((JceMapType) type).getKeyType()) + ", " + getProtoType(((JceMapType) type).getValueType()) + ">";
        }
        if (type instanceof JceListType) {
            return "repeated " + getProtoType(((JceListType) type).getElementType());
        }
        throw new IllegalStateException("Unknown type" + type.getClass().getName());
    }

    private static JceModule parseModuleStructure(@NotNull JceModuleInfo moduleInfo) {
        ModulePsiParser modulePsiParser = new ModulePsiParser(moduleInfo);
        return modulePsiParser.parse();
    }
}
