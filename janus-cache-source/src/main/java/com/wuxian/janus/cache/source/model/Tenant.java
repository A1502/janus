package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.item.ApplicationItem;
import com.wuxian.janus.cache.source.model.item.TenantItem;
import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.core.critical.CoverageTypeEnum;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer1.RoleStruct;
import com.wuxian.janus.struct.layer1.UserGroupStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tenant implements ApplicationItem {

    @Getter
    @Setter
    protected String id;

    //tenant role

    @Getter
    private String tenantDataOwnerRoleId = null;

    @Getter
    private String tenantMaintainerRoleId = null;

    //tenant userGroup

    @Getter
    private String tenantRootUserGroupId = null;

    //tenant permission

    @Getter
    private String createMultiplePermissionPId;

    @Getter
    private String createOuterGroupPId;

    @Getter
    private String createRolePId;

    @Getter
    private String createInnerGroupPId;

    @Getter
    private List<Role> roles = new ArrayList<>();

    @Getter
    private List<UserGroup> userGroups = new ArrayList<>();

    @Getter
    private List<Permission> permissions = new ArrayList<>();
    //---------------------------------------------------------------------------------------------------------------------------------
    protected Tenant() {
    }

    public static Tenant byId(String id) {
        if (id == null) {
            throw ErrorFactory.createCodeCannotBeNullError();
        }
        Tenant result = new Tenant();
        result.id = id;
        return result;
    }
    //---------------------------------------------------------------------------------------------------------------------------------
    public Tenant addItem(TenantItem... items) {

        for (TenantItem item : items) {
            if (item instanceof Role) {
                roles.add((Role) item);
            } else if (item instanceof UserGroup) {
                userGroups.add((UserGroup) item);
            } else if (item instanceof Permission) {
                permissions.add((Permission) item);
            } else {
                throw ErrorFactory.createIllegalItemTypeError(item.getClass().toString());
            }
        }
        return this;
    }

    public Tenant setNative(NativeRoleEnum role, String id) {
        if (role == NativeRoleEnum.TENANT_DATA_OWNER) {
            this.tenantDataOwnerRoleId = id;
        } else if (role == NativeRoleEnum.TENANT_MAINTAINER) {
            this.tenantMaintainerRoleId = id;
        } else {
            throw ErrorFactory.createIllegalNativeCoverageError(CoverageTypeEnum.TENANT, role.toString());
        }
        return this;
    }

    public Tenant setNative(NativeUserGroupEnum userGroup, String id) {
        if (userGroup == NativeUserGroupEnum.TENANT_ROOT) {
            this.tenantRootUserGroupId = id;
        } else {
            throw ErrorFactory.createIllegalNativeCoverageError(CoverageTypeEnum.TENANT, userGroup.toString());
        }
        return this;
    }

    public Tenant setNative(NativePermissionTemplateEnum template, String permissionId) {
        if (template == NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION) {
            this.createMultiplePermissionPId = permissionId;
        } else if (template == NativePermissionTemplateEnum.CREATE_OUTER_GROUP) {
            this.createOuterGroupPId = permissionId;
        } else if (template == NativePermissionTemplateEnum.CREATE_ROLE) {
            this.createRolePId = permissionId;
        } else if (template == NativePermissionTemplateEnum.CREATE_INNER_GROUP) {
            this.createInnerGroupPId = permissionId;
        } else {
            throw ErrorFactory.createIllegalNativeCoverageError(CoverageTypeEnum.TENANT, template.toString());
        }
        return this;
    }

    public List<Permission> buildNativeTenantPermission() {

        TenantIdType tenantId = IdUtils.createTenantId(this.id);

        //CREATE_PERMISSION_TEMPLATE
        PermissionStruct p0 = new PermissionStruct();
        p0.setTenantId(tenantId.getValue());
        Permission model0 = new Permission(NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION);
        model0.setStruct(p0);
        model0.setId(this.createMultiplePermissionPId);

        //CREATE_OUTER_GROUP
        PermissionStruct p1 = new PermissionStruct();
        p1.setTenantId(tenantId.getValue());
        Permission model1 = new Permission(NativePermissionTemplateEnum.CREATE_OUTER_GROUP);
        model1.setStruct(p1);
        model1.setId(this.createOuterGroupPId);

        //CREATE_ROLE
        PermissionStruct p2 = new PermissionStruct();
        p2.setTenantId(tenantId.getValue());
        Permission model2 = new Permission(NativePermissionTemplateEnum.CREATE_ROLE);
        model2.setStruct(p2);
        model2.setId(this.createRolePId);

        //CREATE_INNER_GROUP
        PermissionStruct p3 = new PermissionStruct();
        p3.setTenantId(tenantId.getValue());
        Permission model3 = new Permission(NativePermissionTemplateEnum.CREATE_INNER_GROUP);
        model3.setStruct(p3);
        model3.setId(this.createInnerGroupPId);

        return Arrays.asList(model0, model1, model2, model3);

    }

    public List<Role> buildNativeTenantRole(ApplicationIdType applicationId) {
        TenantIdType tenantId = IdUtils.createTenantId(this.id);

        RoleStruct r0 = new RoleStruct();
        r0.setCode(NativeRoleEnum.TENANT_DATA_OWNER.getCode());
        r0.setApplicationId(applicationId.getValue());
        r0.setTenantId(tenantId.getValue());
        if (tenantDataOwnerRoleId != null) {
            r0.setId(IdUtils.createId(tenantDataOwnerRoleId).getValue());
        }

        RoleStruct r1 = new RoleStruct();
        r1.setCode(NativeRoleEnum.TENANT_MAINTAINER.getCode());
        r1.setApplicationId(applicationId.getValue());
        r1.setTenantId(tenantId.getValue());
        if (tenantMaintainerRoleId != null) {
            r1.setId(IdUtils.createId(tenantMaintainerRoleId).getValue());
        }

        return Stream.of(r0, r1).map(Role::byStruct).collect(Collectors.toList());
    }

    public List<UserGroup> buildNativeTenantUserGroup(ApplicationIdType applicationId) {

        TenantIdType tenantId = IdUtils.createTenantId(this.id);

        UserGroupStruct ug0 = new UserGroupStruct();
        ug0.setCode(NativeUserGroupEnum.TENANT_ROOT.getCode());
        ug0.setApplicationId(applicationId.getValue());
        ug0.setTenantId(tenantId.getValue());
        if (tenantRootUserGroupId != null) {
            ug0.setId(IdUtils.createId(tenantRootUserGroupId).getValue());
        }

        return Stream.of(ug0).map(UserGroup::byStruct).collect(Collectors.toList());
    }
}
