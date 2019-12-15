package com.wuxian.janus.core.critical;

import com.wuxian.janus.core.StrictUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum NativePermissionTemplateEnum {

    /**
     * 创建权限模板
     */
    CREATE_PERMISSION_TEMPLATE(Code.CREATE_PERMISSION_TEMPLATE
            , CoverageTypeEnum.APPLICATION, PermissionLevelEnum.ADVANCED_READ_WRITE),

    /**
     * 初始化Tenant
     */
    INIT_TENANT(Code.INIT_TENANT
            , CoverageTypeEnum.APPLICATION, PermissionLevelEnum.ADVANCED_READ_WRITE),

    /**
     * 创建数据级权限
     */
    CREATE_MULTIPLE_PERMISSION(Code.CREATE_MULTIPLE_PERMISSION
            , CoverageTypeEnum.TENANT, PermissionLevelEnum.ADVANCED_ADD),

    /**
     * 创建外部用户组
     */
    CREATE_OUTER_GROUP(Code.CREATE_OUTER_GROUP
            , CoverageTypeEnum.TENANT, PermissionLevelEnum.ADVANCED_ADD),

    /**
     * 创建角色
     */
    CREATE_ROLE(Code.CREATE_ROLE
            , CoverageTypeEnum.TENANT, PermissionLevelEnum.ADVANCED_ADD),

    /**
     * 创建内部用户组
     */
    CREATE_INNER_GROUP(Code.CREATE_INNER_GROUP
            , CoverageTypeEnum.TENANT, PermissionLevelEnum.ADVANCED_ADD);

    public static class Code {

        public static final String APPLICATION_PERMISSION_PREFIX = Constant.JANUS + "_ap";
        public static final String TENANT_PERMISSION_PREFIX = Constant.JANUS + "_tp";

        static final String CREATE_PERMISSION_TEMPLATE = APPLICATION_PERMISSION_PREFIX + ":create_permission_template";
        static final String INIT_TENANT = APPLICATION_PERMISSION_PREFIX + ":init_tenant";

        static final String CREATE_MULTIPLE_PERMISSION = TENANT_PERMISSION_PREFIX + ":create_multiple_permission";
        static final String CREATE_OUTER_GROUP = TENANT_PERMISSION_PREFIX + ":create_outer_group";

        static final String CREATE_ROLE = TENANT_PERMISSION_PREFIX + ":create_role";
        static final String CREATE_INNER_GROUP = TENANT_PERMISSION_PREFIX + ":create_inner_group";
    }

    @Getter
    private String code;

    @Getter
    CoverageTypeEnum coverageType;

    @Getter
    private PermissionLevelEnum level;

    NativePermissionTemplateEnum(String code, CoverageTypeEnum coverageType, PermissionLevelEnum level) {
        this.code = code;
        this.coverageType = coverageType;
        this.level = level;
    }

    public String getPermissionType() {
        if (this.coverageType.equals(CoverageTypeEnum.APPLICATION)) {
            return Code.APPLICATION_PERMISSION_PREFIX;
        } else if (this.coverageType.equals(CoverageTypeEnum.TENANT)) {
            return Code.TENANT_PERMISSION_PREFIX;
        }
        return null;
    }

    public static List<String> getAllPermissionType() {
        return Arrays.asList(Code.APPLICATION_PERMISSION_PREFIX, Code.TENANT_PERMISSION_PREFIX);
    }

    public static NativePermissionTemplateEnum getByCode(String code) {
        for (NativePermissionTemplateEnum item : values()) {
            if (StrictUtils.equals(item.code, code)) {
                return item;
            }
        }
        return null;
    }
}
