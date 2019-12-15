package com.wuxian.janus.core.critical;


import lombok.Getter;

/**
 * 权限级别
 *
 * @author lijianun
 */
@SuppressWarnings("all")
public enum PermissionLevelEnum {

    READ("read"),
    EDIT("edit"),
    DELETE("delete"),
    ADD("add"),
    READ_WRITE("read_write"),

    ADVANCED_READ("advanced_read"),
    ADVANCED_EDIT("advanced_edit"),
    ADVANCED_DELETE("advanced_delete"),
    ADVANCED_ADD("advanced_add"),
    ADVANCED_READ_WRITE("advanced_read_write");

    // 字段类型
    @Getter
    private String code;

    private PermissionLevelEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
