package com.wuxian.janus.core.critical;

import lombok.Getter;

@Getter
@SuppressWarnings("all")
public class NativeRuleTable {
    /**
     * 对内置角色"AllPermission"的访问控制
     */
    private ClassicLevelEnum allPermissionRoleRule;

    /**
     * 对内置角色"ApplicationMaintainer"的访问控制
     */
    private ClassicLevelEnum applicationMaintainerRoleRule;

    /**
     * 对内置角色"TenantDataOwner"的访问控制
     */
    private ClassicLevelEnum tenantDataOwnerRoleRule;

    /**
     * 对内置角色"TenantMaintainer"的访问控制
     */
    private ClassicLevelEnum tenantMaintainerRoleRule;

    /**
     * 对所有Tenant级自定义角色的访问控制
     */
    private ClassicLevelEnum tenantCustomRoleRule;

    /**
     * 对内置用户组"ApplicationRoot"的访问控制
     */
    private ClassicLevelEnum applicationRootUserGroupRule;

    /**
     * 对内置用户组"ApplicationMaintainer"的访问控制
     */
    private ClassicLevelEnum applicationMaintainerUserGroupRule;

    /**
     * 对内置用户组"TenantRoot"的访问控制
     */
    private ClassicLevelEnum tenantRootUserGroupRule;

    /**
     * 对Tenant级所有自定义用户组的访问控制
     */
    private ClassicLevelEnum tenantCustomUserGroupRule;

    /**
     * 对内置权限打包的访问控制
     */
    private ClassicLevelEnum nativePermissionPackageRule;

    /**
     * 对Tenant级所有自定义权限打包的访问控制
     */
    private ClassicLevelEnum customPermissionPackageRule;

    /**
     * 这是特意不用lombok自动生成。为了避免自动生成的参数顺序不确定性的风险。
     */
    public NativeRuleTable(ClassicLevelEnum allPermissionRoleRule,
                           ClassicLevelEnum applicationMaintainerRoleRule,
                           ClassicLevelEnum tenantDataOwnerRoleRule,
                           ClassicLevelEnum tenantMaintainerRoleRule,
                           ClassicLevelEnum tenantCustomRoleRule,
                           ClassicLevelEnum applicationRootUserGroupRule,
                           ClassicLevelEnum applicationMaintainerUserGroupRule,
                           ClassicLevelEnum tenantRootUserGroupRule,
                           ClassicLevelEnum tenantCustomUserGroupRule,
                           ClassicLevelEnum nativePermissionPackageRule,
                           ClassicLevelEnum customPermissionPackageRule
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
