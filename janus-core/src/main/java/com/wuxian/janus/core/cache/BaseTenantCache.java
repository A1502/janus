package com.wuxian.janus.core.cache;

import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.index.*;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author wuxian
 */

public abstract class BaseTenantCache {

    protected ApplicationIdType applicationId;

    protected TenantIdType tenantId;

    public BaseTenantCache(ApplicationIdType applicationId, TenantIdType tenantId) {
        this.applicationId = applicationId;
        this.tenantId = tenantId;
    }

    //<editor-fold desc="SingleInstancePool">

    private Map<Class, Object> singleInstancePool = new HashMap<>();

    private <T> T getSingleInstance(Class<T> clazz, Supplier<T> instanceBuilder) {
        if (!StrictUtils.containsKey(singleInstancePool, clazz)) {
            singleInstancePool.put(clazz, instanceBuilder.get());
        }
        return (T) singleInstancePool.get(clazz);
    }
    //</editor-fold>

    //<editor-fold desc="MultipleInstancePool">

    private Map<Class, Object> multipleInstancePool = new HashMap<>();

    private <T> T getMultipleInstance(Class<T> clazz, Supplier<T> instanceBuilder) {
        if (!StrictUtils.containsKey(multipleInstancePool, clazz)) {
            multipleInstancePool.put(clazz, instanceBuilder.get());
        }
        return (T) multipleInstancePool.get(clazz);
    }
    //</editor-fold>

    //<editor-fold desc="Scope map">

    protected abstract ScopeRoleUserXMap createScopeSingleRoleUserXMap();

    protected abstract ScopeRoleUserXMap createScopeMultipleRoleUserXMap();

    protected abstract ScopeUserGroupUserXMap createScopeUserGroupUserXMap();

    public ScopeRoleUserXMap getScopeSingleRoleUserX() {
        return getSingleInstance(ScopeRoleUserXMap.class, this::createScopeSingleRoleUserXMap);
    }

    public ScopeRoleUserXMap getScopeMultipleRoleUserX() {
        return getMultipleInstance(ScopeRoleUserXMap.class, this::createScopeMultipleRoleUserXMap);
    }

    public ScopeUserGroupUserXMap getScopeUserGroupUserX() {
        return getSingleInstance(ScopeUserGroupUserXMap.class, this::createScopeUserGroupUserXMap);
    }
    //</editor-fold>

    //<editor-fold desc="User group map">

    protected abstract UserGroupMap createUserGroupMap();

    protected abstract UserGroupUserXMap createUserGroupUserXMap();

    public UserGroupMap getUserGroup() {
        return getSingleInstance(UserGroupMap.class, this::createUserGroupMap);
    }

    public UserGroupUserXMap getUserGroupUserX() {
        return getSingleInstance(UserGroupUserXMap.class, this::createUserGroupUserXMap);
    }
    //</editor-fold>

    //<editor-fold desc="Single permission map">

    protected abstract PermissionMap createSinglePermissionMap();

    protected abstract RolePermissionXMap createSingleRolePermissionXMap();

    protected abstract RoleMap createSingleRoleMap();

    protected abstract RoleOtherXMap createSingleRoleOtherXMap();

    protected abstract RoleUserGroupXMap createSingleRoleUserGroupXMap();

    protected abstract RoleUserXMap createSingleRoleUserXMap();

    public PermissionMap getSinglePermission() {
        return getSingleInstance(PermissionMap.class, this::createSinglePermissionMap);
    }

    public RolePermissionXMap getSingleRolePermissionX() {
        return getSingleInstance(RolePermissionXMap.class, this::createSingleRolePermissionXMap);
    }

    public RoleMap getSingleRole() {
        return getSingleInstance(RoleMap.class, this::createSingleRoleMap);
    }

    public RoleOtherXMap getSingleRoleOtherX() {
        return getSingleInstance(RoleOtherXMap.class, this::createSingleRoleOtherXMap);
    }

    public RoleUserGroupXMap getSingleRoleUserGroupX() {
        return getSingleInstance(RoleUserGroupXMap.class, this::createSingleRoleUserGroupXMap);
    }

    public RoleUserXMap getSingleRoleUserX() {
        return getSingleInstance(RoleUserXMap.class, this::createSingleRoleUserXMap);
    }
    //</editor-fold>

    //<editor-fold desc="Multiple permission map">

    protected abstract PermissionMap createMultiplePermissionMap();

    protected abstract RolePermissionXMap createMultipleRolePermissionXMap();

    protected abstract RoleMap createMultipleRoleStructMap();

    protected abstract RoleOtherXMap createMultipleRoleOtherXMap();

    protected abstract RoleUserGroupXMap createMultipleRoleUserGroupXMap();

    protected abstract RoleUserXMap createMultipleRoleUserXMap();

    public PermissionMap getMultiplePermission() {
        return getMultipleInstance(PermissionMap.class, this::createMultiplePermissionMap);
    }

    public RolePermissionXMap getMultipleRolePermissionX() {
        return getMultipleInstance(RolePermissionXMap.class, this::createMultipleRolePermissionXMap);
    }

    public RoleMap getMultipleRole() {
        return getMultipleInstance(RoleMap.class, this::createMultipleRoleStructMap);
    }

    public RoleOtherXMap getMultipleRoleOtherX() {
        return getMultipleInstance(RoleOtherXMap.class, this::createMultipleRoleOtherXMap);
    }

    public RoleUserGroupXMap getMultipleRoleUserGroupX() {
        return getMultipleInstance(RoleUserGroupXMap.class, this::createMultipleRoleUserGroupXMap);
    }

    public RoleUserXMap getMultipleRoleUserX() {
        return getMultipleInstance(RoleUserXMap.class, this::createMultipleRoleUserXMap);
    }
    //</editor-fold>
}
