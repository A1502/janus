package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer1.*;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.Map;
import java.util.Set;

public abstract class ProxyAccessControlCacheProvider extends BaseAccessControlCacheProvider {

    protected BaseAccessControlCacheProvider provider;

    protected void setProvider(BaseAccessControlCacheProvider provider) {
        this.provider = provider;
    }

    @Override
    protected Map<ApplicationIdType, Set<TenantIdType>> loadApplicationIdTenantIdRange() {
        return this.provider.loadApplicationIdTenantIdRange();
    }

    //1
    @Override
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createScopeSingleRoleUserXLoader(applicationId, tenantId);
    }

    //2
    @Override
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createScopeMultipleRoleUserXLoader(applicationId, tenantId);
    }

    //3
    @Override
    protected SourceLoader<IdType, ScopeUserGroupUserXStruct> createScopeUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createScopeUserGroupUserXLoader(applicationId, tenantId);
    }

    //4
    @Override
    protected SourceLoader<IdType, UserGroupStruct> createUserGroupLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createUserGroupLoader(applicationId, tenantId);
    }

    //5
    @Override
    protected SourceLoader<IdType, UserGroupUserXStruct> createUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createUserGroupUserXLoader(applicationId, tenantId);
    }

    //6
    @Override
    protected SourceLoader<IdType, UserGroupOtherXStruct> createUserGroupOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createUserGroupOtherXLoader(applicationId, tenantId);
    }

    //7
    @Override
    protected SourceLoader<IdType, PermissionStruct> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createSinglePermissionLoader(applicationId, tenantId);
    }

    //8
    @Override
    protected SourceLoader<IdType, RolePermissionXStruct> createSingleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createSingleRolePermissionXLoader(applicationId, tenantId);
    }

    //9
    @Override
    protected SourceLoader<IdType, RoleStruct> createSingleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createSingleRoleLoader(applicationId, tenantId);
    }

    //10
    @Override
    protected SourceLoader<IdType, RoleOtherXStruct> createSingleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createSingleRoleOtherXLoader(applicationId, tenantId);
    }

    //11
    @Override
    protected SourceLoader<IdType, RoleUserGroupXStruct> createSingleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createSingleRoleUserGroupXLoader(applicationId, tenantId);
    }

    //12
    @Override
    protected SourceLoader<IdType, RoleUserXStruct> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createSingleRoleUserXLoader(applicationId, tenantId);
    }

    //13
    @Override
    protected SourceLoader<IdType, PermissionStruct> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createMultiplePermissionLoader(applicationId, tenantId);
    }

    //14
    @Override
    protected SourceLoader<IdType, RolePermissionXStruct> createMultipleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createMultipleRolePermissionXLoader(applicationId, tenantId);
    }

    //15
    @Override
    protected SourceLoader<IdType, RoleStruct> createMultipleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createMultipleRoleLoader(applicationId, tenantId);
    }

    //16
    @Override
    protected SourceLoader<IdType, RoleOtherXStruct> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createMultipleRoleOtherXLoader(applicationId, tenantId);
    }

    //17
    @Override
    protected SourceLoader<IdType, RoleUserGroupXStruct> createMultipleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createMultipleRoleUserGroupXLoader(applicationId, tenantId);
    }

    //18
    @Override
    protected SourceLoader<IdType, RoleUserXStruct> createMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return this.provider.createMultipleRoleUserXLoader(applicationId, tenantId);
    }

    //19
    @Override
    protected SourceLoader<IdType, PermissionTemplateStruct> createPermissionTemplateLoader(ApplicationIdType applicationId) {
        return this.provider.createPermissionTemplateLoader(applicationId);
    }

    //20
    @Override
    protected SourceLoader<IdType, OuterObjectTypeStruct> createOuterObjectTypeLoader() {
        return this.provider.createOuterObjectTypeLoader();
    }

    //21
    @Override
    protected SourceLoader<IdType, OuterObjectStruct> createOuterObjectLoader(IdType outerObjectTypeId) {
        return this.provider.createOuterObjectLoader(outerObjectTypeId);
    }

    //22
    @Override
    protected SourceLoader<IdType, UserOuterObjectXStruct> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        return this.provider.createUserOuterObjectXLoader(outerObjectTypeId);
    }
}
