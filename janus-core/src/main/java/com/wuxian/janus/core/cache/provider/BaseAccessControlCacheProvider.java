package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.core.cache.*;
import com.wuxian.janus.core.index.*;
import com.wuxian.janus.core.synchronism.BaseChangeRecorder;
import com.wuxian.janus.core.synchronism.BaseStatusSynchronizer;
import com.wuxian.janus.entity.*;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;

import java.util.Map;
import java.util.Set;

public abstract class BaseAccessControlCacheProvider {

    protected abstract Map<ApplicationIdType, Set<TenantIdType>> loadApplicationIdTenantIdRange();

    //1
    protected abstract SourceLoader<IdType, ScopeRoleUserXEntity> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //2
    protected abstract SourceLoader<IdType, ScopeRoleUserXEntity> createScopeMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //3
    protected abstract SourceLoader<IdType, ScopeUserGroupUserXEntity> createScopeUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //4
    protected abstract SourceLoader<IdType, UserGroupEntity> createUserGroupLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //5
    protected abstract SourceLoader<IdType, UserGroupUserXEntity> createUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //6
    protected abstract SourceLoader<IdType, PermissionEntity> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //7
    protected abstract SourceLoader<IdType, RolePermissionXEntity> createSingleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //8
    protected abstract SourceLoader<IdType, RoleEntity> createSingleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //9
    protected abstract SourceLoader<IdType, RoleOtherXEntity> createSingleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //10
    protected abstract SourceLoader<IdType, RoleUserGroupXEntity> createSingleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //11
    protected abstract SourceLoader<IdType, RoleUserXEntity> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //12
    protected abstract SourceLoader<IdType, PermissionEntity> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //13
    protected abstract SourceLoader<IdType, RolePermissionXEntity> createMultipleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //14
    protected abstract SourceLoader<IdType, RoleEntity> createMultipleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //15
    protected abstract SourceLoader<IdType, RoleOtherXEntity> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //16
    protected abstract SourceLoader<IdType, RoleUserGroupXEntity> createMultipleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //17
    protected abstract SourceLoader<IdType, RoleUserXEntity> createMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId);

    //18
    protected abstract SourceLoader<IdType, PermissionTemplateEntity> createPermissionTemplateLoader(ApplicationIdType applicationId);

    //19
    protected abstract SourceLoader<IdType, OuterObjectTypeEntity> createOuterObjectTypeLoader();

    //20
    protected abstract SourceLoader<IdType, OuterObjectEntity> createOuterObjectLoader(IdType outerObjectTypeId);

    //21
    protected abstract SourceLoader<IdType, UserOuterObjectXEntity> createUserOuterObjectXLoader(IdType outerObjectTypeId);

    public class TenantCache extends BaseTenantCache {

        TenantCache(ApplicationIdType applicationId, TenantIdType tenantId) {
            super(applicationId, tenantId);
        }

        @Override
        protected ScopeRoleUserXMap createScopeSingleRoleUserXMap() {
            ScopeRoleUserXMap result = new ScopeRoleUserXMap(BaseAccessControlCacheProvider.this.createScopeSingleRoleUserXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected ScopeRoleUserXMap createScopeMultipleRoleUserXMap() {
            ScopeRoleUserXMap result = new ScopeRoleUserXMap(BaseAccessControlCacheProvider.this.createScopeMultipleRoleUserXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected ScopeUserGroupUserXMap createScopeUserGroupUserXMap() {
            ScopeUserGroupUserXMap result = new ScopeUserGroupUserXMap(BaseAccessControlCacheProvider.this.createScopeUserGroupUserXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected UserGroupMap createUserGroupMap() {
            UserGroupMap result = new UserGroupMap(BaseAccessControlCacheProvider.this.createUserGroupLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected UserGroupUserXMap createUserGroupUserXMap() {
            UserGroupUserXMap result = new UserGroupUserXMap(BaseAccessControlCacheProvider.this.createUserGroupUserXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected PermissionMap createSinglePermissionMap() {
            PermissionMap result = new PermissionMap(BaseAccessControlCacheProvider.this.createSinglePermissionLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RolePermissionXMap createSingleRolePermissionXMap() {
            RolePermissionXMap result = new RolePermissionXMap(BaseAccessControlCacheProvider.this.createSingleRolePermissionXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RoleMap createSingleRoleMap() {
            RoleMap result = new RoleMap(BaseAccessControlCacheProvider.this.createSingleRoleLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RoleOtherXMap createSingleRoleOtherXMap() {
            RoleOtherXMap result = new RoleOtherXMap(BaseAccessControlCacheProvider.this.createSingleRoleOtherXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RoleUserGroupXMap createSingleRoleUserGroupXMap() {
            RoleUserGroupXMap result = new RoleUserGroupXMap(BaseAccessControlCacheProvider.this.createSingleRoleUserGroupXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RoleUserXMap createSingleRoleUserXMap() {
            RoleUserXMap result = new RoleUserXMap(BaseAccessControlCacheProvider.this.createSingleRoleUserXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected PermissionMap createMultiplePermissionMap() {
            PermissionMap result = new PermissionMap(BaseAccessControlCacheProvider.this.createMultiplePermissionLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RolePermissionXMap createMultipleRolePermissionXMap() {
            RolePermissionXMap result = new RolePermissionXMap(BaseAccessControlCacheProvider.this.createMultipleRolePermissionXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RoleMap createMultipleRoleEntityMap() {
            RoleMap result = new RoleMap(BaseAccessControlCacheProvider.this.createMultipleRoleLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RoleOtherXMap createMultipleRoleOtherXMap() {
            RoleOtherXMap result = new RoleOtherXMap(BaseAccessControlCacheProvider.this.createMultipleRoleOtherXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RoleUserGroupXMap createMultipleRoleUserGroupXMap() {
            RoleUserGroupXMap result = new RoleUserGroupXMap(BaseAccessControlCacheProvider.this.createMultipleRoleUserGroupXLoader(applicationId, tenantId));
            return result;
        }

        @Override
        protected RoleUserXMap createMultipleRoleUserXMap() {
            RoleUserXMap result = new RoleUserXMap(BaseAccessControlCacheProvider.this.createMultipleRoleUserXLoader(applicationId, tenantId));
            return result;
        }
    }

    public class ApplicationCache extends BaseApplicationCache {
        ApplicationCache(ApplicationIdType applicationId) {
            super(applicationId);
        }

        @Override
        protected PermissionTemplateMap createPermissionTemplateMap() {
            PermissionTemplateMap result = new PermissionTemplateMap(BaseAccessControlCacheProvider.this.createPermissionTemplateLoader(this.applicationId));
            return result;
        }

        @Override
        protected BaseTenantCache createTenantCache(ApplicationIdType applicationId, TenantIdType tenantId) {
            return new TenantCache(applicationId, tenantId);
        }
    }

    public class ApplicationCachePool extends BaseApplicationCachePool {

        @Override
        protected BaseApplicationCache createApplicationCache(ApplicationIdType applicationId) {
            return new ApplicationCache(applicationId);
        }
    }

    public class OuterObjectTypeCache extends BaseOuterObjectTypeCache {

        OuterObjectTypeCache(IdType outerObjectTypeId) {
            super(outerObjectTypeId);
        }

        @Override
        protected OuterObjectMap createOuterObjectMap() {
            OuterObjectMap result =
                    new OuterObjectMap(BaseAccessControlCacheProvider.this.createOuterObjectLoader(this.getOuterObjectTypeId()));
            return result;
        }

        @Override
        protected UserOuterObjectXMap createUserOuterObjectXMap() {
            UserOuterObjectXMap result =
                    new UserOuterObjectXMap(BaseAccessControlCacheProvider.this.createUserOuterObjectXLoader(this.getOuterObjectTypeId()));
            return result;
        }
    }

    public class OuterObjectCachePool extends BaseOuterObjectTypeCachePool {

        @Override
        protected OuterObjectTypeMap createOuterObjectTypeMap() {
            OuterObjectTypeMap result = new OuterObjectTypeMap(BaseAccessControlCacheProvider.this.createOuterObjectTypeLoader());
            return result;
        }

        @Override
        protected BaseOuterObjectTypeCache createOuterObjectTypeCache(IdType outerObjectTypeId) {
            return new OuterObjectTypeCache(outerObjectTypeId);
        }
    }

    public class StatusSynchronizer extends BaseStatusSynchronizer {

        public StatusSynchronizer(BaseChangeRecorder restoreHandler) {
            super(restoreHandler);
        }

        @Override
        protected Map<ApplicationIdType, Set<TenantIdType>> loadApplicationIdTenantIdRange() {
            return BaseAccessControlCacheProvider.this.loadApplicationIdTenantIdRange();
        }
    }
}
