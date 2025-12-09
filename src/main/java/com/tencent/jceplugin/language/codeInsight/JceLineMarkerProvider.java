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

package com.tencent.jceplugin.language.codeInsight;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.JceNavIcons;
import com.tencent.jceplugin.language.JceUtil;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * JCE 到 Java 的导航标记提供者
 * <p>
 * 支持：原始 TarsTools、TarsJava、tafjava2
 * <p>
 * 接口级别显示多个图标（按类型分组）：
 * <ul>
 *   <li>蓝色 S (Servant) - 服务端接口</li>
 *   <li>橙色 P (Proxy) - 代理类</li>
 *   <li>青色 C (Callback) - 回调类</li>
 *   <li>红色 I (Impl) - 实现类</li>
 * </ul>
 * <p>
 * 方法级别显示 3 个图标：
 * <ul>
 *   <li>橙色 P (Proxy) - 代理方法（包含同步、异步、Promise）</li>
 *   <li>青色 C (Callback) - 回调方法</li>
 *   <li>蓝色 S (Servant) - 服务端接口方法</li>
 * </ul>
 */
public class JceLineMarkerProvider extends RelatedItemLineMarkerProvider {

    /**
     * 类级别类型枚举（用于接口行的导航图标）
     * 顺序：P, C, I(Servant), I(Impl)
     */
    enum ClassType {
        PROXY("Proxy", JceNavIcons.PROXY, "Navigate to Proxy class"),
        CALLBACK("Callback", JceNavIcons.CALLBACK, "Navigate to Callback class"),
        SERVANT("Servant", AllIcons.Nodes.Interface, "Navigate to Servant interface"),
        IMPL("Impl", AllIcons.Gutter.ImplementedMethod, "Navigate to Implementation class");

        final String label;
        final Icon icon;
        final String tooltip;

        ClassType(String label, Icon icon, String tooltip) {
            this.label = label;
            this.icon = icon;
            this.tooltip = tooltip;
        }
    }

    /**
     * 方法级别类型枚举（用于方法行的导航图标）
     * 顺序：P, C, I(Servant), I(Impl)
     */
    enum MethodType {
        PROXY("Proxy", JceNavIcons.PROXY, "Navigate to Proxy methods"),
        CALLBACK("Callback", JceNavIcons.CALLBACK, "Navigate to Callback methods"),
        SERVANT("Servant", AllIcons.Nodes.Interface, "Navigate to Servant method"),
        IMPL("Impl", AllIcons.Gutter.ImplementedMethod, "Navigate to Implementation methods");

        final String label;
        final Icon icon;
        final String tooltip;

        MethodType(String label, Icon icon, String tooltip) {
            this.label = label;
            this.icon = icon;
            this.tooltip = tooltip;
        }
    }

    /**
     * Proxy 方法调用风格（用于弹出列表中的图标区分）
     */
    enum ProxyCallStyle {
        SYNC("Sync", JceNavIcons.PROXY_SYNC),         // S - 同步方法（橙色 S）
        ASYNC("Async", JceNavIcons.PROXY_ASYNC),      // A - 异步方法（紫色 A）
        PROMISE("Promise", JceNavIcons.PROXY_PROMISE); // P - Promise 方法（蓝色 P）

        final String label;
        final Icon icon;

        ProxyCallStyle(String label, Icon icon) {
            this.label = label;
            this.icon = icon;
        }
    }

    /**
     * 封装方法信息
     */
    static class MethodInfo {
        final PsiMethod method;
        final PsiClass containingClass;
        final MethodType type;
        final ProxyCallStyle callStyle; // Proxy 方法的调用风格

        MethodInfo(PsiMethod method, PsiClass containingClass, MethodType type) {
            this.method = method;
            this.containingClass = containingClass;
            this.type = type;
            this.callStyle = determineProxyCallStyle(method);
        }

