package com.wuxian.janus.cache.model.extract.id;

import java.lang.reflect.Type;

public interface IdGeneratorFactory {
    IdGenerator create(Type valueType);
}
