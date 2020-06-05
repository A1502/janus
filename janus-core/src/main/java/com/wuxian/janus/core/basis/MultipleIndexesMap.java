package com.wuxian.janus.core.basis;

import com.wuxian.janus.core.ErrorFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;

/**
 * @author wuxian
 * <p>
 * 这个类实现了<K,V>方式存储基础上同时支持索引来提高查询性能，最终体现在getByCondition方法
 */

public class MultipleIndexesMap<K, V> {

    protected Map<K, V> sourceMap = null;

    private Map<String, IndexedList<V>> indexedListMap = new HashMap<>();

    private String getIndexName(String... fieldNames) {
        return String.join(",", fieldNames);
    }

    public List<V> getByCondition(V conditionSample, String... conditionFieldNames) {

        String indexName = getIndexName(conditionFieldNames);
        if (indexedListMap == null || !StrictUtils.containsKey(indexedListMap, indexName)) {
            return new ArrayList<>();
        }

        IndexedList<V> index = StrictUtils.get(indexedListMap, indexName);
        try {
            return index.get(conditionSample);
        } catch (InvocationTargetException e) {
            throw ErrorFactory.createRetrieveIndexError(e.getMessage());
        } catch (IllegalAccessException e) {
            throw ErrorFactory.createRetrieveIndexError(e.getMessage());
        }
    }

    public List<V> getAll() {
        return new ArrayList<>(sourceMap.values());
    }

    public V getByKey(K key) {
        return sourceMap.getOrDefault(key, null);
    }

    public List<V> getByKeys(K... keys) {
        List<V> result = new ArrayList<>();
        for (K k : keys) {
            result.add(getByKey(k));
        }
        return result;
    }

    public void setSource(Map<K, V> source) {
        if (sourceMap == null) {
            sourceMap = new HashMap<>();
        }
        this.sourceMap.clear();
        this.sourceMap.putAll(source);
        for (IndexedList indexedList : indexedListMap.values()) {
            indexedList.setSource(this.sourceMap.values());
        }
    }

    public void clearSource() {
        this.sourceMap.clear();
        this.sourceMap = null;
        for (IndexedList indexedList : indexedListMap.values()) {
            indexedList.clearSource();
        }
    }

    public boolean hasSource() {
        return sourceMap != null;
    }

    public void createIndex(Class<V> clazz, NamedConverter... fieldNameAndConverters) {
        IndexedList<V> indexedList;
        try {
            indexedList = new IndexedList<>(clazz, fieldNameAndConverters);
        } catch (NoSuchMethodException e) {
            throw ErrorFactory.createCreateIndexError(e.getMessage());
        }

        String[] fieldNames = new String[fieldNameAndConverters.length];
        for (int i = 0; i < fieldNameAndConverters.length; i++) {
            fieldNames[i] = fieldNameAndConverters[i].getName();
        }
        String indexName = getIndexName(fieldNames);
        indexedListMap.put(indexName, indexedList);

        if (sourceMap != null) {
            indexedList.setSource(this.sourceMap.values());
        }
    }
}