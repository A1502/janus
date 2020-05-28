package com.wuxian.janus.core.cache.data;

import com.wuxian.janus.EntityBuilder;
import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.core.cache.provider.BaseAccessControlCacheProvider;
import com.wuxian.janus.entity.*;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;

import java.util.*;

/*
    Role-Other关系查询角色--Multiple
    等号左边：OuterObject，UserGroup，Role
    等号右边：Permission，Role，user

*/
public class AccessControlCacheProvider08 extends BaseAccessControlCacheProvider {

    private final int defaultAId = 1;
    private final int defaultTId = 10;

    @Override
    protected Map<ApplicationIdType, Set<TenantIdType>> loadApplicationIdTenantIdRange() {
        Map<ApplicationIdType, Set<TenantIdType>> result = new HashMap<>();
        result.put(IdBuilder.aId(defaultAId), Collections.singleton(IdBuilder.tId(defaultTId)));
        return result;
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXEntity> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, ScopeRoleUserXEntity> result = () -> {
                Map<IdType, ScopeRoleUserXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(2), EntityBuilder.newScopeRoleUserX(2, null, 1, 1, 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        }
        return null;
    }


    @Override
    protected SourceLoader<IdType, ScopeRoleUserXEntity> createScopeMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, ScopeUserGroupUserXEntity> createScopeUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupEntity> createUserGroupLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupUserXEntity> createUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionEntity> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXEntity> createSingleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleEntity> createSingleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXEntity> createSingleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXEntity> createSingleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserXEntity> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserXEntity> loader = () -> {
                Map<IdType, RoleUserXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleUserXEntity(2, 1, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return loader;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionEntity> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, PermissionEntity> result = () -> {
                Map<IdType, PermissionEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newPermission(1, 1, defaultTId, 1, "outerObjectRemark", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXEntity> createMultipleRolePermissionXLoader(ApplicationIdType applicationId,
                                                                                              TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RolePermissionXEntity> result = () -> {
                Map<IdType, RolePermissionXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRolePermissionX(1, 1, 1, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRolePermissionX(2, 2, 1, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleEntity> createMultipleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {

        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleEntity> result = () -> {
                Map<IdType, RoleEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleEntity(1, defaultAId, defaultTId, "role1", false, "角色1", true, null, null, "role1-outerObjectRemark", "role1-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleEntity(2, defaultAId, defaultTId, "role2", false, "角色2", true, null, 1, "role2-outerObjectRemark", "role2-description", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXEntity> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleOtherXEntity> result = () -> {
                Map<IdType, RoleOtherXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleOtherXEntity(1, 1, true, false, true, true, true, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleOtherXEntity(2, 2, true, true, true, true, true, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newRoleOtherXEntity(3, 3, true, true, true, true, true, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXEntity> createMultipleRoleUserGroupXLoader(ApplicationIdType applicationId,
                                                                                            TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserXEntity> createMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionTemplateEntity> createPermissionTemplateLoader(ApplicationIdType applicationId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId))) {
            SourceLoader<IdType, PermissionTemplateEntity> result = () -> {
                Map<IdType, PermissionTemplateEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newPermissionTemplateEntity(1, defaultAId, "pt1", false, "权限模板1", "权限模板1", "高级查询", "高级查询", null, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectTypeEntity> createOuterObjectTypeLoader() {
        return null;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectEntity> createOuterObjectLoader(IdType outerObjectTypeId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserOuterObjectXEntity> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        return null;
    }
}
