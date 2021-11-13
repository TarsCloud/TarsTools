package com.tencent.jceplugin.language.actions.common.element;

import java.util.List;

/**
 * 方法信息
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceFunction {
    /**
     * 方法名
     */
    private final String name;
    /**
     * 参数列表
     */
    private final List<JceFunctionParameter> parameterList;
    /**
     * 返回类型
     */
    private final JceType returnType;
    /**
     * 注释
     */
    private final String comment;

    public JceFunction(String name, List<JceFunctionParameter> parameterList, JceType returnType, String comment) {
        this.name = name;
        this.parameterList = parameterList;
        this.returnType = returnType;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public List<JceFunctionParameter> getParameterList() {
        return parameterList;
    }

    public JceType getReturnType() {
        return returnType;
    }

    public String getComment() {
        return comment;
    }
}
