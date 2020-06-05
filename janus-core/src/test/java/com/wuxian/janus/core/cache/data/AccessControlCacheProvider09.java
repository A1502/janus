package com.wuxian.janus.core.cache.data;

import com.wuxian.janus.StructBuilder;
import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.core.cache.provider.BaseAccessControlCacheProvider;
import com.wuxian.janus.struct.layer1.*;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

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
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, ScopeUserGroupUserXStruct> createScopeUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, ScopeUserGroupUserXStruct> result = () -> {
                Map<IdType, ScopeUserGroupUserXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newScopeUserGroupUserXStruct(1, null, 1, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), StructBuilder.newScopeUserGroupUserXStruct(2, null, 2, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), StructBuilder.newScopeUserGroupUserXStruct(3, null, 3, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(4), StructBuilder.newScopeUserGroupUserXStruct(4, null, 4, 1, 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupStruct> createUserGroupLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, UserGroupStruct> result = () -> {
                Map<IdType, UserGroupStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newUserGroupStruct(1, defaultAId, defaultTId, "code1", "name1", true, "des1", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newUserGroupStruct(2, defaultAId, defaultTId, "code2", "name2", true, "des2", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newUserGroupStruct(3, defaultAId, defaultTId, "code3", "name3", true, "des3", null, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupUserXStruct> createUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, UserGroupUserXStruct> result = () -> {
                Map<IdType, UserGroupUserXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newUserGroupUserXStruct(1, 1, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newUserGroupUserXStruct(2, 2, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newUserGroupUserXStruct(3, 3, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), StructBuilder.newUserGroupUserXStruct(4, 4, 1, true, false, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), StructBuilder.newUserGroupUserXStruct(5, 4, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionStruct> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, PermissionStruct> result = () -> {
                Map<IdType, PermissionStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newPermission(1, 1, defaultTId, null, "outerObjectRemark", 1, new Date(), 1, new Date(), 1));
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
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleStruct> result = () -> {
                Map<IdType, RoleStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newRoleStruct(1, defaultAId, defaultTId, "role1", true, "角色1", true, null, 10, "role1-outerObjectRemark", "role1-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newRoleStruct(2, defaultAId, defaultTId, "role2", true, "角色2", true, null, null, "role2-outerObjectRemark", "role2-description", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXStruct> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXStruct> createMultipleRoleUserGroupXLoader(ApplicationIdType applicationId,
                                                                                            TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserGroupXStruct> result = () -> {
                Map<IdType, RoleUserGroupXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newRoleUserGroupXStruct(1, 1, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newRoleUserGroupXStruct(2, 2, 2, true, false, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newRoleUserGroupXStruct(3, 3, 3, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), StructBuilder.newRoleUserGroupXStruct(4, 2, 4, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), StructBuilder.newRoleUserGroupXStruct(5, 5, 4, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(6), StructBuilder.newRoleUserGroupXStruct(6, 2, 6, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(7), StructBuilder.newRoleUserGroupXStruct(7, 2, 1, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
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
                map.put(IdBuilder.id(1), StructBuilder.newPermissionTemplateStruct(1, defaultAId, "pt1", false, "权限模板1", "权限模板1", "高级查询", "高级查询", null, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectTypeStruct> createOuterObjectTypeLoader() {
        SourceLoader<IdType, OuterObjectTypeStruct> result = () -> {
            Map<IdType, OuterObjectTypeStruct> map = new HashMap<>();
            map.put(IdBuilder.id(1), StructBuilder.newOuterObjectTypeStruct(1, "outGroup", "外部组", "外部组", true, false, 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectStruct> createOuterObjectLoader(IdType outerObjectTypeId) {
        SourceLoader<IdType, OuterObjectStruct> result = () -> {
            Map<IdType, OuterObjectStruct> map = new HashMap<>();
            map.put(IdBuilder.id(10), StructBuilder.newOuterObjectStruct(10, 1, "EMS001", "100", "销售", null, 1, new Date(), 1, new Date()));
            map.put(IdBuilder.id(20), StructBuilder.newOuterObjectStruct(20, 1, "EMS002", "101", "HR", null, 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }

    @Override
    protected SourceLoader<IdType, UserOuterObjectXStruct> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        SourceLoader<IdType, UserOuterObjectXStruct> result = () -> {
            Map<IdType, UserOuterObjectXStruct> map = new HashMap<>();
            map.put(IdBuilder.id(2), StructBuilder.newUserOuterObjectXStruct(2, 1, null, 1, "10,20,030", 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }
}
