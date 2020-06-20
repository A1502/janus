package com.wuxian.janus.core.cache.data;

import com.wuxian.janus.StructBuilder;
import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.util.StrictUtils;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.core.cache.provider.BaseAccessControlCacheProvider;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
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
    内置用户组AGRoot得到AllPermission角色--Single
    等号左边：OuterObject，UserGroup，Role
    等号右边：Permission，Role，user

*/
public class AccessControlCacheProvider10 extends BaseAccessControlCacheProvider {

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
                map.put(IdBuilder.id(1), StructBuilder.newUserGroupStruct(1, defaultAId, null, "janus_ag:root", NativeUserGroupEnum.APPLICATION_ROOT.getCode(), true, null, null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newUserGroupStruct(3, defaultAId, null, "janus_ag:root", NativeUserGroupEnum.APPLICATION_ROOT.getCode(), true, null, null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), StructBuilder.newUserGroupStruct(4, defaultAId, defaultTId, "code4", "name4", true, "des4", 1, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), StructBuilder.newUserGroupStruct(5, defaultAId, defaultTId, "code5", "name5", true, "des5", 10, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(6), StructBuilder.newUserGroupStruct(6, defaultAId, defaultTId, "code6", "name6", true, "des6", 20, 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(1), StructBuilder.newUserGroupUserXStruct(1, 1, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newUserGroupUserXStruct(2, 2, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newUserGroupUserXStruct(3, 3, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), StructBuilder.newUserGroupUserXStruct(4, 4, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, UserGroupOtherXStruct> createUserGroupOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
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
                map.put(IdBuilder.id(1), StructBuilder.newRoleStruct(1, defaultAId, null, NativeRoleEnum.ALL_PERMISSION.getCode(), false, NativeRoleEnum.ALL_PERMISSION.getCode(), true, null, null, null, NativeRoleEnum.ALL_PERMISSION.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newRoleStruct(2, defaultAId, null, NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), false, NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), true, null, null, null, NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newRoleStruct(3, defaultAId, defaultTId, NativeRoleEnum.TENANT_MAINTAINER.getCode(), false, NativeRoleEnum.TENANT_MAINTAINER.getCode(), true, null, null, null, NativeRoleEnum.TENANT_MAINTAINER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), StructBuilder.newRoleStruct(4, defaultAId, defaultTId, NativeRoleEnum.TENANT_DATA_OWNER.getCode(), false, NativeRoleEnum.TENANT_DATA_OWNER.getCode(), true, null, null, null, NativeRoleEnum.TENANT_DATA_OWNER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), StructBuilder.newRoleStruct(5, defaultAId, defaultTId, "role5", false, "角色5", true, null, null, "role5-outerObjectRemark", "role1-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(6), StructBuilder.newRoleStruct(6, defaultAId, defaultTId, "role6", false, "角色6", true, null, null, "role6-outerObjectRemark", "role2-description", 1, new Date(), 1, new Date(), 1));

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
                map.put(IdBuilder.id(1), StructBuilder.newRoleUserGroupXStruct(1, 5, 5, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newRoleUserGroupXStruct(2, 60, 6, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
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
            map.put(IdBuilder.id(1), StructBuilder.newUserOuterObjectXStruct(1, 1, null, 1, "10,20", 1, new Date(), 1, new Date()));
//            map.put(IdBuilder.id(2), StructBuilder.newUserOuterObjectXStruct(2, 1, 1, "1,2,3", 1, new Date(), 1, new Date()));
//            map.put(IdBuilder.id(3), StructBuilder.newUserOuterObjectXStruct(3, 1, 1, ",1,2,", 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }
}
