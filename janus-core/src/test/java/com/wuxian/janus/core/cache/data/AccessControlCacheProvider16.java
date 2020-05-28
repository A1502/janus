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

public class AccessControlCacheProvider16 extends BaseAccessControlCacheProvider {

    //测试scope功能

    //等号左边：OuterObject，UserGroup，Role
    //等号右边：Permission，Role，User


    //【主数据】
    //OuterObject(10,primary:1) = User(1)[scope:A,X,Y,null]

    //UserGroup(7) = User(1)[scope:B,X,Y],Role(3)
    //UserGroup(6,outer:10) = Role(4)
    //Role(5) = User(1)[scope:C,Y]

    //【陪衬数据】
    //无

    private final int defaultAId = 1;
    private final int defaultTId = 10;
    private final int outerObjectTypeId1 = 1;

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
                map.put(IdBuilder.id(1), EntityBuilder.newScopeRoleUserX(1, "C", 5, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), EntityBuilder.newScopeRoleUserX(2, "Y", 5, 1, 1, new Date(), 1, new Date()));
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
                map.put(IdBuilder.id(1), EntityBuilder.newScopeUserGroupUserXEntity(1, "B", 7, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), EntityBuilder.newScopeUserGroupUserXEntity(2, "X", 7, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), EntityBuilder.newScopeUserGroupUserXEntity(3, "Y", 7, 1, 1, new Date(), 1, new Date()));
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
                map.put(IdBuilder.id(7), EntityBuilder.newUserGroupEntity(7, defaultAId, defaultTId, "bbbUG7", "bbbUG76", true, "stdUG", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(6), EntityBuilder.newUserGroupEntity(6, defaultAId, defaultTId, "bbbUG6", "bbbUG", true, "stdUG", 10, 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(1), EntityBuilder.newUserGroupUserXEntity(1, 7, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(5), EntityBuilder.newRoleEntity(5, defaultAId, defaultTId, "xxxRole", false, "xxxRole", true, null, null, "role5-outerObjectRemark", "draft-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), EntityBuilder.newRoleEntity(4, defaultAId, defaultTId, "yyyRole", false, "yyyRole", true, null, null, "role4-outerObjectRemark", "yyy-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newRoleEntity(3, defaultAId, defaultTId, "zzzRole", false, "zzzRole", true, null, null, "role3-outerObjectRemark", "yyy-description", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXEntity> createSingleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXEntity> createSingleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserGroupXEntity> result = () -> {
                Map<IdType, RoleUserGroupXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(63), EntityBuilder.newRoleUserGroupXEntity(63, 4, 6, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(64), EntityBuilder.newRoleUserGroupXEntity(64, 3, 7, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserXEntity> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserXEntity> result = () -> {
                Map<IdType, RoleUserXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleUserXEntity(1, 5, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
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
            map.put(IdBuilder.id(1), EntityBuilder.newOuterObjectTypeEntity(1, "AAAOutGroup", "AAA外部组", "AAA外部组", true, false, 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectEntity> createOuterObjectLoader(IdType outerObjectTypeId) {
        if (StrictUtils.equals(outerObjectTypeId, IdBuilder.id(outerObjectTypeId1))) {
            SourceLoader<IdType, OuterObjectEntity> result = () -> {
                Map<IdType, OuterObjectEntity> map = new HashMap<>();
                map.put(IdBuilder.id(10), EntityBuilder.newOuterObjectEntity(10, outerObjectTypeId1, "EMS001", "100", "销售", 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserOuterObjectXEntity> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        if (StrictUtils.equals(outerObjectTypeId, IdBuilder.id(outerObjectTypeId1))) {
            SourceLoader<IdType, UserOuterObjectXEntity> result = () -> {
                Map<IdType, UserOuterObjectXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newUserOuterObjectXEntity(1, outerObjectTypeId1, null, 1, "10", 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), EntityBuilder.newUserOuterObjectXEntity(2, outerObjectTypeId1, "A", 1, "10", 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), EntityBuilder.newUserOuterObjectXEntity(3, outerObjectTypeId1, "X", 1, "10", 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(4), EntityBuilder.newUserOuterObjectXEntity(4, outerObjectTypeId1, "Y", 1, "10", 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        }
        return null;
    }
}
