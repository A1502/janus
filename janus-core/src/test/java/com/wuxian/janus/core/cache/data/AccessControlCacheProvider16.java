package com.wuxian.janus.core.cache.data;

import com.wuxian.janus.StructBuilder;
import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.basis.StrictUtils;
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
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, ScopeRoleUserXStruct> result = () -> {
                Map<IdType, ScopeRoleUserXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newScopeRoleUserX(1, "C", 5, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), StructBuilder.newScopeRoleUserX(2, "Y", 5, 1, 1, new Date(), 1, new Date()));
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
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, ScopeUserGroupUserXStruct> result = () -> {
                Map<IdType, ScopeUserGroupUserXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newScopeUserGroupUserXStruct(1, "B", 7, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), StructBuilder.newScopeUserGroupUserXStruct(2, "X", 7, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), StructBuilder.newScopeUserGroupUserXStruct(3, "Y", 7, 1, 1, new Date(), 1, new Date()));
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
                map.put(IdBuilder.id(7), StructBuilder.newUserGroupStruct(7, defaultAId, defaultTId, "bbbUG7", "bbbUG76", true, "stdUG", null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(6), StructBuilder.newUserGroupStruct(6, defaultAId, defaultTId, "bbbUG6", "bbbUG", true, "stdUG", 10, 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(1), StructBuilder.newUserGroupUserXStruct(1, 7, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionStruct> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXStruct> createSingleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleStruct> createSingleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleStruct> result = () -> {
                Map<IdType, RoleStruct> map = new HashMap<>();
                map.put(IdBuilder.id(5), StructBuilder.newRoleStruct(5, defaultAId, defaultTId, "xxxRole", false, "xxxRole", true, null, null, "role5-outerObjectRemark", "draft-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), StructBuilder.newRoleStruct(4, defaultAId, defaultTId, "yyyRole", false, "yyyRole", true, null, null, "role4-outerObjectRemark", "yyy-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newRoleStruct(3, defaultAId, defaultTId, "zzzRole", false, "zzzRole", true, null, null, "role3-outerObjectRemark", "yyy-description", 1, new Date(), 1, new Date(), 1));
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
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserGroupXStruct> result = () -> {
                Map<IdType, RoleUserGroupXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(63), StructBuilder.newRoleUserGroupXStruct(63, 4, 6, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(64), StructBuilder.newRoleUserGroupXStruct(64, 3, 7, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleUserXStruct> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleUserXStruct> result = () -> {
                Map<IdType, RoleUserXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newRoleUserXStruct(1, 5, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
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
        return null;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectTypeStruct> createOuterObjectTypeLoader() {
        SourceLoader<IdType, OuterObjectTypeStruct> result = () -> {
            Map<IdType, OuterObjectTypeStruct> map = new HashMap<>();
            map.put(IdBuilder.id(1), StructBuilder.newOuterObjectTypeStruct(1, "AAAOutGroup", "AAA外部组", "AAA外部组", true, false, 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectStruct> createOuterObjectLoader(IdType outerObjectTypeId) {
        if (StrictUtils.equals(outerObjectTypeId, IdBuilder.id(outerObjectTypeId1))) {
            SourceLoader<IdType, OuterObjectStruct> result = () -> {
                Map<IdType, OuterObjectStruct> map = new HashMap<>();
                map.put(IdBuilder.id(10), StructBuilder.newOuterObjectStruct(10, outerObjectTypeId1, "EMS001", "100", "销售", null, 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserOuterObjectXStruct> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        if (StrictUtils.equals(outerObjectTypeId, IdBuilder.id(outerObjectTypeId1))) {
            SourceLoader<IdType, UserOuterObjectXStruct> result = () -> {
                Map<IdType, UserOuterObjectXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(1), StructBuilder.newUserOuterObjectXStruct(1, outerObjectTypeId1, null, 1, "10", 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), StructBuilder.newUserOuterObjectXStruct(2, outerObjectTypeId1, "A", 1, "10", 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), StructBuilder.newUserOuterObjectXStruct(3, outerObjectTypeId1, "X", 1, "10", 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(4), StructBuilder.newUserOuterObjectXStruct(4, outerObjectTypeId1, "Y", 1, "10", 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        }
        return null;
    }
}
