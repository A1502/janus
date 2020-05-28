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
    role-user,role-userGroup,role-other,role-outGroup 4种组合查询roles
    等号左边：OuterObject，UserGroup，Role
    等号右边：Permission，Role，user

*/
public class AccessControlCacheProvider17 extends BaseAccessControlCacheProvider {

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
                map.put(IdBuilder.id(1), EntityBuilder.newScopeRoleUserX(1, null, 1, 1, 1, new Date(), 1, new Date()));
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
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, ScopeUserGroupUserXEntity> result = () -> {
                Map<IdType, ScopeUserGroupUserXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newScopeUserGroupUserXEntity(1, null, 1, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), EntityBuilder.newScopeUserGroupUserXEntity(2, null, 2, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), EntityBuilder.newScopeUserGroupUserXEntity(3, null, 3, 1, 1, new Date(), 1, new Date()));
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
                map.put(IdBuilder.id(1), EntityBuilder.newUserGroupEntity(1, defaultAId, defaultTId, "userGroup1", "userGroup1", true, "userGroup1", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newUserGroupEntity(2, defaultAId, defaultTId, "userGroup2", "userGroup2", true, "userGroup2", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newUserGroupEntity(3, defaultAId, defaultTId, "userGroup3", "userGroup3", true, "userGroup3", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), EntityBuilder.newUserGroupEntity(4, defaultAId, defaultTId, "outGroup1", "outGroup1", true, "outGroup1", 10, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), EntityBuilder.newUserGroupEntity(5, defaultAId, defaultTId, "outGroup2", "outGroup2", true, "outGroup2", 3, 1, new Date(), 1, new Date(), 1));
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
                return map;
            };
            return result;
        }
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
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleEntity> result = () -> {
                Map<IdType, RoleEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleEntity(1, defaultAId, defaultTId, "role1", false, "角色1", true, null, null, "role1-outerObjectRemark", "role1-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleEntity(2, defaultAId, defaultTId, "role2", false, "角色2", true, null, null, "role2-outerObjectRemark", "role2-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newRoleEntity(3, defaultAId, defaultTId, "role3", false, "角色3", true, null, null, "role3-outerObjectRemark", "role3-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), EntityBuilder.newRoleEntity(4, defaultAId, defaultTId, "role4", false, "角色4", true, null, null, "role4-outerObjectRemark", "role4-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), EntityBuilder.newRoleEntity(5, defaultAId, defaultTId, "role5", false, "角色5", true, null, null, "role5-outerObjectRemark", "role5-description", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    //role-other=>role3
    @Override
    protected SourceLoader<IdType, RoleOtherXEntity> createSingleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleOtherXEntity> result = () -> {
                Map<IdType, RoleOtherXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleOtherXEntity(1, 3, true, true, true, true, true, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleOtherXEntity(2, 20, true, true, true, true, true, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    //role-userGroup=>role1和role2和role4
    @Override
    protected SourceLoader<IdType, RoleUserGroupXEntity> createSingleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserGroupXEntity> result = () -> {
                Map<IdType, RoleUserGroupXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleUserGroupXEntity(1, 1, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleUserGroupXEntity(2, 2, 2, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newRoleUserGroupXEntity(3, 5, 3, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), EntityBuilder.newRoleUserGroupXEntity(4, 4, 4, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), EntityBuilder.newRoleUserGroupXEntity(5, 5, 5, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    //role-user直接关联=>role1
    @Override
    protected SourceLoader<IdType, RoleUserXEntity> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserXEntity> result = () -> {
                Map<IdType, RoleUserXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleUserXEntity(1, 1, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
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
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXEntity> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
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
