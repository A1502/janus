package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.NamedConverter;
import com.wuxian.janus.core.basis.SourceLoader;

public class SimpleIndexesMap<K, V> extends AutoFillMultipleIndexesMap<K, V> {

    public SimpleIndexesMap(SourceLoader<K, V> sourceLoader) {
        super(sourceLoader);
    }

    public void createIndex(Class<V> clazz, String... fieldNames) {
        NamedConverter[] converters = new NamedConverter[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            converters[i] = new NamedConverter(fieldNames[i], this::safeToString);
        }
        super.createIndex(clazz, converters);
    }

    protected String safeToString(Object input) {
        if (input != null) {
            return input.toString();
        } else {
            return null;
        }
    }
}
