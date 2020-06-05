package com.wuxian.janus.core.cache;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.core.StrictUtils;

import java.util.*;

/**
 * @author wuxian
 */

public abstract class BaseApplicationCachePool {

    protected Map<ApplicationIdType, BaseApplicationCache> applicationCachePool = new HashMap<>();

    public BaseApplicationCache getApplicationCache(ApplicationIdType applicationId) {
        if (!StrictUtils.containsKey(applicationCachePool, applicationId)) {
            BaseApplicationCache item = createApplicationCache(applicationId);
            applicationCachePool.put(applicationId, item);
            return item;
        } else {
            return StrictUtils.get(applicationCachePool, applicationId);
        }
    }

    /**
     * 根据给定的range来收缩数据，去掉过期的key
     */
    public void shrink(Map<ApplicationIdType, Set<TenantIdType>> applicationIdTenantIdRange) {
        List<ApplicationIdType> existsApplicationIds = new ArrayList<>(applicationCachePool.keySet());
        for (ApplicationIdType existsApplicationId : existsApplicationIds) {
            if (!StrictUtils.containsKey(applicationIdTenantIdRange, existsApplicationId)) {
                //applicationId不存在，移除此项
                applicationCachePool.remove(existsApplicationId);
            } else {
                //applicationId存在，对其tenant范围进行shrink
                StrictUtils.get(applicationCachePool, existsApplicationId)
                        .shrink(StrictUtils.get(applicationIdTenantIdRange, existsApplicationId));
            }
        }
    }

    protected abstract BaseApplicationCache createApplicationCache(ApplicationIdType applicationId);
}
