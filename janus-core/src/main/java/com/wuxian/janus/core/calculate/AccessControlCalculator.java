package com.wuxian.janus.core.calculate;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.primary.UserIdType;
import com.wuxian.janus.core.cache.BaseApplicationCache;
import com.wuxian.janus.core.cache.BaseApplicationCachePool;
import com.wuxian.janus.core.cache.BaseOuterObjectTypeCachePool;
import com.wuxian.janus.core.cache.BaseTenantCache;
import com.wuxian.janus.core.synchronism.BaseStatusSynchronizer;

import java.util.List;

/**
 * @author wuxian
 */

public class AccessControlCalculator {

    public AccessControlCalculator(BaseOuterObjectTypeCachePool outerObjectCachePool
            , BaseApplicationCachePool applicationPoolCachePool
            , BaseStatusSynchronizer statusSynchronizer) {
        this.applicationPoolCachePool = applicationPoolCachePool;
        this.outerObjectCachePool = outerObjectCachePool;
        this.statusSynchronizer = statusSynchronizer;
    }

    private BaseApplicationCachePool applicationPoolCachePool;

    private BaseOuterObjectTypeCachePool outerObjectCachePool;

    private BaseStatusSynchronizer statusSynchronizer;

    private boolean checkApplicationIdTenantId(ApplicationIdType applicationId, TenantIdType tenantId) {
        this.statusSynchronizer.refresh(applicationId, tenantId, this.outerObjectCachePool, this.applicationPoolCachePool);
        return this.statusSynchronizer.checkApplicationIdTenantId(applicationId, tenantId);
    }

    //<editor-fold desc="简洁参数">

    public PermissionResult checkPermission(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, List<IdType> permissionTemplateIds) {
        return checkPermission(userId, applicationId, tenantId, null, permissionTemplateIds);
    }

    public PermissionResult checkPermission(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId,
                                            List<IdType> permissionTemplateIds, IdType outerObjectId) {
        return checkPermission(userId, applicationId, tenantId, null, permissionTemplateIds, outerObjectId);
    }

    public ExecuteAccessRolePackage getUserExecuteAccessSingleRoles(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, ErrorDataRecorder recorder) {
        return getUserExecuteAccessSingleRoles(userId, applicationId, tenantId, null, false, recorder);
    }

    public ExecuteAccessRolePackage getUserExecuteAccessMultipleRoles(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, ErrorDataRecorder recorder) {
        return getUserExecuteAccessMultipleRoles(userId, applicationId, tenantId, null, recorder);
    }

    public PermissionPackage getSinglePermission(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, ErrorDataRecorder recorder) {
        return getSinglePermission(userId, applicationId, tenantId, null, false, recorder);
    }

    public PermissionPackage getMultiplePermission(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, ErrorDataRecorder recorder) {
        return getMultiplePermission(userId, applicationId, tenantId, null, recorder);
    }

    //</editor-fold>

    //<editor-fold desc="完整参数">

    public PermissionResult checkPermission(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, List<String> scopes,
                                            List<IdType> permissionTemplateIds, IdType outerObjectId) {

        if (!this.checkApplicationIdTenantId(applicationId, tenantId)) {
            return null;
        }

        BaseApplicationCache applicationCache = applicationPoolCachePool.getApplicationCache(applicationId);

        ErrorDataRecorder recorder = new ErrorDataRecorder();

        PermissionPackage nativePermission = getSinglePermissionInner(userId, applicationId, tenantId, scopes, true, recorder);

        if (nativePermission.getHasAllPermission()) {
            //走native逻辑更快得到结果
            PermissionResult nativeResult = PermissionUtils.isPermitted(permissionTemplateIds, null,
                    nativePermission, applicationCache.getPermissionTemplateMap());
            return nativeResult;
        } else {
            PermissionPackage multiplePermission = getMultiplePermissionInner(userId, applicationId, tenantId, scopes, recorder);
            PermissionResult result = PermissionUtils.isPermitted(permissionTemplateIds, outerObjectId,
                    multiplePermission, applicationCache.getPermissionTemplateMap());
            return result;
        }
    }

    public PermissionResult checkPermission(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, List<String> scopes, List<IdType> permissionTemplateId) {

        if (!this.checkApplicationIdTenantId(applicationId, tenantId)) {
            return null;
        }

        ErrorDataRecorder recorder = new ErrorDataRecorder();
        PermissionPackage permissionPackage = getSinglePermissionInner(userId, applicationId, tenantId, scopes, false, recorder);

        BaseApplicationCache applicationCache = applicationPoolCachePool.getApplicationCache(applicationId);
        PermissionResult result = PermissionUtils.isPermitted(permissionTemplateId, null,
                permissionPackage, applicationCache.getPermissionTemplateMap());
        return result;
    }

    public ExecuteAccessRolePackage getUserExecuteAccessSingleRoles(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId,
                                                                    List<String> scopes, boolean nativeRoleOnly, ErrorDataRecorder recorder) {
        if (!this.checkApplicationIdTenantId(applicationId, tenantId)) {
            return null;
        }

        return getUserExecuteAccessSingleRolesInner(userId, applicationId, tenantId,
                scopes, nativeRoleOnly, recorder);
    }

