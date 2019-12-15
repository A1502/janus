package com.wuxian.janus.core.critical;

import com.wuxian.janus.core.StrictUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuppressWarnings("all")
public enum NativeUserGroupEnum {

    /**
     * 应用级root组
     */
    APPLICATION_ROOT(Code.APPLICATION_ROOT, CoverageTypeEnum.APPLICATION, new AccessControlAbility(
            AccessControlLevelEnum.TWO,// visitAllPermissionRole,
            AccessControlLevelEnum.TWO_POINT_FIVE,// visitApplicationMaintainerRole,
            AccessControlLevelEnum.TWO_POINT_FIVE,// visitTenantDataOwnerRole,
            AccessControlLevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            AccessControlLevelEnum.HYPO_FULL,// visitTenantCustomRole,

            AccessControlLevelEnum.TWO, // visitAGRootUserGroup,
            AccessControlLevelEnum.TWO_POINT_FIVE, // visitApplicationMaintainerUserGroup,
            AccessControlLevelEnum.TWO_POINT_FIVE, // visitTGRootUserGroup,
            AccessControlLevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            AccessControlLevelEnum.FOUR,// visitNativePermissionPackage,
            AccessControlLevelEnum.FULL// visitCustomPermissionPackage
    )),

    /**
     * 应用级admin组
     */
    APPLICATION_ADMIN(Code.APPLICATION_MAINTAINER, CoverageTypeEnum.APPLICATION, new AccessControlAbility(
            AccessControlLevelEnum.TWO_POINT_FIVE,// visitAllPermissionRole,
            AccessControlLevelEnum.THREE,// visitApplicationMaintainerRole,
            AccessControlLevelEnum.TWO_POINT_FIVE,// visitTenantDataOwnerRole,
            AccessControlLevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            AccessControlLevelEnum.HYPO_FULL,// visitTenantCustomRole,

            AccessControlLevelEnum.NONE, // visitAGRootUserGroup,
            AccessControlLevelEnum.TWO, // visitApplicationMaintainerUserGroup,
            AccessControlLevelEnum.TWO_POINT_FIVE, // visitTGRootUserGroup,
            AccessControlLevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            AccessControlLevelEnum.FOUR,// visitNativePermissionPackage,
            AccessControlLevelEnum.FULL// visitCustomPermissionPackage,
    )),

    /**
     * tenant级root组
     */
    TENANT_ROOT(Code.TENANT_ROOT, CoverageTypeEnum.TENANT, new AccessControlAbility(
            AccessControlLevelEnum.NONE,// visitAllPermissionRole,
            AccessControlLevelEnum.NONE,// visitApplicationMaintainerRole,
            AccessControlLevelEnum.NONE,// visitTenantDataOwnerRole,
            AccessControlLevelEnum.TWO_POINT_FIVE,// visitTenantMaintainerRole,
            AccessControlLevelEnum.TWO_POINT_FIVE,// visitTenantCustomRole,

            AccessControlLevelEnum.NONE, // visitAGRootUserGroup,
            AccessControlLevelEnum.NONE, // visitApplicationMaintainerUserGroup,
            AccessControlLevelEnum.THREE, // visitTGRootUserGroup,
            AccessControlLevelEnum.HYPO_FULL,// visitTenantCustomUserGroup,

            AccessControlLevelEnum.NONE,// visitNativePermissionPackage,
            AccessControlLevelEnum.NONE// visitCustomPermissionPackage,
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
    CoverageTypeEnum coverageType;

    private AccessControlAbility accessControlAbility;

    NativeUserGroupEnum(String code, CoverageTypeEnum coverageType, AccessControlAbility accessControlAbility) {
        this.code = code;
        this.coverageType = coverageType;
        this.accessControlAbility = accessControlAbility;
    }

    public List<NativeRoleEnum> getMatchedNativeRoleEnum(AccessControlLevel level) {
        List<NativeRoleEnum> result = new ArrayList<>();
        if (this.accessControlAbility.getAllPermissionRoleAbility().match(level)) {
            result.add(NativeRoleEnum.ALL_PERMISSION);
        }
        if (this.accessControlAbility.getApplicationMaintainerRoleAbility().match(level)) {
            result.add(NativeRoleEnum.APPLICATION_MAINTAINER);
        }
        if (this.accessControlAbility.getTenantDataOwnerRoleAbility().match(level)) {
            result.add(NativeRoleEnum.TENANT_DATA_OWNER);
        }
        if (this.accessControlAbility.getTenantMaintainerRoleAbility().match(level)) {
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
