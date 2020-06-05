package com.wuxian.janus.core.cache.data;

import com.wuxian.janus.StructBuilder;
import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.core.cache.provider.BaseAccessControlCacheProvider;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.critical.PermissionLevelEnum;
import com.wuxian.janus.struct.*;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.*;

public class AccessControlCacheProvider15 extends BaseAccessControlCacheProvider {

    //相对于Data18,把Role(1)=User(1)去掉了，不再有AllPermission

    //等号左边：OuterObject，UserGroup，Role
    //等号右边：Permission，Role，User


    //【主数据】
    //OuterObject(12,type:2)
    //OuterObject(10,type:1) = User(1)

    //UserGroup(6,outer:10) = Role(5)
    //UserGroup(7) = User(1),Role(6),Role(7)

    //Role(3,TENANT_MAINTAINER) = Permission(715,template:15),Permission(205,template:5),Permission(206,template:6),User(1)
    //Role(5) = Permission(715,template:15)(716,template:16)
    //Role(6) = Permission(716,template:16)(717,template:17)
    //Role(7,outer:12) = Permission(719,outer:12,template:19)

    //【陪衬数据】
    //4个NativeRole，6个NativePermission
    //OuterObject(11,type:2)
    //Role(1,ALL_PERMISSION)
    //Permission(714,template:14)
    //Permission(718,outer:12,template:18)
    //Permission(1000,outer:11,template:100)

    //【测试中间结果】
    //User(1)加入了Role(3,5,6)

