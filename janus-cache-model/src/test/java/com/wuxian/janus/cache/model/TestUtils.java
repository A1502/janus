package com.wuxian.janus.cache.model;

import java.util.function.Predicate;

public class TestUtils {
    public static <T> T findFirst(Iterable<T> list, Predicate<T> findBy) {
        for (T item : list) {
            if (findBy.test(item)) {
                return item;
            }
        }
        return null;
    }
}
