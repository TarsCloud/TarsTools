package com.tencent.jceplugin.language.actions.common.element;

/**
 * 结构体字段信息
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceField {
    /**
     * 字段名称
     */
    private final String name;
    /**
     * 字段类型
     */
    private final JceType type;
    /**
     * 字段序号
     */
    private final int tag;
    /**
     * 注释
     */
    private final String comment;
    /**
     * 是否是必填字段
     */
    private final boolean required;

    public JceField(String name, JceType type, int tag, String comment, boolean required) {
        this.name = name;
        this.type = type;
        this.tag = tag;
        this.comment = comment;
        this.required = required;
    }

    public String getName() {
        return name;
    }


    public JceType getType() {
        return type;
    }

    public int getTag() {
        return tag;
    }

    public String getComment() {
        return comment;
    }

    public boolean isRequired() {
        return required;
    }
}
