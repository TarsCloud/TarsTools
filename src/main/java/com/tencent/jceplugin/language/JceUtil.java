package com.tencent.jceplugin.language;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
}
