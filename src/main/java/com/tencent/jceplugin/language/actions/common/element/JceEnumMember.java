package com.tencent.jceplugin.language.actions.common.element;

/**
 * jce枚举成员信息
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceEnumMember {
    /**
     * 成员名称
     */
    private final String name;
    /**
     * 枚举序号
     */
    private final int ordinal;
    /**
     * 注释
     */
    private final String comment;

    public JceEnumMember(String name, int ordinal, String comment) {
        this.name = name;
        this.ordinal = ordinal;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public String getComment() {
        return comment;
    }
}
