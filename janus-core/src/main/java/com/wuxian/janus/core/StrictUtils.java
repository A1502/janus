package com.wuxian.janus.core;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.List;
import java.util.Map;

public final class StrictUtils {

    public static <K, V> boolean containsKey(Map<K, V> map, K key) {
        return map.containsKey(key);
    }

    public static <K, V> boolean containsValue(Map<K, V> map, V value) {
        return map.containsValue(value);
    }

    public static <K, V> V remove(Map<K, V> map, K key) {
        return map.remove(key);
    }

    public static <K, V> V get(Map<K, V> map, K key) {
        return map.get(key);
    }

    public static <T> T get(List<T> list, int index) {
        return list.get(index);
    }

    public static boolean equals(IdType obj1, IdType obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }

    public static boolean equals(ApplicationIdType obj1, ApplicationIdType obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }

    public static boolean equals(TenantIdType obj1, TenantIdType obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }

    public static boolean equals(UserIdType obj1, UserIdType obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }

    public static boolean equals(Boolean obj1, Boolean obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }

    public static boolean equals(Integer obj1, Integer obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }

    public static boolean equals(Long obj1, Long obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }

    public static boolean equals(String obj1, String obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }

    public static <T> boolean equals(T obj1, T obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return obj2 == null;
        }
    }
}
