package com.wuxian.janus.core.cache;

import com.wuxian.janus.core.index.OuterObjectMap;
import com.wuxian.janus.core.index.UserOuterObjectXMap;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.util.StrictUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class BaseOuterObjectTypeCache {

    private Map<Class, Object> instanceBuffer = new HashMap<>();

    private <T> T getInstance(Class<T> clazz, Supplier<T> instanceBuilder) {
        if (!StrictUtils.containsKey(instanceBuffer, clazz)) {
            instanceBuffer.put(clazz, instanceBuilder.get());
        }
        return (T) instanceBuffer.get(clazz);
    }

    public IdType getOuterObjectTypeId() {
        return outerObjectTypeId;
    }

    private IdType outerObjectTypeId;

    public BaseOuterObjectTypeCache(IdType outerObjectTypeId) {
        this.outerObjectTypeId = outerObjectTypeId;
    }

    protected abstract OuterObjectMap createOuterObjectMap();

    public OuterObjectMap getOuterObject() {
        return getInstance(OuterObjectMap.class, this::createOuterObjectMap);
    }

    protected abstract UserOuterObjectXMap createUserOuterObjectXMap();

    public UserOuterObjectXMap getUserOuterObjectX() {
        return getInstance(UserOuterObjectXMap.class, this::createUserOuterObjectXMap);
    }
}
