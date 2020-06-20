package com.wuxian.janus.core.cache;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.core.index.PermissionTemplateMap;
import com.wuxian.janus.util.StrictUtils;

import java.util.*;

/**
 * @author wuxian
 */

public abstract class BaseApplicationCache {

    protected ApplicationIdType applicationId;

    public BaseApplicationCache(ApplicationIdType applicationId) {
        this.applicationId = applicationId;
    }

    private PermissionTemplateMap permissionTemplateMap = null;

    protected abstract PermissionTemplateMap createPermissionTemplateMap();

    public PermissionTemplateMap getPermissionTemplateMap() {
        if (permissionTemplateMap == null) {
            permissionTemplateMap = createPermissionTemplateMap();
        }
        return permissionTemplateMap;
    }

    /**
     * 根据给定的range来收缩数据，去掉过期的key
     */
    public void shrink(Set<TenantIdType> tenantIdRange) {
        List<TenantIdType> existsTenantIds = new ArrayList<>(applicationTenantCachePool.keySet());
        for (TenantIdType existsTenantId : existsTenantIds) {
            if (!tenantIdRange.contains(existsTenantId)) {
                applicationTenantCachePool.remove(existsTenantId);
            }
        }
    }

    private Map<TenantIdType, BaseTenantCache> applicationTenantCachePool = new HashMap<>();

    public BaseTenantCache getTenantCache(TenantIdType tenantId) {
        if (!StrictUtils.containsKey(applicationTenantCachePool, tenantId)) {
            BaseTenantCache applicationCache = createTenantCache(this.applicationId, tenantId);
            applicationTenantCachePool.put(tenantId, applicationCache);
            return applicationCache;
        } else {
            return StrictUtils.get(applicationTenantCachePool, tenantId);
        }
    }

    protected abstract BaseTenantCache createTenantCache(ApplicationIdType applicationId, TenantIdType tenantId);
}