    private ExecuteAccessRolePackage getUserExecuteAccessSingleRolesInner(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId,
                                                                          List<String> scopes, boolean nativeRoleOnly, ErrorDataRecorder recorder) {

        BaseApplicationCache applicationCache = applicationPoolCachePool.getApplicationCache(applicationId);
        BaseTenantCache applicationTenantCache = applicationCache.getTenantCache(tenantId);

        ExecuteAccessRolePackage rolePackage = RoleUtils.getExecuteAccessRolePackage(userId,
                scopes,
                applicationTenantCache.getScopeSingleRoleUserX(),
                applicationTenantCache.getSingleRoleUserX(),
                applicationTenantCache.getSingleRoleUserGroupX(),
                applicationTenantCache.getSingleRoleOtherX(),
                applicationTenantCache.getScopeUserGroupUserX(),
                applicationTenantCache.getUserGroupUserX(),
                applicationTenantCache.getSingleRole(),
                applicationTenantCache.getMultipleRole(),
                applicationTenantCache.getUserGroup(),
                outerObjectCachePool,
                true,
                nativeRoleOnly,
                recorder
        );

        return rolePackage;
    }

    public ExecuteAccessRolePackage getUserExecuteAccessMultipleRoles(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, List<String> scopes, ErrorDataRecorder recorder) {

        if (!this.checkApplicationIdTenantId(applicationId, tenantId)) {
            return null;
        }

        return getUserExecuteAccessMultipleRolesInner(userId, applicationId, tenantId, scopes, recorder);
    }

    private ExecuteAccessRolePackage getUserExecuteAccessMultipleRolesInner(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, List<String> scopes, ErrorDataRecorder recorder) {

        BaseApplicationCache applicationCache = applicationPoolCachePool.getApplicationCache(applicationId);
        BaseTenantCache applicationTenantCache = applicationCache.getTenantCache(tenantId);

        ExecuteAccessRolePackage rolePackage = RoleUtils.getExecuteAccessRolePackage(userId,
                scopes,
                applicationTenantCache.getScopeMultipleRoleUserX(),
                applicationTenantCache.getMultipleRoleUserX(),
                applicationTenantCache.getMultipleRoleUserGroupX(),
                applicationTenantCache.getMultipleRoleOtherX(),
                applicationTenantCache.getScopeUserGroupUserX(),
                applicationTenantCache.getUserGroupUserX(),
                applicationTenantCache.getSingleRole(),
                applicationTenantCache.getMultipleRole(),
                applicationTenantCache.getUserGroup(),
                outerObjectCachePool,
                false,
                false,
                recorder
        );
        return rolePackage;
    }

    public PermissionPackage getSinglePermission(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId,
                                                 List<String> scopes, boolean nativePermissionOnly, ErrorDataRecorder recorder) {
        if (!this.checkApplicationIdTenantId(applicationId, tenantId)) {
            return null;
        }

        return getSinglePermissionInner(userId, applicationId, tenantId,
                scopes, nativePermissionOnly, recorder);
    }

    private PermissionPackage getSinglePermissionInner(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId,
                                                       List<String> scopes, boolean nativePermissionOnly, ErrorDataRecorder recorder) {

        BaseApplicationCache applicationCache = applicationPoolCachePool.getApplicationCache(applicationId);
        BaseTenantCache applicationTenantCache = applicationCache.getTenantCache(tenantId);

        ExecuteAccessRolePackage rolePackage = getUserExecuteAccessSingleRolesInner(userId, applicationId, tenantId, scopes, nativePermissionOnly, recorder);

        PermissionPackage permissionPackage = PermissionUtils.getPermissionPackage(
                rolePackage,
                applicationCache.getPermissionTemplateMap(),
                applicationTenantCache.getSingleRolePermissionX(),
                applicationTenantCache.getSinglePermission(),
                applicationTenantCache.getMultiplePermission(),
                outerObjectCachePool,
                true,
                nativePermissionOnly,
                recorder);

        return permissionPackage;
    }

    public PermissionPackage getMultiplePermission(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, List<String> scopes, ErrorDataRecorder recorder) {

        if (!this.checkApplicationIdTenantId(applicationId, tenantId)) {
            return null;
        }

        return getMultiplePermissionInner(userId, applicationId, tenantId, scopes, recorder);
    }

    private PermissionPackage getMultiplePermissionInner(UserIdType userId, ApplicationIdType applicationId, TenantIdType tenantId, List<String> scopes, ErrorDataRecorder recorder) {

        BaseApplicationCache applicationCache = applicationPoolCachePool.getApplicationCache(applicationId);
        BaseTenantCache applicationTenantCache = applicationCache.getTenantCache(tenantId);

        ExecuteAccessRolePackage rolePackage = getUserExecuteAccessMultipleRolesInner(userId, applicationId, tenantId, scopes, recorder);

        PermissionPackage permissionPackage = PermissionUtils.getPermissionPackage(
                rolePackage,
                applicationCache.getPermissionTemplateMap(),
                applicationTenantCache.getMultipleRolePermissionX(),
                applicationTenantCache.getSinglePermission(),
                applicationTenantCache.getMultiplePermission(),
                outerObjectCachePool,
                false,
                false,
                recorder);

        return permissionPackage;
    }

    //</editor-fold>

}
