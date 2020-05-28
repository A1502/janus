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
    Inner-UserGroup关系查询角色-Multiple
    等号左边：OuterObject，UserGroup，Role
    等号右边：Permission，Role，user

*/
public class AccessControlCacheProvider09 extends BaseAccessControlCacheProvider {

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
        return null;
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXEntity> createScopeMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, ScopeUserGroupUserXEntity> createScopeUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, ScopeUserGroupUserXEntity> result = () -> {
                Map<IdType, ScopeUserGroupUserXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newScopeUserGroupUserXEntity(1, null, 1, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), EntityBuilder.newScopeUserGroupUserXEntity(2, null, 2, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), EntityBuilder.newScopeUserGroupUserXEntity(3, null, 3, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(4), EntityBuilder.newScopeUserGroupUserXEntity(4, null, 4, 1, 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupEntity> createUserGroupLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, UserGroupEntity> result = () -> {
                Map<IdType, UserGroupEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newUserGroupEntity(1, defaultAId, defaultTId, "code1", "name1", true, "des1", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newUserGroupEntity(2, defaultAId, defaultTId, "code2", "name2", true, "des2", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newUserGroupEntity(3, defaultAId, defaultTId, "code3", "name3", true, "des3", null, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupUserXEntity> createUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, UserGroupUserXEntity> result = () -> {
                Map<IdType, UserGroupUserXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newUserGroupUserXEntity(1, 1, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newUserGroupUserXEntity(2, 2, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newUserGroupUserXEntity(3, 3, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), EntityBuilder.newUserGroupUserXEntity(4, 4, 1, true, false, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), EntityBuilder.newUserGroupUserXEntity(5, 4, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionEntity> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, PermissionEntity> result = () -> {
                Map<IdType, PermissionEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newPermission(1, 1, defaultTId, null, "outerObjectRemark", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXEntity> createSingleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RolePermissionXEntity> result = () -> {
                Map<IdType, RolePermissionXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRolePermissionX(1, 1, 1, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
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
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionEntity> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXEntity> createMultipleRolePermissionXLoader(ApplicationIdType applicationId,
                                                                                              TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleEntity> createMultipleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleEntity> result = () -> {
                Map<IdType, RoleEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleEntity(1, defaultAId, defaultTId, "role1", true, "角色1", true, null, 10, "role1-outerObjectRemark", "role1-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleEntity(2, defaultAId, defaultTId, "role2", true, "角色2", true, null, null, "role2-outerObjectRemark", "role2-description", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXEntity> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXEntity> createMultipleRoleUserGroupXLoader(ApplicationIdType applicationId,
                                                                                            TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserGroupXEntity> result = () -> {
                Map<IdType, RoleUserGroupXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleUserGroupXEntity(1, 1, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleUserGroupXEntity(2, 2, 2, true, false, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newRoleUserGroupXEntity(3, 3, 3, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), EntityBuilder.newRoleUserGroupXEntity(4, 2, 4, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), EntityBuilder.newRoleUserGroupXEntity(5, 5, 4, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(6), EntityBuilder.newRoleUserGroupXEntity(6, 2, 6, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(7), EntityBuilder.newRoleUserGroupXEntity(7, 2, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
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
        SourceLoader<IdType, OuterObjectTypeEntity> result = () -> {
            Map<IdType, OuterObjectTypeEntity> map = new HashMap<>();
            map.put(IdBuilder.id(1), EntityBuilder.newOuterObjectTypeEntity(1, "outGroup", "外部组", "外部组", true, false, 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectEntity> createOuterObjectLoader(IdType outerObjectTypeId) {
        SourceLoader<IdType, OuterObjectEntity> result = () -> {
            Map<IdType, OuterObjectEntity> map = new HashMap<>();
            map.put(IdBuilder.id(10), EntityBuilder.newOuterObjectEntity(10, 1, "EMS001", "100", "销售", 1, new Date(), 1, new Date()));
            map.put(IdBuilder.id(20), EntityBuilder.newOuterObjectEntity(20, 1, "EMS002", "101", "HR", 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }

    @Override
    protected SourceLoader<IdType, UserOuterObjectXEntity> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        SourceLoader<IdType, UserOuterObjectXEntity> result = () -> {
            Map<IdType, UserOuterObjectXEntity> map = new HashMap<>();
            map.put(IdBuilder.id(2), EntityBuilder.newUserOuterObjectXEntity(2, 1, null, 1, "10,20,030", 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }
}
