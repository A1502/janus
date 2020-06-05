package com.wuxian.janus.core.synchronism;

import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.cache.*;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public abstract class BaseStatusSynchronizer {

    protected BaseChangeRecorder changeRecorder;

    public BaseStatusSynchronizer(BaseChangeRecorder restoreHandler) {
        this.changeRecorder = restoreHandler;
    }

    private Map<ApplicationIdType, Set<TenantIdType>> applicationIdTenantIdRange;

    protected abstract Map<ApplicationIdType, Set<TenantIdType>> loadApplicationIdTenantIdRange();

    private void fillApplicationIdTenantIdRange(boolean refresh) {
        if (applicationIdTenantIdRange == null || refresh) {
            applicationIdTenantIdRange = loadApplicationIdTenantIdRange();
        }
    }

    public boolean checkApplicationId(ApplicationIdType applicationId) {
        if (applicationIdTenantIdRange != null) {
            return StrictUtils.containsKey(applicationIdTenantIdRange, applicationId);
        }
        return false;
    }

    public boolean checkApplicationIdTenantId(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (applicationIdTenantIdRange != null) {
            return StrictUtils.containsKey(applicationIdTenantIdRange, applicationId)
                    && StrictUtils.get(applicationIdTenantIdRange, applicationId).contains(tenantId);
        }
        return false;
    }

    private void fill(AutoFillMultipleIndexesMap autoFillMultipleIndexesMap, boolean refresh) {
        boolean filled = autoFillMultipleIndexesMap.autoFillSource();
        if (!filled && refresh) {
            autoFillMultipleIndexesMap.refreshSource();
        }
    }

    private void refresh(boolean outerObjectTypeStatus,
                         Map<IdType, List<OuterObjectTypeCacheChangePart>> statusMap,
                         BaseOuterObjectTypeCachePool outerObjectTypeCachePool) {
        fill(outerObjectTypeCachePool.getOuterObjectTypeMap(), outerObjectTypeStatus);

        if (outerObjectTypeStatus) {
            outerObjectTypeCachePool.shrink();
        }

        for (OuterObjectTypeStruct typeStruct : outerObjectTypeCachePool.getOuterObjectTypeMap().getAll()) {
            IdType outerObjectTypeId = new IdType(typeStruct.getId());
            BaseOuterObjectTypeCache typeCache = outerObjectTypeCachePool.getOuterObjectTypeCache(outerObjectTypeId);

            if (StrictUtils.containsKey(statusMap, outerObjectTypeId)) {
                List<OuterObjectTypeCacheChangePart> list = StrictUtils.get(statusMap, outerObjectTypeId);
                fill(typeCache.getOuterObject(), list.contains(OuterObjectTypeCacheChangePart.OUTER_OBJECT));
                fill(typeCache.getUserOuterObjectX(), list.contains(OuterObjectTypeCacheChangePart.USER_OUTER_OBJECT));
            } else {
                fill(typeCache.getOuterObject(), false);
                fill(typeCache.getUserOuterObjectX(), false);
            }
        }
    }

    private void refresh(List<TenantChangePart> list, BaseTenantCache cache) {
        fill(cache.getScopeSingleRoleUserX(), list.contains(TenantChangePart.SCOPE_SINGLE_ROLE_USER));
        fill(cache.getScopeMultipleRoleUserX(), list.contains(TenantChangePart.SCOPE_MULTIPLE_ROLE_USER));
        fill(cache.getScopeUserGroupUserX(), list.contains(TenantChangePart.SCOPE_USER_GROUP_USER));

        fill(cache.getUserGroup(), list.contains(TenantChangePart.USER_GROUP));
        fill(cache.getUserGroupUserX(), list.contains(TenantChangePart.USER_GROUP_USER));

        fill(cache.getSinglePermission(), list.contains(TenantChangePart.SINGLE_PERMISSION));
        fill(cache.getSingleRole(), list.contains(TenantChangePart.SINGLE_ROLE));
        fill(cache.getSingleRolePermissionX(), list.contains(TenantChangePart.SINGLE_ROLE_PERMISSION));
        fill(cache.getSingleRoleUserX(), list.contains(TenantChangePart.SINGLE_ROLE_USER));
        fill(cache.getSingleRoleUserGroupX(), list.contains(TenantChangePart.SINGLE_ROLE_USER_GROUP));
        fill(cache.getSingleRoleOtherX(), list.contains(TenantChangePart.SINGLE_ROLE_OTHER));

        fill(cache.getMultiplePermission(), list.contains(TenantChangePart.MULTIPLE_PERMISSION));
        fill(cache.getMultipleRole(), list.contains(TenantChangePart.MULTIPLE_ROLE));
        fill(cache.getMultipleRolePermissionX(), list.contains(TenantChangePart.MULTIPLE_ROLE_PERMISSION));
        fill(cache.getMultipleRoleUserX(), list.contains(TenantChangePart.MULTIPLE_ROLE_USER));
        fill(cache.getMultipleRoleUserGroupX(), list.contains(TenantChangePart.MULTIPLE_ROLE_USER_GROUP));
        fill(cache.getMultipleRoleOtherX(), list.contains(TenantChangePart.MULTIPLE_ROLE_OTHER));
    }

    private ChangeStatus refresh(boolean outerObjectTypeStatus,
                                 Map<IdType, List<OuterObjectTypeCacheChangePart>> outerObjectTypeCacheStatusMap,
                                 BaseOuterObjectTypeCachePool outerObjectTypeCachePool,
                                 boolean applicationIdTenantIdRangeStatus,
                                 ApplicationIdType applicationId, TenantIdType tenantId,
                                 boolean permissionTemplateStatus,
                                 List<TenantChangePart> tenantStatus,
                                 BaseApplicationCachePool applicationCachePool) {

        ChangeStatus revert = new ChangeStatus();
        revert.setOuterObjectTypeStatus(false);
        revert.changeOuterObjectTypeCacheStatus(outerObjectTypeCacheStatusMap, false);
        revert.setApplicationIdTenantIdRangeStatus(false);

        fillApplicationIdTenantIdRange(applicationIdTenantIdRangeStatus);
        if (applicationIdTenantIdRangeStatus) {
            applicationCachePool.shrink(this.applicationIdTenantIdRange);
        }

        if (checkApplicationId(applicationId)) {
            revert.changeApplicationStatus(applicationId, false);
        }

        refresh(outerObjectTypeStatus, outerObjectTypeCacheStatusMap, outerObjectTypeCachePool);

        if (checkApplicationIdTenantId(applicationId, tenantId)) {
            BaseApplicationCache applicationCache = applicationCachePool.getApplicationCache(applicationId);
            fill(applicationCache.getPermissionTemplateMap(), permissionTemplateStatus);

            BaseTenantCache applicationTenantCache = applicationCache.getTenantCache(tenantId);
            refresh(tenantStatus, applicationTenantCache);

            revert.changeTenantStatus(applicationId, tenantId, tenantStatus, false);
        }
        return revert;
    }

    public void refresh(ApplicationIdType applicationId, TenantIdType tenantId
            , BaseOuterObjectTypeCachePool outerObjectTypeCachePool
            , BaseApplicationCachePool applicationCachePool) {


        Function<ChangeStatus, ChangeStatus> handler = input -> {

            List<TenantChangePart> tenantStatus = new ArrayList<>();

            input.fetchChangedTenant().stream().filter(t -> StrictUtils.equals(t.applicationId, applicationId)
                    && StrictUtils.equals(t.tenantId, tenantId)).findFirst().ifPresent(
                    k -> tenantStatus.addAll(k.value)
            );

            //输入和定义该refresh方法排版一致，方便阅读
            return refresh(input.getOuterObjectTypeStatus(),
                    input.fetchChangedOuterObjectTypeCache(),
                    outerObjectTypeCachePool,
                    input.getApplicationIdTenantIdRangeStatus(),
                    applicationId, tenantId,
                    input.fetchChangedApplication().contains(applicationId),
                    tenantStatus,
                    applicationCachePool);
        };
        changeRecorder.accept(handler);
    }
}
