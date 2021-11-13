package com.tencent.jceplugin.language.actions.common.element;

/**
 * map类型
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceMapType implements JceType {
    /**
     * 元素类型
     */
    private final JceType keyType;
    /**
     * 值类型
     */
    private final JceType valueType;

    public JceMapType(JceType keyType, JceType valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public JceType getKeyType() {
        return keyType;
    }

    public JceType getValueType() {
        return valueType;
    }
}
