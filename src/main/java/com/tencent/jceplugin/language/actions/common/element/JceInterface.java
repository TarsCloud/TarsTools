package com.tencent.jceplugin.language.actions.common.element;

import java.util.List;

/**
 * 接口信息
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceInterface {
    /**
     * 接口名
     */
    private final String name;
    /**
     * 方法列表
     */
    private final List<JceFunction> functionList;
    /**
     * 注释
     */
    private final String comment;

    public JceInterface(String name, List<JceFunction> functionList, String comment) {
        this.name = name;
        this.functionList = functionList;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public List<JceFunction> getFunctionList() {
        return functionList;
    }

    public String getComment() {
        return comment;
    }
}
