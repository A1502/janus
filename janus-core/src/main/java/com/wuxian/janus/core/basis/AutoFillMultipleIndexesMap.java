package com.wuxian.janus.core.basis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuxian
 */

public class AutoFillMultipleIndexesMap<K, V> extends MultipleIndexesMap<K, V> {

    private SourceLoader<K, V> sourceLoader;

    public AutoFillMultipleIndexesMap(SourceLoader<K, V> sourceLoader) {
        if (sourceLoader == null) {
            this.sourceLoader = () -> new HashMap<>();
        } else {
            this.sourceLoader = sourceLoader;
        }
    }

    public boolean autoFillSource() {
        if (!hasSource()) {
            Map<K, V> source = sourceLoader.load();
            this.setSource(source);
            return true;
        } else {
            return false;
        }
    }

    public void refreshSource() {
        this.clearSource();
        autoFillSource();
    }

    @Override
    public List<V> getByCondition(V conditionSample, String... conditionFieldNames) {
        autoFillSource();
        return super.getByCondition(conditionSample, conditionFieldNames);
    }

    @Override
    public List<V> getByKeys(K... keys) {
        autoFillSource();
        return super.getByKeys(keys);
    }

    @Override
    public V getByKey(K key) {
        autoFillSource();
        return super.getByKey(key);
    }

    @Override
    public List<V> getAll() {
        autoFillSource();
        return super.getAll();
    }
}