        /**
         * 判断 Proxy 方法的调用风格
         */
        private static ProxyCallStyle determineProxyCallStyle(PsiMethod method) {
            String methodName = method.getName();
            if (methodName.startsWith("async_")) {
                return ProxyCallStyle.ASYNC;
            } else if (methodName.startsWith("promise_")) {
                return ProxyCallStyle.PROMISE;
            } else if (methodName.startsWith("async") && method.getReturnType() != null) {
                String returnType = method.getReturnType().getCanonicalText();
                if (returnType.contains("CompletableFuture")) {
                    return ProxyCallStyle.PROMISE;
                } else if (returnType.contains("CompletionStage")) {
                    return ProxyCallStyle.ASYNC;
                }
            }
            return ProxyCallStyle.SYNC;
        }

        /**
         * 获取用于弹出列表的图标
         */
        Icon getListIcon() {
            if (type == MethodType.PROXY) {
                return callStyle.icon;
            }
            return type.icon;
        }

        String getDisplayName() {
            StringBuilder sb = new StringBuilder();
            sb.append(containingClass.getName()).append(".").append(method.getName());
            sb.append(getSimplifiedParameters());
            return sb.toString();
        }

        String getSimplifiedParameters() {
            StringBuilder sb = new StringBuilder("(");
            PsiParameter[] parameters = method.getParameterList().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                String typeText = parameters[i].getType().getPresentableText();
                // 简化泛型，如 Holder<String> -> Holder
                int genericIndex = typeText.indexOf('<');
                if (genericIndex > 0) {
                    typeText = typeText.substring(0, genericIndex);
                }
                sb.append(typeText);
            }
            sb.append(")");
            return sb.toString();
        }

