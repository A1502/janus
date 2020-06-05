package com.wuxian.janus.core.basis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

class IndexedList<V> {

    private List<V> source = null;

    private Method[] fieldGetters;

    private Function<Object[], String[]> indexBuilder;

    private Map<String, List<V>> index;

    private String createIndexKey(V sample) throws InvocationTargetException, IllegalAccessException {

        Object[] fields = new Object[this.fieldGetters.length];
        for (int i = 0; i < fieldGetters.length; i++) {
            fields[i] = fieldGetters[i].invoke(sample);
        }
        String[] strings = indexBuilder.apply(fields);
        return StringUtils.safeJoinStrings(strings);
    }

    private void createIndex() throws InvocationTargetException, IllegalAccessException {
        index = new HashMap<>(this.source.size());
        for (V value : this.source) {
            String indexKey = createIndexKey(value);
            if (!StrictUtils.containsKey(index, indexKey)) {
                index.put(indexKey, new ArrayList<>());
            }
            StrictUtils.get(index, indexKey).add(value);
        }
    }

    List<V> get(V sample) throws InvocationTargetException, IllegalAccessException {
        if (source == null) {
            return new ArrayList<>();
        }
        if (index == null) {
            createIndex();
        }
        String indexKey = createIndexKey(sample);
        if (StrictUtils.containsKey(index, indexKey)) {
            return StrictUtils.get(index, indexKey);
        } else {
            return new ArrayList<>();
        }
    }

    void setSource(Collection<V> source) {

        clearSource();

        if (this.source == null) {
            this.source = new ArrayList<>();
        }
        this.source.addAll(source);
    }

    void clearSource() {
        if (this.source != null) {
            this.source.clear();
        }
        this.source = null;

        if (this.index != null) {
            this.index.clear();
        }
        this.index = null;
    }

    IndexedList(Class<V> clazz, Function<Object[], String[]> indexBuilder, String... fieldNames) throws NoSuchMethodException {
        this.indexBuilder = indexBuilder;

        fieldGetters = new Method[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            fieldGetters[i] = ReflectUtils.findGetterMethodByFieldName(clazz, fieldName);
            if (fieldGetters[i] == null) {
                throw new NoSuchMethodException(clazz.toString() + "." + fieldName);
            }
        }
    }
}
