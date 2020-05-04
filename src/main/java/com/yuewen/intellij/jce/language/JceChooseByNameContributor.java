package com.yuewen.intellij.jce.language;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.yuewen.intellij.jce.language.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Goto by symbol
 */
public class JceChooseByNameContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        List<JceModuleInfo> moduleList = JceUtil.findModules(project);
        List<JceInterfaceInfo> interfaceList = JceUtil.findInterface(project);
        List<JceEnumMember> enumMemberList = JceUtil.findEnumMember(project);
        List<JceEnumType> enumTypeList = JceUtil.findEnum(project);
        List<JceConstType> constList = JceUtil.findConst(project);
        List<JceStructType> structList = JceUtil.findStruct(project);
        List<JceFunctionInfo> functionList = JceUtil.findFunction(project);
        List<String> nameList = new ArrayList<>();
        moduleList.stream().map(JceModuleInfo::getName).forEach(nameList::add);
        interfaceList.stream().map(JceInterfaceInfo::getName).forEach(nameList::add);
        enumMemberList.stream().map(JceEnumMember::getName).forEach(nameList::add);
        enumTypeList.stream().map(JceEnumType::getName).forEach(nameList::add);
        constList.stream().map(JceConstType::getName).forEach(nameList::add);
        structList.stream().map(JceStructType::getName).forEach(nameList::add);
        functionList.stream().map(JceFunctionInfo::getName).forEach(nameList::add);
        return nameList.toArray(new String[0]);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        List<JceModuleInfo> moduleList = JceUtil.findModules(project);
        List<JceInterfaceInfo> interfaceList = JceUtil.findInterface(project);
        List<JceEnumMember> enumMemberList = JceUtil.findEnumMember(project);
        List<JceEnumType> enumTypeList = JceUtil.findEnum(project);
        List<JceConstType> constList = JceUtil.findConst(project);
        List<JceStructType> structList = JceUtil.findStruct(project);
        List<JceFunctionInfo> functionList = JceUtil.findFunction(project);
        List<NavigationItem> navigationItemList = new ArrayList<>();
        navigationItemList.addAll(moduleList);
        navigationItemList.addAll(interfaceList);
        navigationItemList.addAll(enumMemberList);
        navigationItemList.addAll(enumTypeList);
        navigationItemList.addAll(constList);
        navigationItemList.addAll(structList);
        navigationItemList.addAll(functionList);
        return navigationItemList.toArray(new NavigationItem[0]);
    }
}
