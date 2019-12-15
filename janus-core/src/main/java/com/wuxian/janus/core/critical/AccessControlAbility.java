package com.wuxian.janus.core.critical;

import lombok.Getter;

@Getter
@SuppressWarnings("all")
public class AccessControlAbility {
    /**
     * 对内置角色"AllPermission"的访问控制
     */
    private AccessControlLevelEnum allPermissionRoleAbility;

    /**
     * 对内置角色"ApplicationMaintainer"的访问控制
     */
    private AccessControlLevelEnum applicationMaintainerRoleAbility;

    /**
     * 对内置角色"TenantDataOwner"的访问控制
     */
    private AccessControlLevelEnum tenantDataOwnerRoleAbility;

    /**
     * 对内置角色"TenantMaintainer"的访问控制
     */
    private AccessControlLevelEnum tenantMaintainerRoleAbility;

    /**
     * 对所有Tenant级自定义角色的访问控制
     */
    private AccessControlLevelEnum tenantCustomRoleAbility;

    /**
     * 对内置用户组"ApplicationRoot"的访问控制
     */
    private AccessControlLevelEnum applicationRootUserGroupAbility;

    /**
     * 对内置用户组"ApplicationMaintainer"的访问控制
     */
    private AccessControlLevelEnum applicationMaintainerUserGroupAbility;

    /**
     * 对内置用户组"TenantRoot"的访问控制
     */
    private AccessControlLevelEnum tenantRootUserGroupAbility;

    /**
     * 对Tenant级所有自定义用户组的访问控制
     */
    private AccessControlLevelEnum tenantCustomUserGroupAbility;

    /**
     * 对内置权限打包的访问控制
     */
    private AccessControlLevelEnum nativePermissionPackageAbility;

    /**
     * 对Tenant级所有自定义权限打包的访问控制
     */
    private AccessControlLevelEnum customPermissionPackageAbility;

    /**
     * 这是特意不用lombok自动生成。为了避免自动生成的参数顺序不确定性的风险。
     */
    public AccessControlAbility(AccessControlLevelEnum allPermissionRoleAbility,
                        AccessControlLevelEnum applicationMaintainerRoleAbility,
                        AccessControlLevelEnum tenantDataOwnerRoleAbility,
                        AccessControlLevelEnum tenantMaintainerRoleAbility,
                        AccessControlLevelEnum tenantCustomRoleAbility,
                        AccessControlLevelEnum applicationRootUserGroupAbility,
                        AccessControlLevelEnum applicationMaintainerUserGroupAbility,
                        AccessControlLevelEnum tenantRootUserGroupAbility,
                        AccessControlLevelEnum tenantCustomUserGroupAbility,
                        AccessControlLevelEnum nativePermissionPackageAbility,
                        AccessControlLevelEnum customPermissionPackageAbility
    ) {
        this.allPermissionRoleAbility = allPermissionRoleAbility;
        this.applicationMaintainerRoleAbility = applicationMaintainerRoleAbility;
        this.tenantDataOwnerRoleAbility = tenantDataOwnerRoleAbility;
        this.tenantMaintainerRoleAbility = tenantMaintainerRoleAbility;
        this.tenantCustomRoleAbility = tenantCustomRoleAbility;
        this.applicationRootUserGroupAbility = applicationRootUserGroupAbility;
        this.applicationMaintainerUserGroupAbility = applicationMaintainerUserGroupAbility;
        this.tenantRootUserGroupAbility = tenantRootUserGroupAbility;
        this.tenantCustomUserGroupAbility = tenantCustomUserGroupAbility;
        this.nativePermissionPackageAbility = nativePermissionPackageAbility;
        this.customPermissionPackageAbility = customPermissionPackageAbility;
    }
}
