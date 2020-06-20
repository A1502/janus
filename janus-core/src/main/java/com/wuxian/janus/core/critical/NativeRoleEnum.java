package com.wuxian.janus.core.critical;

import com.wuxian.janus.util.StrictUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum NativeRoleEnum {

    /**
     * 全部权限
     */
    ALL_PERMISSION(Code.ALL_PERMISSION, DimensionEnum.APPLICATION, true),

    /**
     * 应用维护者
     */
    APPLICATION_MAINTAINER(Code.APPLICATION_MAINTAINER, DimensionEnum.APPLICATION, false,
            NativePermissionTemplateEnum.CREATE_PERMISSION_TEMPLATE,
            NativePermissionTemplateEnum.INIT_TENANT,
            NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION,
            NativePermissionTemplateEnum.CREATE_OUTER_GROUP,
            NativePermissionTemplateEnum.CREATE_ROLE,
            NativePermissionTemplateEnum.CREATE_INNER_GROUP
    ),

    /**
     * tenant数据所有者
     */
    TENANT_DATA_OWNER(Code.TENANT_DATA_OWNER, DimensionEnum.TENANT, false,
            NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION,
            NativePermissionTemplateEnum.CREATE_OUTER_GROUP),

    /**
     * tenant维护者
     */
    TENANT_MAINTAINER(Code.TENANT_MAINTAINER, DimensionEnum.TENANT, false,
            NativePermissionTemplateEnum.CREATE_ROLE,
            NativePermissionTemplateEnum.CREATE_INNER_GROUP);

    public static class Code {
        public static final String APPLICATION_ROLE_PREFIX = Constant.JANUS + "_ar";
        public static final String TENANT_ROLE_PREFIX = Constant.JANUS + "_tr";

        static final String ALL_PERMISSION = APPLICATION_ROLE_PREFIX + ":all_permission";
        static final String APPLICATION_MAINTAINER = APPLICATION_ROLE_PREFIX + ":application_maintainer";
        static final String TENANT_DATA_OWNER = TENANT_ROLE_PREFIX + ":tenant_data_owner";
        static final String TENANT_MAINTAINER = TENANT_ROLE_PREFIX + ":tenant_maintainer";
    }

    @Getter
    private String code;

    @Getter
    DimensionEnum dimension;

    @Getter
    private Boolean hasAllPermission;

    private List<NativePermissionTemplateEnum> permissionTemplates;

    NativeRoleEnum(String code, DimensionEnum dimension, boolean hasAllPermission, NativePermissionTemplateEnum... templates) {
        this.code = code;
        this.dimension = dimension;
        this.hasAllPermission = hasAllPermission;
        this.permissionTemplates = Arrays.asList(templates);
    }

    public static NativeRoleEnum getByCode(String code) {
        for (NativeRoleEnum item : values()) {
            if (StrictUtils.equals(item.code, code)) {
                return item;
            }
        }
        return null;
    }
}
