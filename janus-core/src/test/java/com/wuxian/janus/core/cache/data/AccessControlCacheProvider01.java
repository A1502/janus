package com.wuxian.janus.core.cache.data;

import com.wuxian.janus.StructBuilder;
import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.core.cache.provider.BaseAccessControlCacheProvider;
import com.wuxian.janus.struct.*;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.*;

/*
    Role-User关系查找角色--Single
    等号左边：OuterObject，UserGroup，Role
    等号右边：Permission，Role，user


    【主数据】
    Role(1) = userGroup(1,user:1)

    【陪衬数据】
    Role(2)
    Permission()

*/

public class AccessControlCacheProvider01 extends BaseAccessControlCacheProvider {

    private final int defaultAId = 1;
    private final int defaultTId = 10;

    @Override
    protected Map<ApplicationIdType, Set<TenantIdType>> loadApplicationIdTenantIdRange() {
        Map<ApplicationIdType, Set<TenantIdType>> result = new HashMap<>();
        result.put(IdBuilder.aId(defaultAId), Collections.singleton(IdBuilder.tId(defaultTId)));
        return result;
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, ScopeRoleUserXStruct> result = () -> {
                Map<IdType, ScopeRoleUserXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newScopeRoleUserX(1, null, 1, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), StructBuilder.newScopeRoleUserX(2, null, 2, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), StructBuilder.newScopeRoleUserX(3, null, 3, 1, 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, ScopeUserGroupUserXStruct> createScopeUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupStruct> createUserGroupLoader(ApplicationIdType applicationId, TenantIdType tenantId) {

        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupUserXStruct> createUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionStruct> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, PermissionStruct> result = () -> {
                Map<IdType, PermissionStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newPermission(1, 1, 10, null, "outerObjectRemark", 1, new Date(), 1, new Date(), 1));
//                map.put(IdBuilder.id(2, StructBuilder.newPermission(1, 1, defaultTId, 1, "outerObjectRemark", 1, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXStruct> createSingleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RolePermissionXStruct> result = () -> {
                Map<IdType, RolePermissionXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newRolePermissionX(1, 1, 1, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleStruct> createSingleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleStruct> result = () -> {
                Map<IdType, RoleStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newRoleStruct(1, 1, 10, "role1", false, "角色1", true, null, null, "role1-outerObjectRemark", "role1-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newRoleStruct(2, 1, 10, "role2", false, "角色2", true, null, null, "role2-outerObjectRemark", "role2-description", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXStruct> createSingleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {

        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXStruct> createSingleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {

        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserXStruct> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserXStruct> result = () -> {
                Map<IdType, RoleUserXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newRoleUserXStruct(1, 1, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newRoleUserXStruct(2, 2, 1, true, false, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newRoleUserXStruct(3, 3, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionStruct> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXStruct> createMultipleRolePermissionXLoader(ApplicationIdType applicationId,
                                                                                              TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleStruct> createMultipleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXStruct> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXStruct> createMultipleRoleUserGroupXLoader(ApplicationIdType applicationId,
                                                                                            TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserXStruct> createMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionTemplateStruct> createPermissionTemplateLoader(ApplicationIdType applicationId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId))) {
            SourceLoader<IdType, PermissionTemplateStruct> result = () -> {
                Map<IdType, PermissionTemplateStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newPermissionTemplateStruct(1, 1, "pt1", false, "权限模板1", "权限模板1", "高级查询", "高级查询", null, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectTypeStruct> createOuterObjectTypeLoader() {
        return null;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectStruct> createOuterObjectLoader(IdType outerObjectTypeId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserOuterObjectXStruct> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        return null;
    }
}
