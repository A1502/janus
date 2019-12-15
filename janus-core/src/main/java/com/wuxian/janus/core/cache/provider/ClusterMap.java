package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.core.StrictUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClusterMap<ID, K, V> {

    private Map<ID, Map<K, V>> storage = new HashMap<>();

    public Set<ID> getIds() {
        return storage.keySet();
    }

    private Map<K, V> get(ID id, boolean autoCreate) {
        if (!StrictUtils.containsKey(storage, id)) {
            if (autoCreate) {
                storage.put(id, new HashMap<>());
            } else {
                return null;
            }
        }
        return StrictUtils.get(storage, id);
    }

    public Map<K, V> get(ID id) {
        return get(id, false);
    }

    public void add(ID id, Map<K, V> data) {
        get(id, true).putAll(data);
    }
}
