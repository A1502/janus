package com.wuxian.janus.cache.source;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class IdUtils {

    public static IdType createId(String stringValue) {
        IdType id = new IdType(null);
        id.setStringValue(stringValue);
        return id;
    }

    public static UserIdType createUserId(String stringValue) {
        UserIdType id = new UserIdType(null);
        id.setStringValue(stringValue);
        return id;
    }

    public static TenantIdType createTenantId(String stringValue) {
        TenantIdType id = new TenantIdType(null);
        id.setStringValue(stringValue);
        return id;
    }

    public static ApplicationIdType createApplicationId(String stringValue) {
        ApplicationIdType id = new ApplicationIdType(null);
        id.setStringValue(stringValue);
        return id;
    }

    public static IdGenerator createIdGenerator(IdGeneratorFactory idGeneratorFactory) {
        Type type = ((ParameterizedType) IdType.class.getGenericSuperclass()).getActualTypeArguments()[0];
        return idGeneratorFactory.create(type);
    }
}
