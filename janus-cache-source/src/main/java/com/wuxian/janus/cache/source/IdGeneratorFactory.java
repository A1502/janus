package com.wuxian.janus.cache.source;

import java.lang.reflect.Type;

public interface IdGeneratorFactory {
    IdGenerator create(Type valueType);
}
