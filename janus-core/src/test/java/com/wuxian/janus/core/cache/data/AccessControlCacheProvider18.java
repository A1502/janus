package com.wuxian.janus.core.cache.data;

import com.wuxian.janus.EntityBuilder;
import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.core.cache.provider.BaseAccessControlCacheProvider;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.critical.PermissionLevelEnum;
import com.wuxian.janus.entity.*;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;

import java.util.*;

public class AccessControlCacheProvider18 extends BaseAccessControlCacheProvider {

    //等号左边：OuterObject，UserGroup，Role
    //等号右边：Permission，Role，User


    //【主数据】
    //OuterObject(12,type:2)
    //OuterObject(10,type:1) = User(1)

    //UserGroup(6,outer:10) = Role(5)
    //UserGroup(7) = User(1),Role(6),Role(7)

    //Role(1,ALL_PERMISSION) = User(1)
    //Role(3,TENANT_MAINTAINER) = Permission(715,template:15),Permission(205,template:5),Permission(206,template:6),User(1)
    //Role(5) = Permission(715,template:15)(716,template:16)
    //Role(6) = Permission(716,template:16)(717,template:17)
    //Role(7,outer:12) = Permission(719,outer:12,template:19)

    //【陪衬数据】
    //4个NativeRole，6个NativePermission
    //OuterObject(11,type:2)
    //Permission(714,template:14)
    //Permission(718,outer:12,template:18)
    //Permission(1000,outer:11,template:100)

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
    protected SourceLoader<IdType, ScopeRoleUserXEntity> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, ScopeRoleUserXEntity> result = () -> {
                Map<IdType, ScopeRoleUserXEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newScopeRoleUserX(1, null, 1, 1, 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(3), EntityBuilder.newScopeRoleUserX(3, null, 3, 1, 1, new Date(), 1, new Date()));
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
                map.put(IdBuilder.id(1), EntityBuilder.newScopeUserGroupUserXEntity(1, null, 7, 1, 1, new Date(), 1, new Date()));
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
                map.put(IdBuilder.id(6), EntityBuilder.newUserGroupEntity(6, defaultAId, defaultTId, "stdUG", "stdUG", true, "stdUG", 10, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(7), EntityBuilder.newUserGroupEntity(7, defaultAId, defaultTId, "bbbUG", "bbbUG", true, "stdUG", null, 1, new Date(), 1, new Date(), 1));
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
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, PermissionEntity> result = () -> {
                Map<IdType, PermissionEntity> map = new HashMap<>();

                map.put(IdBuilder.id(1), EntityBuilder.newPermission(1, 1, null, null, null, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newPermission(2, 2, null, null, null, 1, new Date(), 1, new Date(), 1));

                map.put(IdBuilder.id(714), EntityBuilder.newPermission(714, 14, defaultTId, null, "14-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(715), EntityBuilder.newPermission(715, 15, defaultTId, null, "15-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(716), EntityBuilder.newPermission(716, 16, defaultTId, null, "16-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(717), EntityBuilder.newPermission(717, 17, defaultTId, null, "17-remark", 1, new Date(), 1, new Date(), 1));

                map.put(IdBuilder.id(205), EntityBuilder.newPermission(205, 5, defaultTId, null, "17-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(206), EntityBuilder.newPermission(206, 6, defaultTId, null, "17-remark", 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(80), EntityBuilder.newRolePermissionX(80, 3, 715, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(81), EntityBuilder.newRolePermissionX(81, 5, 715, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(82), EntityBuilder.newRolePermissionX(82, 5, 716, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(83), EntityBuilder.newRolePermissionX(83, 6, 716, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(84), EntityBuilder.newRolePermissionX(84, 6, 717, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, RoleEntity> createSingleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, RoleEntity> result = () -> {
                Map<IdType, RoleEntity> map = new HashMap<>();
                map.put(IdBuilder.id(1), EntityBuilder.newRoleEntity(1, defaultAId, null, NativeRoleEnum.ALL_PERMISSION.getCode(), NativeRoleEnum.ALL_PERMISSION.getCode(), true, null, null, null, NativeRoleEnum.ALL_PERMISSION.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleEntity(2, defaultAId, null, NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), true, null, null, null, NativeRoleEnum.APPLICATION_MAINTAINER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(3), EntityBuilder.newRoleEntity(3, defaultAId, defaultTId, NativeRoleEnum.TENANT_MAINTAINER.getCode(), NativeRoleEnum.TENANT_MAINTAINER.getCode(), true, null, null, null, NativeRoleEnum.TENANT_MAINTAINER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(4), EntityBuilder.newRoleEntity(4, defaultAId, defaultTId, NativeRoleEnum.TENANT_DATA_OWNER.getCode(), NativeRoleEnum.TENANT_DATA_OWNER.getCode(), true, null, null, null, NativeRoleEnum.TENANT_DATA_OWNER.getCode(), 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(5), EntityBuilder.newRoleEntity(5, defaultAId, defaultTId, "xxxRole", "xxxRole", true, null, null, "role5-outerObjectRemark", "draft-description", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(6), EntityBuilder.newRoleEntity(6, defaultAId, defaultTId, "yyyRole", "yyyRole", true, null, null, "role6-outerObjectRemark", "yyy-description", 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(62), EntityBuilder.newRoleUserGroupXEntity(62, 5, 6, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(63), EntityBuilder.newRoleUserGroupXEntity(63, 6, 7, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(1), EntityBuilder.newRoleUserXEntity(1, 3, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(2), EntityBuilder.newRoleUserXEntity(2, 1, 1, true, true, false, false, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
                return map;
            };
            return result;
        }
        return null;
    }

    @Override
    protected SourceLoader<IdType, PermissionEntity> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (StrictUtils.equals(applicationId, IdBuilder.aId(defaultAId)) && StrictUtils.equals(tenantId, IdBuilder.tId(defaultTId))) {
            SourceLoader<IdType, PermissionEntity> result = () -> {
                Map<IdType, PermissionEntity> map = new HashMap<>();
                map.put(IdBuilder.id(718), EntityBuilder.newPermission(718, 18, defaultTId, 12, "18-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(719), EntityBuilder.newPermission(719, 19, defaultTId, 12, "19-remark", 1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(1000), EntityBuilder.newPermission(1000, 100, defaultTId, 11, "1000-remark", 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(85), EntityBuilder.newRolePermissionX(85, 7, 719, 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(7), EntityBuilder.newRoleEntity(7, defaultAId, defaultTId, "zzzRole", "zzzRole", true, null, 12, "role7-outerObjectRemark", "zzz-description", 1, new Date(), 1, new Date(), 1));
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
                map.put(IdBuilder.id(61), EntityBuilder.newRoleUserGroupXEntity(61, 7, 7, true, true, true, true, false, false, false, false, false, false, 1, new Date(), 1, new Date(), 1));
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
            SourceLoader<IdType, PermissionTemplateEntity> result;
            result = () -> {
                Map<IdType, PermissionTemplateEntity> map = new HashMap<>();

                String pretext = NativePermissionTemplateEnum.CREATE_PERMISSION_TEMPLATE.getCode();
                map.put(IdBuilder.id(1), EntityBuilder.newPermissionTemplateEntity(1, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.INIT_TENANT.getCode();
                map.put(IdBuilder.id(2), EntityBuilder.newPermissionTemplateEntity(2, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION.getCode();
                map.put(IdBuilder.id(3), EntityBuilder.newPermissionTemplateEntity(3, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.CREATE_OUTER_GROUP.getCode();
                map.put(IdBuilder.id(4), EntityBuilder.newPermissionTemplateEntity(4, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.CREATE_INNER_GROUP.getCode();
                map.put(IdBuilder.id(5), EntityBuilder.newPermissionTemplateEntity(5, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                pretext = NativePermissionTemplateEnum.CREATE_ROLE.getCode();
                map.put(IdBuilder.id(6), EntityBuilder.newPermissionTemplateEntity(6, defaultAId,
                        pretext, false, pretext + "name", pretext + "esc",
                        PermissionLevelEnum.ADVANCED_ADD.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));

                map.put(IdBuilder.id(14), EntityBuilder.newPermissionTemplateEntity(14, defaultAId, "p-14-synchronism", false, "p-14-name", "p-14-desc", PermissionLevelEnum.READ.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(15), EntityBuilder.newPermissionTemplateEntity(15, defaultAId, "p-15-del", false, "p-15-name", "p-15-desc", PermissionLevelEnum.READ.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(16), EntityBuilder.newPermissionTemplateEntity(16, defaultAId, "p-16-add", false, "p-16-name", "p-16-desc", PermissionLevelEnum.READ.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(17), EntityBuilder.newPermissionTemplateEntity(17, defaultAId, "p-17-edit", false, "p-17-name", "p-17-desc", PermissionLevelEnum.READ.getCode(), "permission_type", null,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(18), EntityBuilder.newPermissionTemplateEntity(18, defaultAId, "p-18-del-org", true, "p-18-name", "p-18-desc", PermissionLevelEnum.READ.getCode(), "permission_type", 2,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(19), EntityBuilder.newPermissionTemplateEntity(19, defaultAId, "p-19-update-org", true, "p-19-name", "p-19-desc", PermissionLevelEnum.READ.getCode(), "permission_type", 2,
                        1, new Date(), 1, new Date(), 1));
                map.put(IdBuilder.id(100), EntityBuilder.newPermissionTemplateEntity(100, defaultAId, "p-100-add-org", true, "p-100-name", "p-100-desc", PermissionLevelEnum.READ.getCode(), "permission_type", 2,
                        1, new Date(), 1, new Date(), 1));
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
            map.put(IdBuilder.id(1), EntityBuilder.newOuterObjectTypeEntity(1, "AAAOutGroup", "AAA外部组", "AAA外部组", true, false, 1, new Date(), 1, new Date()));
            map.put(IdBuilder.id(2), EntityBuilder.newOuterObjectTypeEntity(2, "MyOrganization", "My组织", "My组织", false, true, 1, new Date(), 1, new Date()));
            return map;
        };
        return result;
    }

    @Override
    protected SourceLoader<IdType, OuterObjectEntity> createOuterObjectLoader(IdType outerObjectTypeId) {
        if (StrictUtils.equals(outerObjectTypeId, IdBuilder.id(outerObjectTypeId1))) {
            SourceLoader<IdType, OuterObjectEntity> result = () -> {
                Map<IdType, OuterObjectEntity> map = new HashMap<>();
                map.put(IdBuilder.id(10), EntityBuilder.newOuterObjectEntity(10, outerObjectTypeId1, "EMS001", "ref100", "销售", 1, new Date(), 1, new Date()));
                return map;
            };
            return result;
        } else if (StrictUtils.equals(outerObjectTypeId, IdBuilder.id(outerObjectTypeId2))) {
            SourceLoader<IdType, OuterObjectEntity> result = () -> {
                Map<IdType, OuterObjectEntity> map = new HashMap<>();
                map.put(IdBuilder.id(11), EntityBuilder.newOuterObjectEntity(11, outerObjectTypeId2, "ORG001", "ref8001", "ORG001", 1, new Date(), 1, new Date()));
                map.put(IdBuilder.id(12), EntityBuilder.newOuterObjectEntity(12, outerObjectTypeId2, "ORG002", "ref8002", "ORG002", 1, new Date(), 1, new Date()));
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
                return map;
            };
            return result;
        }
        return null;
    }
}
