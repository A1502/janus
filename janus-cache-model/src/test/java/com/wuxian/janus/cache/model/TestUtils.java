package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.extract.SourceExtractor;
import com.wuxian.janus.cache.model.extract.id.LongIdGeneratorFactory;
import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;

import java.util.Set;
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

    public static DirectAccessControlSource extractAndPrint(String testName, ApplicationGroup applicationGroup) {
        SourceExtractor sourceExtractor = new SourceExtractor(new LongIdGeneratorFactory());
        DirectAccessControlSource result = sourceExtractor.extract(applicationGroup);

        System.out.println("==============" + testName + "===============");
        result.print(System.out);
        System.out.println("--------------------------------------------------------");
        return result;
    }


    public static <T> boolean match(Set<T> set, T[] array) {

        if (set.size() != array.length) {
            return false;
        }

        for (T item : array) {
            if (!set.contains(item)) {
                return false;
            }
        }
        return true;
    }
}
