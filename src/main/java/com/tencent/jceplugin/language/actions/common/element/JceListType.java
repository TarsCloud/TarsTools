package com.tencent.jceplugin.language.actions.common.element;

/**
 * 列表类型
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceListType implements JceType {
    /**
     * 元素类型
     */
    private final JceType elementType;

    public JceListType(JceType elementType) {
        this.elementType = elementType;
    }

    public JceType getElementType() {
        return elementType;
    }
}
