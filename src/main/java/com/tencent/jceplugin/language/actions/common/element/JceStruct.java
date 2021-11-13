package com.tencent.jceplugin.language.actions.common.element;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * jce结构体信息
 *
 * @author kongyuanyuan
 * @since 2021/11/7
 */
public class JceStruct implements JceType {
    /**
     * 结构体名称
     */
    private final String name;
    /**
     * 字段列表（为了避免循环引用（A->B->A），所以fieldList需要延迟设置）
     */
    private List<JceField> fieldList;
    /**
     * 注释
     */
    private final String comment;

    public JceStruct(String name, @NotNull List<JceField> fieldList, String comment) {
        this.name = name;
        this.fieldList = fieldList;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public List<JceField> getFieldList() {
        return fieldList;
    }

    public String getComment() {
        return comment;
    }

    public void setFieldList(List<JceField> fieldList) {
        this.fieldList = fieldList;
    }
}
