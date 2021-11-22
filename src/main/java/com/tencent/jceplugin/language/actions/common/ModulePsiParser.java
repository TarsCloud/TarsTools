package com.tencent.jceplugin.language.actions.common;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceUtil;
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
import com.tencent.jceplugin.language.psi.JceElementType;
import com.tencent.jceplugin.language.psi.JceEnumType;
import com.tencent.jceplugin.language.psi.JceFieldInfo;
import com.tencent.jceplugin.language.psi.JceFieldType;
import com.tencent.jceplugin.language.psi.JceFunctionInfo;
import com.tencent.jceplugin.language.psi.JceFunctionParamList;
import com.tencent.jceplugin.language.psi.JceInterfaceInfo;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import com.tencent.jceplugin.language.psi.JceNamedElement;
import com.tencent.jceplugin.language.psi.JceStructType;
import com.tencent.jceplugin.language.psi.JceTypes;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 模块psi解析器
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class ModulePsiParser {
    /**
     * 模块名称
     */
    private final String moduleName;
    /**
     * 该模块在每个文件中的psi结构（包括通过include引用的其他文件里的同名模块）
     */
    private final List<JceModuleInfo> moduleList;
    /**
     * 缓存每个类型引用，防止出现循环引用导致同一个类型Psi被创建了两个jceType对象
     */
    private final Map<PsiElement, JceType> psiTypeMap = new HashMap<>();
    /**
     * 模块里的枚举列表，保证顺序
     */
    private final Map<String, JceEnum> enumList = new LinkedHashMap<>();
    /**
     * 模块里的结构体列表，保证顺序
     */
    private final Map<String, JceStruct> structList = new LinkedHashMap<>();
    /**
     * 模块里的接口列表，保证顺序
     */
    private final Map<String, JceInterface> interfaceList = new LinkedHashMap<>();

    public ModulePsiParser(@NotNull JceModuleInfo moduleInfo) {
        this.moduleName = moduleInfo.getName();
        moduleList = JceUtil.findModules(moduleInfo.getContainingFile(), moduleName, true, false);
    }

    public JceModule parse() {
        for (JceModuleInfo moduleInfo : moduleList) {
            parseEnumList(moduleInfo.getEnumTypeList());
            parseStructList(moduleInfo.getStructTypeList());
            parseInterfaceList(moduleInfo.getInterfaceInfoList());
        }
        return new JceModule(moduleName, new ArrayList<>(enumList.values()), new ArrayList<>(structList.values()), new ArrayList<>(interfaceList.values()));
    }

    private void parseInterfaceList(List<JceInterfaceInfo> jceInterfaceInfoList) {
        for (JceInterfaceInfo jceInterfaceInfo : jceInterfaceInfoList) {
            parseInterface(jceInterfaceInfo);
        }
    }

    private void parseInterface(JceInterfaceInfo jceInterfaceInfo) {
        interfaceList.computeIfAbsent(jceInterfaceInfo.getName(), interfaceName ->
                new JceInterface(interfaceName, parseFunctionList(jceInterfaceInfo.getFunctionInfoList()), getCommentText(getCommentElementWithBlockBackward(jceInterfaceInfo))));
    }

    private List<JceFunction> parseFunctionList(List<JceFunctionInfo> functionInfoList) {
        return functionInfoList.stream()
                .map(jceFunctionInfo ->
                        new JceFunction(jceFunctionInfo.getName(), parseParameterList(jceFunctionInfo.getFunctionParamList()),
                                jceFunctionInfo.getReturnType().getFieldType() == null ? new JceBuiltInBaseType("void")
                                        : parseFieldType(jceFunctionInfo.getReturnType().getFieldType()), getCommentText(getCommentElementWithBlockBackward(jceFunctionInfo))))
                .collect(Collectors.toList());
    }

    private List<JceFunctionParameter> parseParameterList(JceFunctionParamList functionParamList) {
        if (functionParamList == null) {
            return Collections.emptyList();
        }
        return functionParamList.getFunctionParamList().stream()
                .map(jceFunctionParam -> new JceFunctionParameter(jceFunctionParam.getName(),
                        jceFunctionParam.getFieldTypeModifier() != null,
                        parseFieldType(jceFunctionParam.getFieldType())))
                .collect(Collectors.toList());
    }

    private void parseEnumList(List<JceEnumType> enumTypeList) {
        for (JceEnumType enumType : enumTypeList) {
            parseEnum(enumType);
        }
    }

    private JceEnum parseEnum(JceEnumType enumType) {
        String name = getName(enumType);
        return enumList.computeIfAbsent(name, enumName -> new JceEnum(enumType.getName(),
                parseEnumMemberList(enumType.getEnumMemberList()), getCommentText(getCommentElementWithBlockBackward(enumType))));
    }

    private List<JceEnumMember> parseEnumMemberList(List<com.tencent.jceplugin.language.psi.JceEnumMember> enumMemberList) {
        List<JceEnumMember> list = new ArrayList<>(enumMemberList.size());
        int lastOrdinal = -1;
        for (com.tencent.jceplugin.language.psi.JceEnumMember jceEnumMember : enumMemberList) {
            lastOrdinal = jceEnumMember.getNumInt() != null && !jceEnumMember.getNumInt().getText().isEmpty()
                    ? NumberUtils.toInt(jceEnumMember.getNumInt().getText()) : lastOrdinal + 1;
            list.add(new JceEnumMember(jceEnumMember.getName(), lastOrdinal,
                    getCommentText(getCommentElementWithBlockForward(jceEnumMember.getNextSibling()))));
        }
        return list;
    }

    private void parseStructList(List<JceStructType> structTypeList) {
        for (JceStructType jceStructType : structTypeList) {
            parseStruct(jceStructType);
        }
    }

    private JceStruct parseStruct(JceStructType jceStructType) {
        String name = getName(jceStructType);
        JceStruct jceStruct = structList.get(name);
        if (jceStruct != null) {
            return jceStruct;
        }
        //为了在parseStruct中嵌套进行parseStruct（比如循环引用（A->B->A），或者结构体的字段的类型引用的结构体在当前结构体后面定义的（比如A引用B，但A在B前面定义））
        // 这里结构体对象需要提前暴露，结构体中的字段列表需要后面再解析
        jceStruct = new JceStruct(name, Collections.emptyList(), getCommentText(getCommentElementWithBlockBackward(jceStructType)));
        structList.put(name, jceStruct);
        jceStruct.setFieldList(parseStructFieldList(jceStructType.getFieldInfoList()));
        return jceStruct;
    }

    @Nullable
    private static PsiElement getCommentElementWithBlockBackward(PsiElement psiElement) {
        return getCommentElement(PsiTreeUtil.skipWhitespacesBackward(psiElement));
    }

    @NotNull
    private static PsiElement[] getCommentElementWithBlockForward(PsiElement psiElement) {
        List<PsiElement> commentElementList = new ArrayList<>();
        PsiElement lastCommentElement = getCommentElement(PsiTreeUtil.skipWhitespacesForward(psiElement));
        while (lastCommentElement != null) {
            commentElementList.add(lastCommentElement);
            lastCommentElement = getCommentElement(PsiTreeUtil.skipWhitespacesForward(lastCommentElement));
        }
        return commentElementList.toArray(new PsiElement[0]);
    }

    private static PsiElement getCommentElement(PsiElement prevElement) {
        if (prevElement == null) {
            return null;
        }
        return prevElement.getNode().getElementType() == JceTypes.LINE_COMMENT
                || prevElement.getNode().getElementType() == JceTypes.BLOCK_COMMENT
                || prevElement.getNode().getElementType() == JceElementType.DOC_COMMENT
                ? prevElement : null;
    }

    private static String getCommentText(@NotNull PsiElement[] elements) {
        StringBuilder sb = new StringBuilder();
        for (PsiElement element : elements) {
            String commentText = getCommentText(element);
            if (StringUtils.isNotBlank(commentText)) {
                sb.append(commentText).append('\n');
            }
        }
        return sb.toString().trim();
    }

    private static String getCommentText(PsiElement element) {
        if (element == null) {
            return "";
        }
        if (element.getNode().getElementType() == JceTypes.LINE_COMMENT) {
            //干掉开头的//
            int length = element.getText().length();
            int lastIndex;
            for (lastIndex = 0; lastIndex < length; lastIndex++) {
                if (element.getText().charAt(lastIndex) != '/') {
                    break;
                }
            }
            return element.getText().substring(lastIndex).trim();
        } else {
            //块注释
            //用状态机干掉 /* * */
            String comment = element.getText();
            int state = 0;
            StringBuilder sb = new StringBuilder(comment.length());
            //直接跳过开头的/*两个字符
            for (int i = 2; i < comment.length(); i++) {
                char c = comment.charAt(i);
                if (state == 0) {
                    //初始状态，要跳过遇到的第一个*或换行或空白符或/
                    for (; i < comment.length(); i++) {
                        c = comment.charAt(i);
                        if (c != '*' && c != ' ' && c != '\t' && c != '\n' && c != '/') {
                            break;
                        }
                    }
                    if (i >= comment.length()) {
                        //没了，不可能走到这
                        break;
                    }
                    sb.append(c);
                    state = 1;
                } else {
                    //正文状态，要吃进每个字符直到遇到换行为止
                    for (; i < comment.length(); i++) {
                        c = comment.charAt(i);
                        if (c != '\n') {
                            sb.append(c);
                        } else {
                            //遇到换行了，回到初始状态
                            state = 0;
                            sb.append('\n');
                            break;
                        }
                    }
                }
            }
            String finalComment = sb.toString();
            if (finalComment.endsWith("*/")) {
                finalComment = finalComment.substring(0, finalComment.length() - 2);
            }
            return finalComment.trim();
        }
    }

    @Nullable
    private String getName(JceNamedElement enumType) {
        String name = enumType.getName();
        JceModuleInfo jceModule = (JceModuleInfo) PsiTreeUtil.findFirstParent(enumType, JceModuleInfo.class::isInstance);
        if (jceModule != null && !moduleName.equals(jceModule.getName())) {
            //其他模块的结构体，变成模块名+结构体名
            assert name != null;
            char[] charArr = name.toCharArray();
            charArr[0] = Character.toUpperCase(charArr[0]);
            name = new String(charArr);
            name = jceModule.getName() + name;
        }
        return name;
    }

    private List<JceField> parseStructFieldList(List<JceFieldInfo> fieldInfoList) {
        return fieldInfoList.stream()
                .map(psiFieldInfo ->
                        new JceField(psiFieldInfo.getName(),
                                parseFieldType(Objects.requireNonNull(psiFieldInfo.getFieldType())),
                                NumberUtils.toInt(psiFieldInfo.getFieldTag().getNumInt().getText()),
                                getCommentText(getCommentElementWithBlockForward(psiFieldInfo)),
                                psiFieldInfo.getFieldLabel() != null && psiFieldInfo.getFieldLabel().isRequired()))
                .collect(Collectors.toList());
    }

    private JceType parseFieldType(JceFieldType fieldType) {
        if (fieldType.getMapType() != null) {
            return new JceMapType(parseFieldType(fieldType.getMapType().getFieldTypeList().get(0)), parseFieldType(fieldType.getMapType().getFieldTypeList().get(1)));
        } else if (fieldType.getVectorType() != null) {
            return new JceListType(parseFieldType(Objects.requireNonNull(fieldType.getVectorType().getFieldType())));
        } else if (fieldType.getBuiltInTypes() != null) {
            return new JceBuiltInBaseType(fieldType.getBuiltInTypes().getText().toLowerCase());
        } else {
            assert fieldType.getReference() != null;
            PsiElement psiElement = fieldType.getReference().resolve();
            JceType jceType = psiTypeMap.get(psiElement);
            if (jceType != null) {
                return jceType;
            }
            if (psiElement instanceof JceStructType) {
                jceType = parseStruct((JceStructType) psiElement);
            } else {
                assert psiElement instanceof JceEnumType;
                jceType = parseEnum((JceEnumType) psiElement);
            }
            psiTypeMap.put(psiElement, jceType);
            return jceType;
        }
    }
}
