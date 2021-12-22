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

package com.tencent.jceplugin.language;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.psi.JceConstType;
import com.tencent.jceplugin.language.psi.JceEnumMember;
import com.tencent.jceplugin.language.psi.JceEnumType;
import com.tencent.jceplugin.language.psi.JceFile;
import com.tencent.jceplugin.language.psi.JceFunctionInfo;
import com.tencent.jceplugin.language.psi.JceFunctionParam;
import com.tencent.jceplugin.language.psi.JceIncludeFilename;
import com.tencent.jceplugin.language.psi.JceIncludeInfo;
import com.tencent.jceplugin.language.psi.JceInterfaceInfo;
import com.tencent.jceplugin.language.psi.JceModuleInfo;
import com.tencent.jceplugin.language.psi.JceStructType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JceUtil {
    @NotNull
    public static List<JceModuleInfo> findModules(Project project, @Nullable String name, boolean ignoreCase) {
        List<JceModuleInfo> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                List<JceModuleInfo> fileResult = findModules(jceFile, name, false, ignoreCase);
                result.addAll(fileResult);
            }
        }
        return result;
    }

    @NotNull
    public static ArrayList<JceModuleInfo> findModules(PsiFile jceFile, @Nullable String name, boolean withInclude,
                                                       boolean ignoreCase) {
        ArrayList<JceModuleInfo> fileResult = new ArrayList<>();
        if (!(jceFile instanceof JceFile)) {
            return fileResult;
        }
        HashMap<String, JceFile> fileMap = new HashMap<>();
        fileMap.put("", (JceFile) jceFile);
        //找出引用的文件里所有的module
        if (withInclude) {
            findIncludeFiles((JceFile) jceFile, fileMap);
        }
        for (JceFile file : fileMap.values()) {
            JceModuleInfo[] jceModuleInfos = PsiTreeUtil.getChildrenOfType(file, JceModuleInfo.class);
            if (jceModuleInfos != null) {
                if (name == null) {
                    fileResult.addAll(Arrays.asList(jceModuleInfos));
                } else {
                    for (JceModuleInfo jceModuleInfo : jceModuleInfos) {
                        if (ignoreCase) {
                            if (name.equalsIgnoreCase(jceModuleInfo.getName())) {
                                fileResult.add(jceModuleInfo);
                            }
                        } else {
                            if (name.equals(jceModuleInfo.getName())) {
                                fileResult.add(jceModuleInfo);
                            }
                        }
                    }
                }
            }
        }
        return fileResult;
    }

    @NotNull
    public static ArrayList<JceModuleInfo> findModules(PsiFile jceFile) {
        return findModules(jceFile, null, true, false);
    }

    @NotNull
    public static ArrayList<JceModuleInfo> findModules(PsiFile jceFile, boolean withInclude) {
        return findModules(jceFile, null, withInclude, false);
    }

    @NotNull
    public static List<JceModuleInfo> findModules(Project project) {
        return findModules(project, null, false);
    }

    @NotNull
    public static List<JceStructType> findStruct(Project project, @Nullable String name) {
        List<JceStructType> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                List<JceStructType> fileResult = findStruct(jceFile, name);
                result.addAll(fileResult);
            }
        }
        return result;
    }

    @NotNull
    public static List<JceStructType> findStruct(PsiFile jceFile, @Nullable String name) {
        JceModuleInfo[] moduleInfos = PsiTreeUtil.getChildrenOfType(jceFile, JceModuleInfo.class);
        List<JceStructType> fileResult = new ArrayList<>();
        if (moduleInfos != null) {
            for (JceModuleInfo jceModuleInfo : moduleInfos) {
                fileResult.addAll(findStruct(jceModuleInfo, name));
            }
        }
        return fileResult;
    }

    @NotNull
    public static List<JceStructType> findStruct(Project project) {
        return findStruct(project, null);
    }

    @NotNull
    public static List<JceStructType> findStruct(PsiFile file) {
        return findStruct(file, null);
    }

    @NotNull
    public static List<JceEnumType> findEnum(Project project, String name) {
        List<JceEnumType> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                List<JceEnumType> fileResult = findEnum(jceFile, name);
                result.addAll(fileResult);
            }
        }
        return result;
    }

    @NotNull
    public static List<JceEnumType> findEnum(PsiFile jceFile, @Nullable String name) {
        List<JceEnumType> fileResult = new ArrayList<>();
        JceModuleInfo[] moduleInfos = PsiTreeUtil.getChildrenOfType(jceFile, JceModuleInfo.class);
        if (moduleInfos != null) {
            for (JceModuleInfo jceModuleInfo : moduleInfos) {
                fileResult.addAll(findEnum(jceModuleInfo, name));
            }
        }
        return fileResult;
    }

    @NotNull
    public static List<JceEnumType> findEnum(Project project) {
        return findEnum(project, null);
    }

    @NotNull
    public static List<JceEnumType> findEnum(PsiFile file) {
        return findEnum(file, null);
    }

    @NotNull
    public static List<JceConstType> findConst(Project project, @Nullable String name) {
        List<JceConstType> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                JceModuleInfo[] moduleInfos = PsiTreeUtil.getChildrenOfType(jceFile, JceModuleInfo.class);
                if (moduleInfos != null) {
                    for (JceModuleInfo jceModuleInfo : moduleInfos) {
                        JceConstType[] jceStructTypes = PsiTreeUtil.getChildrenOfType(jceModuleInfo, JceConstType.class);
                        if (jceStructTypes != null) {
                            if (name == null) {
                                result.addAll(Arrays.asList(jceStructTypes));
                            } else {
                                for (JceConstType jceStructType : jceStructTypes) {
                                    if (name.equals(jceStructType.getName())) {
                                        result.add(jceStructType);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @NotNull
    public static List<JceConstType> findConst(Project project) {
        List<JceConstType> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                JceModuleInfo[] moduleInfos = PsiTreeUtil.getChildrenOfType(jceFile, JceModuleInfo.class);
                if (moduleInfos != null) {
                    for (JceModuleInfo jceModuleInfo : moduleInfos) {
                        JceConstType[] jceStructTypes = PsiTreeUtil.getChildrenOfType(jceModuleInfo, JceConstType.class);
                        if (jceStructTypes != null) {
                            result.addAll(Arrays.asList(jceStructTypes));
                        }
                    }
                }
            }
        }
        return result;
    }

    @NotNull
    public static List<JceInterfaceInfo> findInterface(Project project, String name) {
        List<JceInterfaceInfo> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        try {
            for (VirtualFile virtualFile : virtualFiles) {
                JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
                if (jceFile != null) {
                    JceModuleInfo[] moduleInfos = PsiTreeUtil.getChildrenOfType(jceFile, JceModuleInfo.class);
                    if (moduleInfos != null) {
                        for (JceModuleInfo jceModuleInfo : moduleInfos) {
                            JceInterfaceInfo[] jceStructTypes = PsiTreeUtil.getChildrenOfType(jceModuleInfo, JceInterfaceInfo.class);
                            if (jceStructTypes != null) {
                                for (JceInterfaceInfo jceStructType : jceStructTypes) {
                                    if (name.equals(jceStructType.getName())) {
                                        result.add(jceStructType);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    @NotNull
    public static List<JceInterfaceInfo> findInterface(Project project) {
        List<JceInterfaceInfo> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                List<JceInterfaceInfo> fileResult = findInterface(jceFile);
                result.addAll(fileResult);
            }
        }
        return result;
    }

    @NotNull
    public static List<JceInterfaceInfo> findInterface(@NotNull JceFile jceFile) {
        List<JceInterfaceInfo> fileResult = new ArrayList<>();
        JceModuleInfo[] moduleInfos = PsiTreeUtil.getChildrenOfType(jceFile, JceModuleInfo.class);
        if (moduleInfos != null) {
            for (JceModuleInfo jceModuleInfo : moduleInfos) {
                JceInterfaceInfo[] jceStructTypes = PsiTreeUtil.getChildrenOfType(jceModuleInfo, JceInterfaceInfo.class);
                if (jceStructTypes != null) {
                    fileResult.addAll(Arrays.asList(jceStructTypes));
                }
            }
        }
        return fileResult;
    }

    @NotNull
    public static List<JceFunctionInfo> findFunction(Project project, String name) {
        List<JceFunctionInfo> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                JceModuleInfo[] moduleInfos = PsiTreeUtil.getChildrenOfType(jceFile, JceModuleInfo.class);
                if (moduleInfos != null) {
                    for (JceModuleInfo jceModuleInfo : moduleInfos) {
                        JceInterfaceInfo[] jceInterfaceList = PsiTreeUtil.getChildrenOfType(jceModuleInfo, JceInterfaceInfo.class);
                        if (jceInterfaceList != null) {
                            for (JceInterfaceInfo jceInterfaceInfo : jceInterfaceList) {
                                JceFunctionInfo[] jceFunctionInfos = PsiTreeUtil.getChildrenOfType(jceInterfaceInfo, JceFunctionInfo.class);
                                if (jceFunctionInfos != null) {
                                    for (JceFunctionInfo jceFunctionInfo : jceFunctionInfos) {
                                        if (name.equals(jceFunctionInfo.getName())) {
                                            result.add(jceFunctionInfo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @NotNull
    public static List<JceFunctionInfo> findFunction(Project project) {
        List<JceFunctionInfo> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                JceModuleInfo[] moduleInfos = PsiTreeUtil.getChildrenOfType(jceFile, JceModuleInfo.class);
                if (moduleInfos != null) {
                    for (JceModuleInfo jceModuleInfo : moduleInfos) {
                        JceInterfaceInfo[] jceInterfaceList = PsiTreeUtil.getChildrenOfType(jceModuleInfo, JceInterfaceInfo.class);
                        if (jceInterfaceList != null) {
                            for (JceInterfaceInfo jceInterfaceInfo : jceInterfaceList) {
                                JceFunctionInfo[] jceFunctionInfos = PsiTreeUtil.getChildrenOfType(jceInterfaceInfo, JceFunctionInfo.class);
                                if (jceFunctionInfos != null) {
                                    Collections.addAll(result, jceFunctionInfos);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @NotNull
    public static List<JceFunctionParam> findFunctionParam(Project project) {
        List<JceFunctionParam> result = new ArrayList<>();
        List<JceFunctionInfo> functionList = findFunction(project);
        for (JceFunctionInfo jceFunctionInfo : functionList) {
            JceFunctionParam[] functionParams = PsiTreeUtil.getChildrenOfType(jceFunctionInfo, JceFunctionParam.class);
            if (functionParams != null) {
                Collections.addAll(result, functionParams);
            }
        }
        return result;
    }

    @NotNull
    public static List<JceEnumMember> findEnumMember(Project project) {
        return findEnumMember(project, null);
    }

    @NotNull
    public static List<JceEnumMember> findEnumMember(Project project, @Nullable String name) {
        List<JceEnumMember> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(JceFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            JceFile jceFile = (JceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (jceFile != null) {
                List<JceEnumMember> fileResult = findEnumMember(jceFile, name);
                result.addAll(fileResult);
            }
        }
        return result;
    }

    @NotNull
    public static List<JceEnumMember> findEnumMember(PsiFile jceFile) {
        return findEnumMember(jceFile, null);
    }

    @NotNull
    public static List<JceEnumMember> findEnumMember(PsiFile jceFile, @Nullable String name) {
        List<JceEnumMember> fileResult = new ArrayList<>();
        findEnum(jceFile).forEach(jceEnum -> {
            List<JceEnumMember> jceEnumMemberList = jceEnum.getEnumMemberList();
            if (!jceEnumMemberList.isEmpty()) {
                if (name == null) {
                    fileResult.addAll(jceEnumMemberList);
                } else {
                    for (JceEnumMember jceEnumMember : jceEnumMemberList) {
                        if (name.equals(jceEnumMember.getName())) {
                            fileResult.add(jceEnumMember);
                        }
                    }
                }
            }
        });
        return fileResult;
    }

    @Nullable
    public static PsiFile findIncludeFile(@NotNull JceIncludeInfo includeFilename) {
        String path = includeFilename.getIncludeFileName();
        VirtualFile currDir = includeFilename.getContainingFile().getVirtualFile().getParent();
        if (currDir == null) {
            return null;
        }
        VirtualFile virFile = currDir.findFileByRelativePath(path);
        if (virFile == null) {
            return null;
        }
        return PsiManager.getInstance(includeFilename.getProject()).findFile(virFile);
    }

    public static List<JceStructType> findStruct(JceModuleInfo moduleInfo, String structName) {
        List<JceStructType> fileResult = new ArrayList<>();
        JceStructType[] jceStructTypes = PsiTreeUtil.getChildrenOfType(moduleInfo, JceStructType.class);
        if (jceStructTypes != null) {
            if (structName == null) {
                fileResult.addAll(Arrays.asList(jceStructTypes));
            } else {
                for (JceStructType jceStructType : jceStructTypes) {
                    if (structName.equals(jceStructType.getName())) {
                        fileResult.add(jceStructType);
                    }
                }
            }
        }
        return fileResult;
    }

    public static List<JceEnumType> findEnum(JceModuleInfo moduleInfo, String name) {
        List<JceEnumType> fileResult = new ArrayList<>();
        JceEnumType[] jceStructTypes = PsiTreeUtil.getChildrenOfType(moduleInfo, JceEnumType.class);
        if (jceStructTypes != null) {
            if (name == null) {
                fileResult.addAll(Arrays.asList(jceStructTypes));
            } else {
                for (JceEnumType jceStructType : jceStructTypes) {
                    if (name.equals(jceStructType.getName())) {
                        fileResult.add(jceStructType);
                    }
                }
            }
        }
        return fileResult;
    }

    public static void findIncludeFiles(JceFile current, Map<String, JceFile> all) {
        JceIncludeInfo[] includes = current.getIncludeList();
        for (JceIncludeInfo include : includes) {
            try {
                JceIncludeFilename includeFileNameElement = include.getIncludeFilename();
                if (includeFileNameElement == null) {
                    continue;
                }
                PsiReference filenameReference = includeFileNameElement.getReference();
                String includeFileName = include.getIncludeFileName();
                if (includeFileName.isEmpty() || filenameReference == null) {
                    continue;
                }
                PsiElement includedFile = filenameReference.resolve();
                if (!(includedFile instanceof JceFile)) {
                    continue;
                }
                JceFile includedJceFile = (JceFile) includedFile;
                String path = includedJceFile.getVirtualFile().getPath();
                if (!all.containsKey(path)) {
                    all.put(path, includedJceFile);
                    findIncludeFiles(includedJceFile, all);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public static JceFunctionInfo findFunction(@NotNull JceInterfaceInfo interfaceInfo, @NotNull String name) {
        for (JceFunctionInfo jceFunctionInfo : interfaceInfo.getFunctionInfoList()) {
            if (name.equals(jceFunctionInfo.getName())) {
                return jceFunctionInfo;
            }
        }
        return null;
    }

    @Nullable
    public static PsiElement getPsiElement(@NotNull AnActionEvent event) {
        PsiElement element = event.getData(CommonDataKeys.PSI_ELEMENT);
        if (element == null) {
            PsiFile jceFile = event.getData(CommonDataKeys.PSI_FILE);
            Caret caret = event.getData(CommonDataKeys.CARET);
            if (jceFile instanceof JceFile && caret != null) {
                element = jceFile.findElementAt(caret.getOffset());
            }
        }
        return element;
    }

    @Nullable
    public static JceFile getJceFile(@NotNull AnActionEvent e) {
        if (e.getData(CommonDataKeys.PSI_FILE) instanceof JceFile) {
            return (JceFile) e.getData(CommonDataKeys.PSI_FILE);
        }
        return null;
    }

    public static String convertToSnakeCase(String name) {
        if (name.length() <= 1) {
            return name.toLowerCase();
        }
        //USER_ID -> user_id
        //USER_ID -> user_id
        //UserName -> user_name
        //_user_Name -> user_name
        //_user__Name -> user_name
        //_user_Name___ -> user_name
        StringBuilder sb = new StringBuilder(name.length() * 2);
        char[] chars = name.toCharArray();
        int i = 0;
        boolean hasSlash = false;
        while (i < chars.length) {
            if (Character.isUpperCase(chars[i])) {
                //输出一个下划线
                if (sb.length() > 0 && !hasSlash) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(chars[i]));
                //如果下一个字符也是大写，就需要一同提取出来
                while (i + 1 < chars.length && Character.isUpperCase(chars[i + 1])) {
                    sb.append(Character.toLowerCase(chars[i + 1]));
                    i++;
                }
                hasSlash = false;
            } else if (chars[i] == '_' || chars[i] == ' ') {
                //遇到了下划线，需要合并多个下划线
                if (sb.length() > 0) {
                    //丢弃开头的下划线
                    sb.append('_');
                }
                hasSlash = true;
                while (i + 1 < chars.length && (chars[i + 1] == '_' || chars[i + 1] == ' ')) {
                    i++;
                }
            } else {
                sb.append(chars[i]);
                hasSlash = false;
            }
            i++;
        }
        if (sb.charAt(sb.length() - 1) == '_') {
            //干掉最后的下划线
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String capitalize(String name) {
        char[] chars = name.toCharArray();
        StringBuilder sb = new StringBuilder(name.length());
        boolean needSlash = false;
        for (char aChar : chars) {
            if (!Character.isAlphabetic(aChar) && !Character.isDigit(aChar)) {
                //如果遇到了非英文字母数字，就意味着在下次输出英文字母之前要先输出一个下划线
                needSlash = true;
            } else {
                if (needSlash || sb.length() == 0) {
                    sb.append(Character.toUpperCase(aChar));
                    needSlash = false;
                } else {
                    sb.append(aChar);
                }
            }
        }
        return sb.toString();
    }

    public static boolean isJceClass(@NotNull PsiClass javaClass) {
        return javaClass.hasAnnotation("com.qq.cloud.taf.common.annotation.JceService")
                || javaClass.hasAnnotation("com.tencent.trpc.core.rpc.anno.TRpcService");
    }

    @NotNull
    public static String getFunctionName(@NotNull PsiMethod javaMethod) {
        String methodName;
        methodName = javaMethod.getName();
        if (methodName.startsWith("async_")) {
            methodName = methodName.substring("async_".length());
        }
        if (methodName.startsWith("async")
                && javaMethod.getReturnType() != null
                && javaMethod.getReturnType().getCanonicalText().endsWith("CompletionStage")) {
            methodName = methodName.substring("async".length());
            methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
        }
        return methodName;
    }

    @NotNull
    public static String getJceServiceNameFromClassName(@NotNull String name) {
        if (name.endsWith("Impl")) {
            name = name.substring(0, name.length() - "Impl".length());
        }
        return getJceServiceNameFromInterfaceName(name);
    }

    @NotNull
    public static String getJceServiceNameFromInterfaceName(@NotNull String name) {
        if (isJceServiceInterfaceName(name)) {
            //是Prx
            //查找interface
            if (name.endsWith("Servant")) {
                name = name.substring(0, name.length() - "Servant".length());
            } else if (name.endsWith("Prx")) {
                name = name.substring(0, name.length() - "Prx".length());
            } else {
                name = name.substring(0, name.length() - "ClientApi".length());
            }
        }
        return name;
    }

    public static boolean isJceServiceInterfaceName(@NotNull String name) {
        return name.endsWith("Servant") || name.endsWith("Prx") || name.endsWith("ClientApi");
    }

    public static boolean isJceStructClass(@NotNull PsiClass javaClassElement) {
        return javaClassElement.hasAnnotation("com.qq.cloud.taf.common.annotation.JceStruct")
                || javaClassElement.hasAnnotation("com.tencent.jce.annotation.JceStruct");
    }
}
