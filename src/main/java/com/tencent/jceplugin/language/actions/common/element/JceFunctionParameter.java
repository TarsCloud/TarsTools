package com.tencent.jceplugin.language.actions.common.element;

/**
 * 方法参数信息
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceFunctionParameter {
    /**
     * 参数名称
     */
    private final String name;
    /**
     * 是否是out参数
     */
    private final boolean out;
    /**
     * 类型
     */
    private final JceType type;

    public JceFunctionParameter(String name, boolean out, JceType type) {
        this.name = name;
        this.out = out;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isOut() {
        return out;
    }

    public JceType getType() {
        return type;
    }
}
