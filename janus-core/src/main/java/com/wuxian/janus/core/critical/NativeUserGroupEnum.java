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
            ClassicLevelEnum.TWO,// visitAllPermissionRole,
            ClassicLevelEnum.TWO_POINT_FIVE,// visitApplicationMaintainerRole,
            ClassicLevelEnum.TWO_POINT_FIVE,// visitTenantDataOwnerRole,
            ClassicLevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            ClassicLevelEnum.HYPO_FULL,// visitTenantCustomRole,

            ClassicLevelEnum.TWO, // visitAGRootUserGroup,
            ClassicLevelEnum.TWO_POINT_FIVE, // visitApplicationMaintainerUserGroup,
            ClassicLevelEnum.TWO_POINT_FIVE, // visitTGRootUserGroup,
            ClassicLevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            ClassicLevelEnum.FOUR,// visitNativePermissionPackage,
            ClassicLevelEnum.FULL// visitCustomPermissionPackage
    )),

    /**
     * 应用级admin组
     */
    APPLICATION_ADMIN(Code.APPLICATION_MAINTAINER, DimensionEnum.APPLICATION, new NativeRuleTable(
            ClassicLevelEnum.TWO_POINT_FIVE,// visitAllPermissionRole,
            ClassicLevelEnum.THREE,// visitApplicationMaintainerRole,
            ClassicLevelEnum.TWO_POINT_FIVE,// visitTenantDataOwnerRole,
            ClassicLevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            ClassicLevelEnum.HYPO_FULL,// visitTenantCustomRole,

            ClassicLevelEnum.NONE, // visitAGRootUserGroup,
            ClassicLevelEnum.TWO, // visitApplicationMaintainerUserGroup,
            ClassicLevelEnum.TWO_POINT_FIVE, // visitTGRootUserGroup,
            ClassicLevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            ClassicLevelEnum.FOUR,// visitNativePermissionPackage,
            ClassicLevelEnum.FULL// visitCustomPermissionPackage,
    )),

    /**
     * tenant级root组
     */
    TENANT_ROOT(Code.TENANT_ROOT, DimensionEnum.TENANT, new NativeRuleTable(
            ClassicLevelEnum.NONE,// visitAllPermissionRole,
            ClassicLevelEnum.NONE,// visitApplicationMaintainerRole,
            ClassicLevelEnum.NONE,// visitTenantDataOwnerRole,
            ClassicLevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            ClassicLevelEnum.TWO_POINT_FIVE,// visitTenantCustomRole,

            ClassicLevelEnum.NONE, // visitAGRootUserGroup,
            ClassicLevelEnum.NONE, // visitApplicationMaintainerUserGroup,
            ClassicLevelEnum.THREE, // visitTGRootUserGroup,
            ClassicLevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            ClassicLevelEnum.NONE,// visitNativePermissionPackage,
            ClassicLevelEnum.NONE// visitCustomPermissionPackage,
    ));

    public static class Code {

        public static final String APPLICATION_USER_GROUP_PREFIX = Constant.JANUS + "_ag";
        public static final String TENANT_USER_GROUP_PREFIX = Constant.JANUS + "_tg";

        static final String APPLICATION_ROOT = APPLICATION_USER_GROUP_PREFIX + ":root";
        static final String APPLICATION_MAINTAINER = APPLICATION_USER_GROUP_PREFIX + ":maintainer";
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