        PsiElement getTarget() {
            return method.getNameIdentifier();
        }
    }

    /**
     * 存储方法信息的映射，用于渲染器查找
     * 使用 WeakHashMap 避免内存泄漏
     */
    private static final Map<PsiElement, MethodInfo> methodInfoCache = new WeakHashMap<>();

    /**
     * 创建自定义渲染器
     */
    private static PsiElementListCellRenderer<PsiElement> createCellRenderer(MethodType type) {
        return new PsiElementListCellRenderer<>() {
            @Override
            public String getElementText(PsiElement element) {
                MethodInfo cachedInfo = methodInfoCache.get(element);
                if (cachedInfo != null) {
                    return cachedInfo.getDisplayName();
                }
                if (element.getParent() instanceof PsiMethod method) {
                    PsiClass containingClass = method.getContainingClass();
                    if (containingClass != null) {
                        MethodInfo info = new MethodInfo(method, containingClass, type);
                        return info.getDisplayName();
                    }
                }
                return element.getText();
            }

            @Override
            protected String getContainerText(PsiElement element, String name) {
                if (element.getParent() instanceof PsiMethod method) {
                    PsiClass containingClass = method.getContainingClass();
                    if (containingClass != null && containingClass.getQualifiedName() != null) {
                        return "(" + StringUtil.getPackageName(containingClass.getQualifiedName()) + ")";
                    }
                }
                return null;
            }

            @Override
            protected int getIconFlags() {
                return 0;
            }

            @Override
            protected @Nullable Icon getIcon(PsiElement element) {
                // 从缓存中获取方法信息，使用对应的图标
                MethodInfo cachedInfo = methodInfoCache.get(element);
                if (cachedInfo != null) {
                    return cachedInfo.getListIcon();
                }
                return type.icon;
            }
        };
    }

    /**
     * 判断方法的类型（用于方法行的导航图标）
     * 区分 4 种：Proxy（包含同步/异步/Promise）、Callback、Servant、Impl
     */
    private static MethodType determineMethodType(PsiMethod method, PsiClass containingClass) {
        String className = containingClass.getName();

        if (className == null) {
            return MethodType.PROXY;
        }

        // 回调类
        if (className.endsWith("PrxCallback") || className.endsWith("Callback")) {
            return MethodType.CALLBACK;
        }

        // 实现类
        if (className.endsWith("Impl")) {
            return MethodType.IMPL;
        }

        // Servant 接口
        if (className.endsWith("Servant")) {
            return MethodType.SERVANT;
        }

        // Proxy 类（包含同步、异步、Promise 方法）
        return MethodType.PROXY;
    }

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (!(element instanceof JceStructType || element instanceof JceInterfaceInfo || element instanceof JceFieldInfo)) {
            return;
        }

        // 处理结构体字段
        if (element instanceof JceFieldInfo jceFieldInfo) {
            collectFieldMarkers(jceFieldInfo, result);
            return;
        }

        String className = ((JceNamedElement) element).getName();
        if (className == null || className.isEmpty()) {
            return;
        }
        final PsiElement moduleInfo = PsiTreeUtil.findFirstParent(element, JceModuleInfo.class::isInstance);
        if (moduleInfo == null || ((JceModuleInfo) moduleInfo).getName() == null) {
            return;
        }
        @NotNull String moduleName = Objects.requireNonNull(((JceModuleInfo) moduleInfo).getName());

        if (element instanceof JceStructType jceStructType) {
            collectStructMarkers(jceStructType, className, moduleName, result);
        } else {
            collectInterfaceMarkers((JceInterfaceInfo) element, className, moduleName, result);
        }
    }

    /**
     * 收集结构体的导航标记
     */
    private void collectStructMarkers(JceStructType jceStructType, String className, String moduleName,
                                      Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        PsiClass[] javaClasses = PsiShortNamesCache.getInstance(jceStructType.getProject())
                .getClassesByName(className, GlobalSearchScope.allScope(jceStructType.getProject()));

        List<PsiElement> targets = new ArrayList<>();
        for (PsiClass javaClass : javaClasses) {
            if (javaClass.getQualifiedName() == null) {
                continue;
            }
            final String packageName = StringUtil.getShortName(StringUtil.getPackageName(javaClass.getQualifiedName()));
            if (!moduleName.equalsIgnoreCase(packageName)) {
                continue;
            }
            if (JceUtil.isJceStructClass(javaClass) && className.equals(javaClass.getName())) {
                if (javaClass.getNameIdentifier() != null) {
                    targets.add(javaClass.getNameIdentifier());
                }
            }
        }

        if (!targets.isEmpty()) {
            NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(AllIcons.Nodes.Class)
                            .setTargets(targets)
                            .setTooltipText("Navigate to Java struct class (" + targets.size() + ")");
            result.add(builder.createLineMarkerInfo(Objects.requireNonNull(jceStructType.getNameIdentifier())));
        }
    }

    /**
     * 收集结构体字段的导航标记
     */
    private void collectFieldMarkers(JceFieldInfo jceFieldInfo,
                                     Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        String fieldName = jceFieldInfo.getName();
        if (fieldName == null || fieldName.isEmpty()) {
            return;
        }

        // 获取父结构体
        JceStructType jceStructType = PsiTreeUtil.getParentOfType(jceFieldInfo, JceStructType.class);
        if (jceStructType == null) {
            return;
        }

        String structName = jceStructType.getName();
        if (structName == null || structName.isEmpty()) {
            return;
        }

        // 获取模块名
        final PsiElement moduleInfo = PsiTreeUtil.findFirstParent(jceFieldInfo, JceModuleInfo.class::isInstance);
        if (moduleInfo == null || ((JceModuleInfo) moduleInfo).getName() == null) {
            return;
        }
        @NotNull String moduleName = Objects.requireNonNull(((JceModuleInfo) moduleInfo).getName());

        // 搜索对应的 Java 结构体类
        PsiClass[] javaClasses = PsiShortNamesCache.getInstance(jceFieldInfo.getProject())
                .getClassesByName(structName, GlobalSearchScope.allScope(jceFieldInfo.getProject()));

        List<PsiElement> targets = new ArrayList<>();
        for (PsiClass javaClass : javaClasses) {
            if (javaClass.getQualifiedName() == null) {
                continue;
            }
            final String packageName = StringUtil.getShortName(StringUtil.getPackageName(javaClass.getQualifiedName()));
            if (!moduleName.equalsIgnoreCase(packageName)) {
                continue;
            }
            if (!JceUtil.isJceStructClass(javaClass) || !structName.equals(javaClass.getName())) {
                continue;
            }

            // 在 Java 类中查找对应的字段
            for (PsiField javaField : javaClass.getFields()) {
                // 检查字段是否是 JCE 结构体属性字段
                if (!JceUtil.isJceStructField(javaField)) {
                    continue;
                }
                // 检查字段名是否匹配
                if (fieldName.equals(javaField.getName())) {
                    if (javaField.getNameIdentifier() != null) {
                        targets.add(javaField.getNameIdentifier());
                    }
                }
            }
        }

        if (!targets.isEmpty() && jceFieldInfo.getNameIdentifier() != null) {
            NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(AllIcons.Nodes.Field)
                            .setTargets(targets)
                            .setTooltipText("Navigate to Java field (" + targets.size() + ")");
            result.add(builder.createLineMarkerInfo(jceFieldInfo.getNameIdentifier()));
        }
    }

    /**
     * 收集接口的导航标记
     */
    private void collectInterfaceMarkers(JceInterfaceInfo jceInterfaceInfo, String className, String moduleName,
                                         Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        // 搜索所有可能的 Java 类
        List<PsiClass> javaClasses = Stream.of(
                        className,
                        className + "Servant",
                        className + "Prx",
                        className + "ServantImpl",
                        className + "PrxImpl",
                        className + "Impl",
                        className + "ClientApi",
                        className + "PrxCallback",
                        className + "Callback"
                )
                .map(name -> PsiShortNamesCache.getInstance(jceInterfaceInfo.getProject())
                        .getClassesByName(name, GlobalSearchScope.allScope(jceInterfaceInfo.getProject())))
                .flatMap(Stream::of)
                .toList();

        // 收集接口级别的导航目标（按 ClassType 分组）
        Map<ClassType, List<PsiElement>> interfaceTargetsByType = new HashMap<>();

        // 收集方法级别的导航目标，key 是 JceFunctionInfo，value 按 MethodType 分组
        Map<JceFunctionInfo, Map<MethodType, List<MethodInfo>>> methodTargetsByType = new HashMap<>();

        for (PsiClass javaClass : javaClasses) {
            String name = javaClass.getQualifiedName();
            if (name == null) {
                continue;
            }
            String shortName = StringUtil.getShortName(name);

            // 检查是否需要匹配模块名
            boolean isPrxCallback = shortName.endsWith("PrxCallback") || shortName.endsWith("Callback");
            if (javaClass.isInterface() || javaClass.isEnum() || isPrxCallback
                    || shortName.endsWith("Servant") || shortName.endsWith("Prx")) {
                final String packageName = StringUtil.getShortName(StringUtil.getPackageName(name));
                if (!moduleName.equalsIgnoreCase(packageName)) {
                    continue;
                }
            }

            // 检查是否是 JCE 相关类
            boolean isJceClass = JceUtil.isJceClass(javaClass);
            if (!isJceClass && isPrxCallback) {
                isJceClass = true;
            }
            if (!isJceClass) {
                PsiReferenceList implementsList = javaClass.getImplementsList();
                if (implementsList != null) {
                    for (PsiJavaCodeReferenceElement referenceElement : implementsList.getReferenceElements()) {
                        PsiElement resolve = referenceElement.resolve();
                        if (resolve instanceof PsiClass resolvedClass && resolvedClass.getName() != null) {
                            isJceClass = JceUtil.isJceClass(resolvedClass);
                            if (isJceClass) {
                                shortName = resolvedClass.getName();
                                break;
                            }
                        }
                    }
                }
            }

            if (!isJceClass) {
                continue;
            }

            // 检查类名是否匹配
            String serviceName = JceUtil.getJceServiceNameFromClassName(shortName);
            if (!className.equals(serviceName)) {
                continue;
            }

            // 确定类的基础类型（用于接口级别导航）
            ClassType classType = determineClassType(javaClass);

            // 收集接口导航目标
            if (javaClass.getNameIdentifier() != null) {
                interfaceTargetsByType.computeIfAbsent(classType, k -> new ArrayList<>())
                        .add(javaClass.getNameIdentifier());
            }

            // 收集方法导航目标（包括 Impl 类的方法）
            for (PsiMethod javaMethod : javaClass.getMethods()) {
                String methodName = JceUtil.getFunctionName(javaMethod);
                if (methodName.isEmpty()) {
                    continue;
                }
                JceFunctionInfo jceFunctionInfo = JceUtil.findFunction(jceInterfaceInfo, methodName);
                if (jceFunctionInfo != null && javaMethod.getNameIdentifier() != null) {
                    MethodType type = determineMethodType(javaMethod, javaClass);
                    MethodInfo methodInfo = new MethodInfo(javaMethod, javaClass, type);
                    methodTargetsByType
                            .computeIfAbsent(jceFunctionInfo, k -> new HashMap<>())
                            .computeIfAbsent(type, k -> new ArrayList<>())
                            .add(methodInfo);
                }
            }
        }

        // 为接口创建导航标记（每种类型一个图标）
        for (Map.Entry<ClassType, List<PsiElement>> entry : interfaceTargetsByType.entrySet()) {
            ClassType type = entry.getKey();
            List<PsiElement> targets = entry.getValue();
            if (!targets.isEmpty()) {
                NavigationGutterIconBuilder<PsiElement> builder =
                        NavigationGutterIconBuilder.create(type.icon)
                                .setTargets(targets)
                                .setTooltipText(type.tooltip + " (" + targets.size() + ")");
                result.add(builder.createLineMarkerInfo(Objects.requireNonNull(jceInterfaceInfo.getNameIdentifier())));
            }
        }

        // 为每个方法创建导航标记（3 种类型：P、C、S）
        for (Map.Entry<JceFunctionInfo, Map<MethodType, List<MethodInfo>>> entry : methodTargetsByType.entrySet()) {
            JceFunctionInfo jceFunctionInfo = entry.getKey();
            Map<MethodType, List<MethodInfo>> typeMap = entry.getValue();

            for (Map.Entry<MethodType, List<MethodInfo>> typeEntry : typeMap.entrySet()) {
                MethodType type = typeEntry.getKey();
                List<MethodInfo> methodInfos = typeEntry.getValue();

                List<PsiElement> targets = new ArrayList<>();
                for (MethodInfo info : methodInfos) {
                    PsiElement target = info.getTarget();
                    if (target != null) {
                        targets.add(target);
                        // 将 MethodInfo 缓存起来，供渲染器使用
                        methodInfoCache.put(target, info);
                    }
                }

                if (!targets.isEmpty()) {
                    NavigationGutterIconBuilder<PsiElement> builder =
                            NavigationGutterIconBuilder.create(type.icon)
                                    .setTargets(targets)
                                    .setCellRenderer(() -> createCellRenderer(type))
                                    .setTooltipText(type.tooltip + " (" + targets.size() + ")");
                    result.add(builder.createLineMarkerInfo(Objects.requireNonNull(jceFunctionInfo.getNameIdentifier())));
                }
            }
        }
    }

    /**
     * 根据类名判断类的基础类型（用于接口级别导航图标）
     */
    private static ClassType determineClassType(PsiClass javaClass) {
        String className = javaClass.getName();
        if (className == null) {
            return ClassType.PROXY;
        }

        if (className.endsWith("PrxCallback") || className.endsWith("Callback")) {
            return ClassType.CALLBACK;
        }
        if (className.endsWith("Impl")) {
            return ClassType.IMPL;
        }
        if (className.endsWith("Servant")) {
            return ClassType.SERVANT;
        }
        // Prx 或其他带 @Servant 注解的类
        return ClassType.PROXY;
    }
}
