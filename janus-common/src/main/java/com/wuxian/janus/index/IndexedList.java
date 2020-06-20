package com.wuxian.janus.index;

import com.wuxian.janus.util.ReflectUtils;
import com.wuxian.janus.util.StrictUtils;
import com.wuxian.janus.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class IndexedList<V> {

    private List<V> source = null;

    private FieldConverterWithGetter[] fieldConverters;

    private Map<String, List<V>> index;

    private String createIndexKey(V sample) throws InvocationTargetException, IllegalAccessException {
        String[] fieldStringValues = new String[this.fieldConverters.length];
        for (int i = 0; i < fieldConverters.length; i++) {
            FieldConverterWithGetter converter = fieldConverters[i];
            Object fieldObjectValue = converter.getGetter().invoke(sample);
            fieldStringValues[i] = converter.getAction().apply(fieldObjectValue);
        }
        return StringUtils.safeJoinStrings(fieldStringValues);
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

    IndexedList(Class<V> clazz, NamedConverter... fieldNameAndConverters) throws NoSuchMethodException {

        fieldConverters = new FieldConverterWithGetter[fieldNameAndConverters.length];

        for (int i = 0; i < fieldConverters.length; i++) {
            String fieldName = fieldNameAndConverters[i].getName();
            Method getter = ReflectUtils.findGetterMethodByFieldName(clazz, fieldName);
            if (getter == null) {
                throw new NoSuchMethodException(clazz.getCanonicalName() + "." + fieldName);
            }
            fieldConverters[i] = new FieldConverterWithGetter(fieldNameAndConverters[i], getter);
        }
    }
}
