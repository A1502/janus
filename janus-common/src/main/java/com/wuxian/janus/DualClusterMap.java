package com.wuxian.janus;

import com.wuxian.janus.util.StrictUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DualClusterMap<ID1, ID2, K, V> {

    private Map<ID1, Map<ID2, Map<K, V>>> storage = new HashMap<>();

    public Map<ID1, Set<ID2>> getIds() {
        Map<ID1, Set<ID2>> result = new HashMap<>();

        for (ID1 appId : storage.keySet()) {

            Map<ID2, Map<K, V>> map = StrictUtils.get(storage, appId);

            result.put(appId, map.keySet());
        }
        return result;
    }

    public void add(ID1 applicationId, ID2 tenantId, Map<K, V> data) {
        get(applicationId, tenantId, true).putAll(data);
    }

    public Map<K, V> get(ID1 applicationId, ID2 tenantId) {
        return get(applicationId, tenantId, false);
    }

    private Map<K, V> get(ID1 applicationId, ID2 tenantId, boolean autoCreate) {
        if (!StrictUtils.containsKey(storage, applicationId)) {
            if (autoCreate) {
                storage.put(applicationId, new HashMap<>());
            } else {
                return null;
            }
        }
        Map<ID2, Map<K, V>> applicationMap = StrictUtils.get(storage, applicationId);
        if (!StrictUtils.containsKey(applicationMap, tenantId)) {
            if (autoCreate) {
                applicationMap.put(tenantId, new HashMap<>());
            } else {
                return null;
            }
        }
        return StrictUtils.get(applicationMap, tenantId);
    }

    public boolean clear(ID1 applicationId, ID2 tenantId) {
        if (!StrictUtils.containsKey(storage, applicationId)) {
            return false;
        } else {
            Map<ID2, Map<K, V>> applicationMap = StrictUtils.get(storage, applicationId);
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
