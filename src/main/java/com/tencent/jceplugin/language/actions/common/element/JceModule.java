package com.tencent.jceplugin.language.actions.common.element;

import java.util.List;

/**
 * jce模块
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceModule {
    /**
     * 模块名称
     */
    private final String name;
    /**
     * 枚举列表
     */
    private final List<JceEnum> enumList;
    /**
     * 结构体列表
     */
    private final List<JceStruct> structList;

    /**
     * 接口列表
     */
    private final List<JceInterface> interfaceList;

    public JceModule(String name, List<JceEnum> enumList, List<JceStruct> structList, List<JceInterface> interfaceList) {
        this.name = name;
        this.enumList = enumList;
        this.structList = structList;
        this.interfaceList = interfaceList;
    }

    public String getName() {
        return name;
    }

    public List<JceEnum> getEnumList() {
        return enumList;
    }

    public List<JceStruct> getStructList() {
        return structList;
    }

    public List<JceInterface> getInterfaceList() {
        return interfaceList;
    }
}
