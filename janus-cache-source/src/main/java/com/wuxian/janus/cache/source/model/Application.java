package com.wuxian.janus.cache.source.model;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.item.ApplicationItem;
import com.wuxian.janus.core.PermissionTemplateUtils;
import com.wuxian.janus.core.critical.CoverageTypeEnum;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.layer1.RoleStruct;
import com.wuxian.janus.struct.layer1.UserGroupStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

    @Getter
    @Setter
    protected String id;

    //application role

    @Getter
    private String allPermissionRoleId = null;

    @Getter
    private String applicationMaintainerRoleId = null;

    //application userGroup

    @Getter
    private String applicationRootUserGroupId = null;

    @Getter
    private String applicationAdminUserGroupId = null;

    //application permission

    @Getter
    private PairId createPermissionTemplatePair = null;

    @Getter
    private PairId initTenantPair = null;

    //tenant permission

    @Getter
    private String createMultiplePermissionTemplateId;

    @Getter
    private String createOuterGroupTemplateId;

    @Getter
    private String createRoleTemplateId;

    @Getter
    private String createInnerGroupTemplateId;

    @Getter
    private List<PermissionTemplate> permissionTemplates = new ArrayList<>();

    @Getter
    private List<Tenant> tenants = new ArrayList<>();

    //---------------------------------------------------------------------------------------------------------------------------------
    protected Application() {
    }

    public static Application byId(String id) {
        Application result = new Application();
        result.id = id;
        return result;
    }
    //---------------------------------------------------------------------------------------------------------------------------------

    public Application addItem(ApplicationItem... items) {

        for (ApplicationItem item : items) {
            if (item instanceof PermissionTemplate) {
                permissionTemplates.add((PermissionTemplate) item);
            } else if (item instanceof Tenant) {
                tenants.add((Tenant) item);
            } else {
                throw ErrorFactory.createIllegalItemTypeError(item.getClass().toString());
            }
        }
        return this;
    }

    public Application setNative(NativeRoleEnum role, String id) {
        if (role == NativeRoleEnum.ALL_PERMISSION) {
            this.allPermissionRoleId = id;
        } else if (role == NativeRoleEnum.APPLICATION_MAINTAINER) {
            this.applicationMaintainerRoleId = id;
        } else {
            throw ErrorFactory.createIllegalNativeCoverageError(CoverageTypeEnum.APPLICATION, role.toString());
        }
        return this;
    }

    public Application setNative(NativeUserGroupEnum userGroup, String id) {
        if (userGroup == NativeUserGroupEnum.APPLICATION_ROOT) {
            this.applicationRootUserGroupId = id;
        } else if (userGroup == NativeUserGroupEnum.APPLICATION_ADMIN) {
            this.applicationAdminUserGroupId = id;
        } else {
            throw ErrorFactory.createIllegalNativeCoverageError(CoverageTypeEnum.APPLICATION,
                    userGroup.getCode());
        }
        return this;
    }

    public Application setNative(NativePermissionTemplateEnum template, String permissionId, String templateId) {
        if (template == NativePermissionTemplateEnum.CREATE_PERMISSION_TEMPLATE) {
            this.createPermissionTemplatePair = new PairId(permissionId, templateId);
        } else if (template == NativePermissionTemplateEnum.INIT_TENANT) {
            this.initTenantPair = new PairId(permissionId, templateId);
        } else {
            throw ErrorFactory.createIllegalNativeCoverageError(CoverageTypeEnum.APPLICATION,
                    template.getCode());
        }
        return this;
    }

    public Application setNative(NativePermissionTemplateEnum template, String templateId) {
        if (template == NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION) {
            this.createMultiplePermissionTemplateId = templateId;
        } else if (template == NativePermissionTemplateEnum.CREATE_OUTER_GROUP) {
            this.createOuterGroupTemplateId = templateId;
        } else if (template == NativePermissionTemplateEnum.CREATE_ROLE) {
            this.createRoleTemplateId = templateId;
        } else if (template == NativePermissionTemplateEnum.CREATE_INNER_GROUP) {
            this.createInnerGroupTemplateId = templateId;
        } else {
            throw ErrorFactory.createIllegalNativeCoverageError(CoverageTypeEnum.APPLICATION, template.getCode());
        }
        return this;
    }

    private void completeNativePermissionTemplateId(IdGenerator templateIdGenerator) {
        //记录被占用的id
        List<IdType> used = new ArrayList<>();

        if (this.createPermissionTemplatePair == null) {
            this.createPermissionTemplatePair = new PairId(null, templateIdGenerator.generate().toString());
        } else {
            if (this.createPermissionTemplatePair.getTemplateId() == null) {
                this.createPermissionTemplatePair.setTemplateId(templateIdGenerator.generate().toString());
            } else {
                used.add(IdUtils.createId(this.createPermissionTemplatePair.getTemplateId()));
            }
        }

        if (this.initTenantPair == null) {
            this.initTenantPair = new PairId(null, templateIdGenerator.generate().toString());
        } else {
            if (this.initTenantPair.getTemplateId() == null) {
                this.initTenantPair.setTemplateId(templateIdGenerator.generate().toString());
            } else {
                used.add(IdUtils.createId(this.initTenantPair.getTemplateId()));
            }
        }

        if (createMultiplePermissionTemplateId == null) {
            createMultiplePermissionTemplateId = templateIdGenerator.generate().toString();
        } else {
            used.add(IdUtils.createId(createMultiplePermissionTemplateId));
        }

        if (createOuterGroupTemplateId == null) {
            createOuterGroupTemplateId = templateIdGenerator.generate().toString();
        } else {
            used.add(IdUtils.createId(createOuterGroupTemplateId));
        }

        if (createRoleTemplateId == null) {
            createRoleTemplateId = templateIdGenerator.generate().toString();
        } else {
            used.add(IdUtils.createId(createRoleTemplateId));
        }

        if (createInnerGroupTemplateId == null) {
            createInnerGroupTemplateId = templateIdGenerator.generate().toString();
        } else {
            used.add(IdUtils.createId(createInnerGroupTemplateId));
        }

        templateIdGenerator.addUsed(used);
    }

    public List<PermissionTemplate> buildNativePermissionTemplate(IdGenerator templateIdGenerator) {
        completeNativePermissionTemplateId(templateIdGenerator);
        ApplicationIdType applicationId = IdUtils.createApplicationId(this.id);

        //CREATE_PERMISSION_TEMPLATE
        PermissionTemplateStruct pt0 = PermissionTemplateUtils
                .getNativePermissionTemplate(NativePermissionTemplateEnum.CREATE_PERMISSION_TEMPLATE);
        pt0.setApplicationId(applicationId.getValue());
        pt0.setId(IdUtils.createId(createPermissionTemplatePair.getTemplateId()).getValue());

        //INIT_TENANT
        PermissionTemplateStruct pt1 = PermissionTemplateUtils
                .getNativePermissionTemplate(NativePermissionTemplateEnum.INIT_TENANT);
        pt1.setApplicationId(applicationId.getValue());
        pt1.setId(IdUtils.createId(initTenantPair.getTemplateId()).getValue());

        //CREATE_MULTIPLE_PERMISSION
        PermissionTemplateStruct pt2 = PermissionTemplateUtils
                .getNativePermissionTemplate(NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION);
        pt2.setApplicationId(applicationId.getValue());
        pt2.setId(IdUtils.createId(createMultiplePermissionTemplateId).getValue());

        //CREATE_OUTER_GROUP
        PermissionTemplateStruct pt3 = PermissionTemplateUtils
                .getNativePermissionTemplate(NativePermissionTemplateEnum.CREATE_OUTER_GROUP);
        pt3.setApplicationId(applicationId.getValue());
        pt3.setId(IdUtils.createId(createOuterGroupTemplateId).getValue());

        //CREATE_ROLE
        PermissionTemplateStruct pt4 = PermissionTemplateUtils
                .getNativePermissionTemplate(NativePermissionTemplateEnum.CREATE_ROLE);
        pt4.setApplicationId(applicationId.getValue());
        pt4.setId(IdUtils.createId(createRoleTemplateId).getValue());

        //CREATE_INNER_GROUP
        PermissionTemplateStruct pt5 = PermissionTemplateUtils
                .getNativePermissionTemplate(NativePermissionTemplateEnum.CREATE_INNER_GROUP);
        pt5.setApplicationId(applicationId.getValue());
        pt5.setId(IdUtils.createId(createInnerGroupTemplateId).getValue());

        return Stream.of(pt0, pt1, pt2, pt3, pt4, pt5).map(PermissionTemplate::byStruct).collect(Collectors.toList());
    }

    public List<Permission> buildNativeApplicationPermission(IdGenerator templateIdGenerator) {
        completeNativePermissionTemplateId(templateIdGenerator);

        //CREATE_PERMISSION_TEMPLATE
        PermissionStruct p0 = new PermissionStruct();
        p0.setPermissionTemplateId(IdUtils.createId(this.createPermissionTemplatePair.getTemplateId()).getValue());
        if (createPermissionTemplatePair.getPermissionId() != null) {
            p0.setId(IdUtils.createId(createPermissionTemplatePair.getPermissionId()).getValue());
        }
        Permission model0 = Permission.byStruct(NativePermissionTemplateEnum.CREATE_PERMISSION_TEMPLATE.getCode(), p0);

        //INIT_TENANT
        PermissionStruct p1 = new PermissionStruct();
        p1.setPermissionTemplateId(IdUtils.createId(this.initTenantPair.getTemplateId()).getValue());
        if (initTenantPair.getPermissionId() != null) {
            p1.setId(IdUtils.createId(initTenantPair.getPermissionId()).getValue());
        }
        Permission model1 = Permission.byStruct(NativePermissionTemplateEnum.INIT_TENANT.getCode(), p1);

        return Arrays.asList(model0, model1);
    }

    public List<Permission> buildNativeTenantPermission(TenantIdType tenantId, IdGenerator templateIdGenerator) {
        completeNativePermissionTemplateId(templateIdGenerator);

        //CREATE_PERMISSION_TEMPLATE
        PermissionStruct p0 = new PermissionStruct();
        p0.setTenantId(tenantId.getValue());
        p0.setPermissionTemplateId(IdUtils.createId(this.createMultiplePermissionTemplateId).getValue());
        Permission model0 = Permission.byStruct(NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION.getCode(), p0);

        //CREATE_OUTER_GROUP
        PermissionStruct p1 = new PermissionStruct();
        p1.setTenantId(tenantId.getValue());
        p1.setPermissionTemplateId(IdUtils.createId(this.createOuterGroupTemplateId).getValue());
        Permission model1 = Permission.byStruct(NativePermissionTemplateEnum.CREATE_OUTER_GROUP.getCode(), p1);

        //CREATE_ROLE
        PermissionStruct p2 = new PermissionStruct();
        p2.setTenantId(tenantId.getValue());
        p2.setPermissionTemplateId(IdUtils.createId(this.createRoleTemplateId).getValue());
        Permission model2 = Permission.byStruct(NativePermissionTemplateEnum.CREATE_ROLE.getCode(), p2);

        //CREATE_INNER_GROUP
        PermissionStruct p3 = new PermissionStruct();
        p3.setTenantId(tenantId.getValue());
        p3.setPermissionTemplateId(IdUtils.createId(this.createInnerGroupTemplateId).getValue());
        Permission model3 = Permission.byStruct(NativePermissionTemplateEnum.CREATE_INNER_GROUP.getCode(), p3);

        return Arrays.asList(model0, model1, model2, model3);
    }

    public List<Role> buildNativeApplicationRole() {

        ApplicationIdType applicationId = IdUtils.createApplicationId(this.id);

        RoleStruct r0 = new RoleStruct();
        r0.setCode(NativeRoleEnum.ALL_PERMISSION.getCode());
        r0.setApplicationId(applicationId.getValue());
        if (allPermissionRoleId != null) {
            r0.setId(IdUtils.createId(allPermissionRoleId).getValue());
        }

        RoleStruct r1 = new RoleStruct();
        r1.setCode(NativeRoleEnum.APPLICATION_MAINTAINER.getCode());
        r1.setApplicationId(applicationId.getValue());
        if (applicationMaintainerRoleId != null) {
            r1.setId(IdUtils.createId(applicationMaintainerRoleId).getValue());
        }

        return Stream.of(r0, r1).map(Role::byStruct).collect(Collectors.toList());
    }

    public List<UserGroup> buildNativeApplicationUserGroup() {
        ApplicationIdType applicationId = IdUtils.createApplicationId(this.id);

        UserGroupStruct ug0 = new UserGroupStruct();
        ug0.setCode(NativeUserGroupEnum.APPLICATION_ROOT.getCode());
        ug0.setApplicationId(applicationId.getValue());
        if (applicationRootUserGroupId != null) {
            ug0.setId(IdUtils.createId(applicationRootUserGroupId).getValue());
        }

        UserGroupStruct ug1 = new UserGroupStruct();
        ug1.setCode(NativeUserGroupEnum.APPLICATION_ADMIN.getCode());
        ug1.setApplicationId(applicationId.getValue());
        if (applicationAdminUserGroupId != null) {
            ug1.setId(IdUtils.createId(applicationAdminUserGroupId).getValue());
        }

        return Stream.of(ug0, ug1).map(UserGroup::byStruct).collect(Collectors.toList());
    }
}
