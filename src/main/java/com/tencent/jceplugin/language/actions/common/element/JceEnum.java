package com.tencent.jceplugin.language.actions.common.element;

import java.util.List;

/**
 * jce枚举信息
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceEnum implements JceType {
    /**
     * 枚举名称
     */
    private final String name;
    /**
     * 字段列表
     */
    private final List<JceEnumMember> memberList;
    /**
     * 注释
     */
    private final String comment;

    public JceEnum(String name, List<JceEnumMember> memberList, String comment) {
        this.name = name;
        this.memberList = memberList;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public List<JceEnumMember> getMemberList() {
        return memberList;
    }

    public String getComment() {
        return comment;
    }
}
