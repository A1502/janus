package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.data.AccessControlCacheProvider18;
import com.wuxian.janus.cache.model.data.ApplicationGroupDataLoader;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApplicationGroupTest {
    @Test
    @DisplayName("测试ApplicationGroup构建结果能等价于AccessControlCacheProvider的结果")
    void testInitApplicationTenant() {

        DirectAccessControlSource result1 = ApplicationGroupDataLoader.load();

        //new AccessControlCacheProvider18()和janus-core的test数据源AccessControlCacheProvider18是完全一致的。
        //但是复制了一份。单独作业避免产生依赖
        DirectAccessControlSource result2 = DirectAccessControlSource.dumpFrom(new AccessControlCacheProvider18());

        result1.print(System.out);
        result2.print(System.out);
    }
}
