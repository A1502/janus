package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.DualClusterMap;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.Map;

public class TenantMap<K, V> extends DualClusterMap<ApplicationIdType, TenantIdType,K,V> {

    public TenantMapElement<K, V> getElement(ApplicationIdType applicationId, TenantIdType tenantId) {
        Map<K, V> element = get(applicationId, tenantId);
        TenantMapElement<K, V> result = new TenantMapElement<>(applicationId, tenantId, element);
        return result;
    }
}