    private final int defaultAId = 1;
    private final int defaultTId = 10;
    private final int outerObjectTypeId1 = 1;
    private final int outerObjectTypeId2 = 2;

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
                map.put(IdBuilder.id(1), StructBuilder.newScopeRoleUserX(1, null, 3, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(2), StructBuilder.newScopeRoleUserX(2, null, 1, 1, 1, new Date(), 1, new Date()));

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
                map.put(IdBuilder.id(1), StructBuilder.newScopeUserGroupUserXStruct(1, null, 7, 1, 1, new Date(), 1, new Date()));
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
                map.put(IdBuilder.id(6), StructBuilder.newUserGroupStruct(6, defaultAId, defaultTId, "stdUG", "stdUG", true, "stdUG", 10, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(7), StructBuilder.newUserGroupStruct(7, defaultAId, defaultTId, "bbbUG", "bbbUG", true, "stdUG", null, 1, new Date(), 1, new Date(), 1));
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
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, PermissionStruct> result = () -> {
                Map<IdType, PermissionStruct> map = new HashMap<>();
                map.put(IdBuilder.id(714), StructBuilder.newPermission(714, 14, defaultTId, null, "14-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(715), StructBuilder.newPermission(715, 15, defaultTId, null, "15-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(716), StructBuilder.newPermission(716, 16, defaultTId, null, "16-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(717), StructBuilder.newPermission(717, 17, defaultTId, null, "17-remark", 1, new Date(), 1, new Date(), 1));

                map.put(IdBuilder.id(205), StructBuilder.newPermission(205, 5, defaultTId, null, "17-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(206), StructBuilder.newPermission(206, 6, defaultTId, null, "17-remark", 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(80), StructBuilder.newRolePermissionX(80, 3, 715, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(81), StructBuilder.newRolePermissionX(81, 5, 715, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(82), StructBuilder.newRolePermissionX(82, 5, 716, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(83), StructBuilder.newRolePermissionX(83, 6, 716, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(84), StructBuilder.newRolePermissionX(84, 6, 717, 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(2), StructBuilder.newRoleStruct(2, defaultAId, null, NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), false, NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), true, null, null, null, NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), StructBuilder.newRoleStruct(3, defaultAId, defaultTId, NativeRoleEnum.TENANT_MAINTAINER.getCode(), false, NativeRoleEnum.TENANT_MAINTAINER.getCode(), true, null, null, null, NativeRoleEnum.TENANT_MAINTAINER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), StructBuilder.newRoleStruct(4, defaultAId, defaultTId, NativeRoleEnum.TENANT_DATA_OWNER.getCode(), false, NativeRoleEnum.TENANT_DATA_OWNER.getCode(), true, null, null, null, NativeRoleEnum.TENANT_DATA_OWNER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), StructBuilder.newRoleStruct(5, defaultAId, defaultTId, "xxxRole", false, "xxxRole", true, null, null, "role5-outerObjectRemark", "draft-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(6), StructBuilder.newRoleStruct(6, defaultAId, defaultTId, "yyyRole", false, "yyyRole", true, null, null, "role6-outerObjectRemark", "yyy-description", 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(62), StructBuilder.newRoleUserGroupXStruct(62, 5, 6, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(63), StructBuilder.newRoleUserGroupXStruct(63, 6, 7, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(1), StructBuilder.newRoleUserXStruct(1, 3, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), StructBuilder.newRoleUserXStruct(2, 1, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionStruct> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, PermissionStruct> result = () -> {
                Map<IdType, PermissionStruct> map = new HashMap<>();
                map.put(IdBuilder.id(718), StructBuilder.newPermission(718, 18, defaultTId, 12, "18-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(719), StructBuilder.newPermission(719, 19, defaultTId, 12, "19-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(1000), StructBuilder.newPermission(1000, 100, defaultTId, 11, "1000-remark", 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXStruct> createMultipleRolePermissionXLoader(ApplicationIdType applicationId,
                                                                                              TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RolePermissionXStruct> result = () -> {
                Map<IdType, RolePermissionXStruct> map = new HashMap<>();
                map.put(IdBuilder.id(85), StructBuilder.newRolePermissionX(85, 7, 719, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleStruct> createMultipleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleStruct> result = () -> {
                Map<IdType, RoleStruct> map = new HashMap<>();
                map.put(IdBuilder.id(7), StructBuilder.newRoleStruct(7, defaultAId, defaultTId, "zzzRole", true, "zzzRole", true, null, 12, "role7-outerObjectRemark", "zzz-description", 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(61), StructBuilder.newRoleUserGroupXStruct(61, 7, 7, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
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
            SourceLoader<IdType, PermissionTemplateStruct> result;
            result = () -> {
                Map<IdType, PermissionTemplateStruct> map = new HashMap<>();

                String pretext = NativePermissionTemplateEnum.CREATE_PERMISSION_TEMPLATE.getCode();
                map.put(IdBuilder.id(1), StructBuilder.newPermissionTemplateStruct(1, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.INIT_TENANT.getCode();
                map.put(IdBuilder.id(2), StructBuilder.newPermissionTemplateStruct(2, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION.getCode();
                map.put(IdBuilder.id(3), StructBuilder.newPermissionTemplateStruct(3, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.CREATE_OUTER_GROUP.getCode();
                map.put(IdBuilder.id(4), StructBuilder.newPermissionTemplateStruct(4, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.CREATE_INNER_GROUP.getCode();
                map.put(IdBuilder.id(5), StructBuilder.newPermissionTemplateStruct(5, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.CREATE_ROLE.getCode();
                map.put(IdBuilder.id(6), StructBuilder.newPermissionTemplateStruct(6, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                map.put(IdBuilder.id(14), StructBuilder.newPermissionTemplateStruct(14, defaultAId, "p-14-synchronism", false, "p-14-name", "p-14-desc", PermissionLevelEnum.READ.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(15), StructBuilder.newPermissionTemplateStruct(15, defaultAId, "p-15-del", false, "p-15-name", "p-15-desc", PermissionLevelEnum.READ.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(16), StructBuilder.newPermissionTemplateStruct(16, defaultAId, "p-16-add", false, "p-16-name", "p-16-desc", PermissionLevelEnum.READ.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(17), StructBuilder.newPermissionTemplateStruct(17, defaultAId, "p-17-edit", false, "p-17-name", "p-17-desc", PermissionLevelEnum.READ.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(18), StructBuilder.newPermissionTemplateStruct(18, defaultAId, "p-18-del-org", true, "p-18-name", "p-18-desc", PermissionLevelEnum.READ.getCode(), "permission_type", 2,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(19), StructBuilder.newPermissionTemplateStruct(19, defaultAId, "p-19-update-org", true, "p-19-name", "p-19-desc", PermissionLevelEnum.READ.getCode(), "permission_type", 2,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(100), StructBuilder.newPermissionTemplateStruct(100, defaultAId, "p-100-add-org", true, "p-100-name", "p-100-desc", PermissionLevelEnum.READ.getCode(), "permission_type", 2,
                        1, new Date(), 1, new Date(), 1));
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
            map.put(IdBuilder.id(1), StructBuilder.newOuterObjectTypeStruct(1, "AAAOutGroup", "AAA外部组", "AAA外部组", true, false, 1, new Date(), 1, new Date()));
            map.put(IdBuilder.id(2), StructBuilder.newOuterObjectTypeStruct(2, "MyOrganization", "My组织", "My组织", false, true, 1, new Date(), 1, new Date()));
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
        } else if (StrictUtils.equals(outerObjectTypeId, IdBuilder.id(outerObjectTypeId2))) {
            SourceLoader<IdType, OuterObjectStruct> result = () -> {
                Map<IdType, OuterObjectStruct> map = new HashMap<>();
                map.put(IdBuilder.id(11), StructBuilder.newOuterObjectStruct(11, outerObjectTypeId2, "ORG001", "8001", "ORG001", null, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(12), StructBuilder.newOuterObjectStruct(12, outerObjectTypeId2, "ORG002", "8002", "ORG002", null, 1, new Date(), 1, new Date()));
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
                return map;
            };
            return result;
        }
        return null;
    }
}
