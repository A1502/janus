package com.wuxian.janus.cache.source;

import java.lang.reflect.Type;

public class DefaultIdGeneratorFactory implements IdGeneratorFactory {

    @Override
    public IdGenerator create(Type valueType) {
        if (valueType == Long.class || valueType == Integer.class || valueType == String.class) {
            return new DefaultIdGenerator();
        } else {
            throw ErrorFactory.createIdGeneratorFactoryNotSupportError(this.getClass(), valueType);
        }
    }
}
