package com.wuxian.janus;

import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;
import com.wuxian.janus.entity.primary.UserIdType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IdBuilder {

    public static IdType id(Integer no) {
        IdType result = new IdType(convert(getTypeArgument(IdType.class), no));
        return result;
    }

    public static ApplicationIdType aId(Integer no) {
        ApplicationIdType result = new ApplicationIdType(convert(getTypeArgument(ApplicationIdType.class), no));
        return result;
    }

    public static TenantIdType tId(Integer no) {
        TenantIdType result = new TenantIdType(convert(getTypeArgument(TenantIdType.class), no));
        return result;
    }

    public static UserIdType uId(Integer no) {
        UserIdType result = new UserIdType(convert(getTypeArgument(UserIdType.class), no));
        return result;
    }

    public static List<IdType> ids(Integer... no) {
        List<IdType> list = new ArrayList<>();

        for (int i = 0; i < no.length; i++) {
            list.add(id(no[i]));
        }
        return list.stream().sorted().collect(Collectors.toList());
    }

    private static Type getTypeArgument(Class<?> clazz) {
        Type result = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        return result;
    }

    private static <T> T convert(Type outType, Integer input) {
        if (outType == Long.class) {
            return input == null ? null : (T) new Long(input);
        } else if (outType == Integer.class) {
            return input == null ? null : (T) input;
        } else if (outType == String.class) {
            return input == null ? null : (T) String.valueOf(input);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
