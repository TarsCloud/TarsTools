package com.tencent.jceplugin.language.actions.common.element;

/**
 * jce内建基础类型
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceBuiltInBaseType implements JceType {
    /**
     * 类型
     */
    private final String type;

    public JceBuiltInBaseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
