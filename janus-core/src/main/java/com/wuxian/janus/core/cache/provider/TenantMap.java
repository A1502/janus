package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.core.StrictUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TenantMap<K, V> {

    private Map<ApplicationIdType, Map<TenantIdType, Map<K, V>>> storage = new HashMap<>();

    public Map<ApplicationIdType, Set<TenantIdType>> getIds() {
        Map<ApplicationIdType, Set<TenantIdType>> result = new HashMap<>();

        for (ApplicationIdType appId : storage.keySet()) {

            Map<TenantIdType, Map<K, V>> map = StrictUtils.get(storage, appId);

            result.put(appId, map.keySet());
        }
        return result;
    }

    public void add(ApplicationIdType applicationId, TenantIdType tenantId, Map<K, V> data) {
        get(applicationId, tenantId, true).putAll(data);
    }

    public Map<K, V> get(ApplicationIdType applicationId, TenantIdType tenantId) {
        return get(applicationId, tenantId, false);
    }

    private Map<K, V> get(ApplicationIdType applicationId, TenantIdType tenantId, boolean autoCreate) {
        if (!StrictUtils.containsKey(storage, applicationId)) {
            if (autoCreate) {
                storage.put(applicationId, new HashMap<>());
            } else {
                return null;
            }
        }
        Map<TenantIdType, Map<K, V>> applicationMap = StrictUtils.get(storage, applicationId);
        if (!StrictUtils.containsKey(applicationMap, tenantId)) {
            if (autoCreate) {
                applicationMap.put(tenantId, new HashMap<>());
            } else {
                return null;
            }
        }
        return StrictUtils.get(applicationMap, tenantId);
    }

    public boolean clear(ApplicationIdType applicationId, TenantIdType tenantId) {
        if (!StrictUtils.containsKey(storage, applicationId)) {
            return false;
        } else {
            Map<TenantIdType, Map<K, V>> applicationMap = StrictUtils.get(storage, applicationId);
            if (!StrictUtils.containsKey(applicationMap, tenantId)) {
                return false;
            } else {
                StrictUtils.remove(applicationMap, tenantId);
                if (applicationMap.isEmpty()) {
                    StrictUtils.remove(storage, applicationId);
                }
                return true;
            }
        }
    }
}
