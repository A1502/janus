package com.wuxian.janus.core.critical;

import lombok.Getter;

@Getter
@SuppressWarnings("all")
public class NativeRuleTable {
    /**
     * 对内置角色"AllPermission"的访问控制
     */
    private LevelEnum allPermissionRoleRule;

    /**
     * 对内置角色"ApplicationMaintainer"的访问控制
     */
    private LevelEnum applicationMaintainerRoleRule;

    /**
     * 对内置角色"TenantDataOwner"的访问控制
     */
    private LevelEnum tenantDataOwnerRoleRule;

    /**
     * 对内置角色"TenantMaintainer"的访问控制
     */
    private LevelEnum tenantMaintainerRoleRule;

    /**
     * 对所有Tenant级自定义角色的访问控制
     */
    private LevelEnum tenantCustomRoleRule;

    /**
     * 对内置用户组"ApplicationRoot"的访问控制
     */
    private LevelEnum applicationRootUserGroupRule;

    /**
     * 对内置用户组"ApplicationMaintainer"的访问控制
     */
    private LevelEnum applicationMaintainerUserGroupRule;

    /**
     * 对内置用户组"TenantRoot"的访问控制
     */
    private LevelEnum tenantRootUserGroupRule;

    /**
     * 对Tenant级所有自定义用户组的访问控制
     */
    private LevelEnum tenantCustomUserGroupRule;

    /**
     * 对内置权限打包的访问控制
     */
    private LevelEnum nativePermissionPackageRule;

    /**
     * 对Tenant级所有自定义权限打包的访问控制
     */
    private LevelEnum customPermissionPackageRule;

    /**
     * 这是特意不用lombok自动生成。为了避免自动生成的参数顺序不确定性的风险。
     */
    public NativeRuleTable(LevelEnum allPermissionRoleRule,
                           LevelEnum applicationMaintainerRoleRule,
                           LevelEnum tenantDataOwnerRoleRule,
                           LevelEnum tenantMaintainerRoleRule,
                           LevelEnum tenantCustomRoleRule,
                           LevelEnum applicationRootUserGroupRule,
                           LevelEnum applicationMaintainerUserGroupRule,
                           LevelEnum tenantRootUserGroupRule,
                           LevelEnum tenantCustomUserGroupRule,
                           LevelEnum nativePermissionPackageRule,
                           LevelEnum customPermissionPackageRule
    ) {
        this.allPermissionRoleRule = allPermissionRoleRule;
        this.applicationMaintainerRoleRule = applicationMaintainerRoleRule;
        this.tenantDataOwnerRoleRule = tenantDataOwnerRoleRule;
        this.tenantMaintainerRoleRule = tenantMaintainerRoleRule;
        this.tenantCustomRoleRule = tenantCustomRoleRule;
        this.applicationRootUserGroupRule = applicationRootUserGroupRule;
        this.applicationMaintainerUserGroupRule = applicationMaintainerUserGroupRule;
        this.tenantRootUserGroupRule = tenantRootUserGroupRule;
        this.tenantCustomUserGroupRule = tenantCustomUserGroupRule;
        this.nativePermissionPackageRule = nativePermissionPackageRule;
        this.customPermissionPackageRule = customPermissionPackageRule;
    }
}
