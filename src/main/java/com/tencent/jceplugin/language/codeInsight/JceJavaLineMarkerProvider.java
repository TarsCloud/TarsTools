package com.tencent.jceplugin.language.codeInsight;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.*;
import icons.JceIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 标出Java接口
 */
public class JceJavaLineMarkerProvider extends RelatedItemLineMarkerProvider {
    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
        if (!(element instanceof PsiClass || element instanceof PsiMethodCallExpression || element instanceof PsiField || element instanceof PsiMethod)) {
            return;
        }
        @Nullable PsiIdentifier identifier;
        @Nullable PsiClass javaClassElement;
        @Nullable String methodName;
        if (element instanceof PsiClass) {
            javaClassElement = (PsiClass) element;
            identifier = javaClassElement.getNameIdentifier();
            if (identifier == null) {
                return;
            }
            List<JceInterfaceInfo> interfaceInfoList = resolveJceInterface(javaClassElement);
            if (!interfaceInfoList.isEmpty()) {
                for (JceInterfaceInfo jceInterfaceInfo : interfaceInfoList) {
                    //标注interface实现
                    markInterface(result, identifier, jceInterfaceInfo);
                    //标注方法定义
                    PsiMethod[] javaMethods = javaClassElement.getMethods();
                    for (PsiMethod javaMethod : javaMethods) {
                        methodName = getFunctionName(javaMethod);
                        if (methodName.isEmpty()) {
                            continue;
                        }
                        JceFunctionInfo jceFunction = JceUtil.findFunction(jceInterfaceInfo, methodName);
                        if (jceFunction != null) {
                            markFunction(result, javaMethod.getNameIdentifier(), jceFunction);
                        }
                    }
                }
            } else {
                //检查是不是jce module下的结构体/枚举
                String qualifiedName = javaClassElement.getQualifiedName();
                if (qualifiedName == null) {
                    return;
                }
                boolean isStruct = javaClassElement.hasAnnotation("com.qq.cloud.taf.common.annotation.JceStruct");
                String packageName = StringUtil.getPackageName(qualifiedName);
                String moduleName = StringUtil.getShortName(packageName);
                String className = StringUtil.getShortName(qualifiedName);
                List<JceModuleInfo> modules = JceUtil.findModules(element.getProject(), moduleName, true);
                for (JceModuleInfo module : modules) {
                    if (javaClassElement.isEnum()) {
                        //enum
                        List<JceEnumType> enumTypeList = JceUtil.findEnum(module, className);
                        for (JceEnumType jceEnumType : enumTypeList) {
                            if (className.equalsIgnoreCase(jceEnumType.getName())) {
                                //找到了enum
                                markEnum(result, javaClassElement, jceEnumType);
                            }
                        }
                    } else {
                        if (isStruct) {
                            //结构体
                            //struct
                            List<JceStructType> struct = JceUtil.findStruct(module, className);
                            for (JceStructType jceStructType : struct) {
                                if (className.equalsIgnoreCase(jceStructType.getName())) {
                                    //找到了struct
                                    markStruct(result, javaClassElement, jceStructType);
                                }
                            }
                        }
                    }
                }
            }

        } else if (element instanceof PsiMethodCallExpression) {
            //调用方法，检查是不是在调用taf服务的方法
            PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) element;
            PsiMethod psiMethod = methodCallExpression.resolveMethod();
            if (psiMethod != null) {
                if (methodCallExpression.getMethodExpression().getReferenceNameElement() instanceof PsiIdentifier) {
                    methodName = getFunctionName(psiMethod);
                    identifier = (PsiIdentifier) methodCallExpression.getMethodExpression().getReferenceNameElement();
                    javaClassElement = psiMethod.getContainingClass();
                    if (javaClassElement != null) {
                        List<JceInterfaceInfo> jceInterfaceInfoList = resolveJceInterface(javaClassElement);
                        for (JceInterfaceInfo interfaceInfo : jceInterfaceInfoList) {
                            JceFunctionInfo functionInfo = JceUtil.findFunction(interfaceInfo, methodName);
                            if (functionInfo != null) {
                                //标注在调用语句的方法名字处
                                markFunction(result, identifier, functionInfo);
                            }
                        }
                    }
                }
            }
        } else if (element instanceof PsiField) {
            // PsiField，检查是不是jce interface
            PsiType resolve = ((PsiField) element).getType();
            if (resolve instanceof PsiClassType) {
                javaClassElement = ((PsiClassType) resolve).resolve();
                identifier = ((PsiField) element).getNameIdentifier();
                if (javaClassElement != null) {
                    List<JceInterfaceInfo> jceInterfaceInfoList = resolveJceInterface(javaClassElement);
                    for (JceInterfaceInfo interfaceInfo : jceInterfaceInfoList) {
                        //标注在字段名上
                        markInterface(result, identifier, interfaceInfo);
                    }
                }
            }
        } else {
            //PsiMethod 方法定义，检查返回值是不是interface
            PsiMethod psiMethod = (PsiMethod) element;
            PsiIdentifier nameIdentifier = psiMethod.getNameIdentifier();
            if (nameIdentifier == null) {
                return;
            }
            PsiType returnType = psiMethod.getReturnType();
            if (returnType == null) {
                return;
            }
            if (returnType instanceof PsiClassType) {
                PsiClass javaClass = ((PsiClassType) returnType).resolve();
                if (javaClass == null) {
                    return;
                }
                List<JceInterfaceInfo> interfaceInfoList = resolveJceInterface(javaClass);
                if (!interfaceInfoList.isEmpty()) {
                    for (JceInterfaceInfo jceInterfaceInfo : interfaceInfoList) {
                        //标注interface实现
                        markInterface(result, nameIdentifier, jceInterfaceInfo);
                    }
                }
            }
        }
    }

    private static void markStruct(@NotNull Collection<? super RelatedItemLineMarkerInfo<PsiElement>> result, PsiClass javaClassElement, JceStructType jceStructType) {
        NavigationGutterIconBuilder<PsiElement> javaMethodBuilder =
                NavigationGutterIconBuilder.create(JceIcons.FILE)
                        .setTargets(jceStructType.getIdentifier())
                        .setTooltipText("Navigate to Jce Struct Declaration");
        result.add(javaMethodBuilder.createLineMarkerInfo(Objects.requireNonNull(javaClassElement.getNameIdentifier())));
    }

    private static void markEnum(@NotNull Collection<? super RelatedItemLineMarkerInfo<PsiElement>> result, PsiClass javaClassElement, JceEnumType jceEnumType) {
        NavigationGutterIconBuilder<PsiElement> javaEnumBuilder =
                NavigationGutterIconBuilder.create(JceIcons.FILE)
                        .setTargets(jceEnumType.getIdentifier())
                        .setTooltipText("Navigate to Jce Enum Declaration");
        result.add(javaEnumBuilder.createLineMarkerInfo(Objects.requireNonNull(javaClassElement.getNameIdentifier())));
    }

    private static void markFunction(@NotNull Collection<? super RelatedItemLineMarkerInfo<PsiElement>> result, PsiIdentifier nameIdentifier, JceFunctionInfo jceFunction) {
        NavigationGutterIconBuilder<PsiElement> javaMethodBuilder =
                NavigationGutterIconBuilder.create(JceIcons.FILE)
                        .setTargets(jceFunction.getNameIdentifier())
                        .setTooltipText("Navigate to Jce Function Declaration");
        result.add(javaMethodBuilder.createLineMarkerInfo(Objects.requireNonNull(nameIdentifier)));
    }

    private static void markInterface(@NotNull Collection<? super RelatedItemLineMarkerInfo<PsiElement>> result, PsiIdentifier identifier, JceInterfaceInfo jceInterfaceInfo) {
        NavigationGutterIconBuilder<PsiElement> javaClassBuilder =
                NavigationGutterIconBuilder.create(JceIcons.FILE)
                        .setTargets(jceInterfaceInfo.getNameIdentifier())
                        .setTooltipText("Navigate to Jce Interface Declaration");
        result.add(javaClassBuilder.createLineMarkerInfo(identifier));
    }

    @NotNull
    private static String getFunctionName(PsiMethod javaMethod) {
        String methodName;
        methodName = javaMethod.getName();
        if (methodName.startsWith("async_")) {
            methodName = methodName.substring("async_".length());
        }
        return methodName;
    }

    private static List<JceInterfaceInfo> resolveJceInterface(PsiClass javaClass) {
        boolean isJceClass = javaClass.hasAnnotation("com.qq.cloud.taf.common.annotation.JceService");
        String name = javaClass.getName();
        if (!isJceClass) {
            PsiReferenceList implementsList = javaClass.getImplementsList();
            if (implementsList != null && implementsList.getReferenceElements().length > 0) {
                //看看是不是实现类
                for (PsiJavaCodeReferenceElement referenceElement : implementsList.getReferenceElements()) {
                    PsiElement resolve = referenceElement.resolve();
                    if (resolve instanceof PsiClass) {
                        isJceClass = ((PsiClass) resolve).hasAnnotation("com.qq.cloud.taf.common.annotation.JceService");
                        if (isJceClass) {
                            name = ((PsiClass) resolve).getName();
                            break;
                        }
                        // if (isJceClass) {
                        //     //取实现的interface
                        //     interfaceClassElement = (PsiClass) resolve;
                        // }
                    }
                }
            }
        }
        if (isJceClass) {
            PsiIdentifier nameIdentifier = javaClass.getNameIdentifier();
            if (nameIdentifier != null && name != null && (name.endsWith("Servant") || name.endsWith("Prx"))) {
                //是Prx
                //查找interface
                String interfaceName = getInterfaceName(name);
                return JceUtil.findInterface(javaClass.getProject(), interfaceName);
            }
        }
        return new ArrayList<>();
    }

    @NotNull
    private static String getInterfaceName(String name) {
        String interfaceName;
        if (name.endsWith("Servant")) {
            interfaceName = name.substring(0, name.length() - "Servant".length());
        } else {
            interfaceName = name.substring(0, name.length() - "Prx".length());
        }
        return interfaceName;
    }
}
