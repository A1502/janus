package com.wuxian.janus.core.critical;

import com.wuxian.janus.core.basis.StrictUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuppressWarnings("all")
public enum NativeUserGroupEnum {

    /**
     * 应用级root组
     */
    APPLICATION_ROOT(Code.APPLICATION_ROOT, DimensionEnum.APPLICATION, new NativeRuleTable(
            LevelEnum.TWO,// visitAllPermissionRole,
            LevelEnum.TWO_POINT_FIVE,// visitApplicationMaintainerRole,
            LevelEnum.TWO_POINT_FIVE,// visitTenantDataOwnerRole,
            LevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            LevelEnum.HYPO_FULL,// visitTenantCustomRole,

            LevelEnum.TWO, // visitAGRootUserGroup,
            LevelEnum.TWO_POINT_FIVE, // visitApplicationMaintainerUserGroup,
            LevelEnum.TWO_POINT_FIVE, // visitTGRootUserGroup,
            LevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            LevelEnum.FOUR,// visitNativePermissionPackage,
            LevelEnum.FULL// visitCustomPermissionPackage
    )),

    /**
     * 应用级admin组
     */
    APPLICATION_ADMIN(Code.APPLICATION_ADMIN, DimensionEnum.APPLICATION, new NativeRuleTable(
            LevelEnum.TWO_POINT_FIVE,// visitAllPermissionRole,
            LevelEnum.THREE,// visitApplicationMaintainerRole,
            LevelEnum.TWO_POINT_FIVE,// visitTenantDataOwnerRole,
            LevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            LevelEnum.HYPO_FULL,// visitTenantCustomRole,

            LevelEnum.NONE, // visitAGRootUserGroup,
            LevelEnum.TWO, // visitApplicationMaintainerUserGroup,
            LevelEnum.TWO_POINT_FIVE, // visitTGRootUserGroup,
            LevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            LevelEnum.FOUR,// visitNativePermissionPackage,
            LevelEnum.FULL// visitCustomPermissionPackage,
    )),

    /**
     * tenant级root组
     */
    TENANT_ROOT(Code.TENANT_ROOT, DimensionEnum.TENANT, new NativeRuleTable(
            LevelEnum.NONE,// visitAllPermissionRole,
            LevelEnum.NONE,// visitApplicationMaintainerRole,
            LevelEnum.NONE,// visitTenantDataOwnerRole,
            LevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            LevelEnum.TWO_POINT_FIVE,// visitTenantCustomRole,

            LevelEnum.NONE, // visitAGRootUserGroup,
            LevelEnum.NONE, // visitApplicationMaintainerUserGroup,
            LevelEnum.THREE, // visitTGRootUserGroup,
            LevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            LevelEnum.NONE,// visitNativePermissionPackage,
            LevelEnum.NONE// visitCustomPermissionPackage,
    ));

    public static class Code {

        public static final String APPLICATION_USER_GROUP_PREFIX = Constant.JANUS + "_ag";
        public static final String TENANT_USER_GROUP_PREFIX = Constant.JANUS + "_tg";

        static final String APPLICATION_ROOT = APPLICATION_USER_GROUP_PREFIX + ":root";
        static final String APPLICATION_ADMIN = APPLICATION_USER_GROUP_PREFIX + ":admin";
        static final String TENANT_ROOT = TENANT_USER_GROUP_PREFIX + ":root";
    }

    @Getter
    private String code;

    @Getter
    DimensionEnum dimensionTypeEnum;

    private NativeRuleTable nativeRuleTable;

    NativeUserGroupEnum(String code, DimensionEnum dimensionTypeEnum, NativeRuleTable accessControlRule) {
        this.code = code;
        this.dimensionTypeEnum = dimensionTypeEnum;
        this.nativeRuleTable = accessControlRule;
    }

    public List<NativeRoleEnum> getCompatibleNativeRoleEnum(AccessControlMode mode) {
        List<NativeRoleEnum> result = new ArrayList<>();
        if (this.nativeRuleTable.getAllPermissionRoleRule().compatibleWith(mode)) {
            result.add(NativeRoleEnum.ALL_PERMISSION);
        }
        if (this.nativeRuleTable.getApplicationMaintainerRoleRule().compatibleWith(mode)) {
            result.add(NativeRoleEnum.APPLICATION_MAINTAINER);
        }
        if (this.nativeRuleTable.getTenantDataOwnerRoleRule().compatibleWith(mode)) {
            result.add(NativeRoleEnum.TENANT_DATA_OWNER);
        }
        if (this.nativeRuleTable.getTenantMaintainerRoleRule().compatibleWith(mode)) {
            result.add(NativeRoleEnum.TENANT_MAINTAINER);
        }
        return result;
    }

    public static NativeUserGroupEnum getByCode(String code) {
        for (NativeUserGroupEnum item : values()) {
            if (StrictUtils.equals(item.code, code)) {
                return item;
            }
        }
        return null;
    }
}
