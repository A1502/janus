package com.wuxian.janus.core.basis;

import com.wuxian.janus.core.StrictUtils;

import java.util.HashMap;
import java.util.Map;

public class BeanCopyUtils {

    private static Map<Class, BeanCopyManager> managerCache = new HashMap<>();

    public static <T> void copy(T copyTo, T copyFrom, Class<T> clazz, FieldCopyHandler handler) {
        if (copyFrom == null) {
            return;
        }
        if (!StrictUtils.containsKey(managerCache, clazz)) {
            managerCache.put(clazz, new BeanCopyManager<T>());
        }
        BeanCopyManager<T> manager = StrictUtils.get(managerCache, clazz);
        manager.copy(copyTo, copyFrom, clazz, handler);
    }

    public static <T> void copy(T copyTo, T copyFrom, Class<T> clazz) {
        copy(copyTo, copyFrom, clazz, null);
    }
}
