package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlCacheProvider;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.calculate.AccessControlCalculator;
import com.wuxian.janus.core.calculate.PermissionResult;
import com.wuxian.janus.core.synchronism.ClassicChangeRecorder;
import com.wuxian.janus.struct.primary.IdType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ApplicationTenantTest {

    @Test
    @DisplayName("测试Application和Tenant在最简模式下的纯Native角色和用户组的输出")
    void testNativeOnly() {
        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                Application.byId("1").addItem(
                        Tenant.byId("10"), Tenant.byId("20")));

        TestUtils.extractAndPrint("testNativeOnly", applicationGroup);
    }


    @Test
    @DisplayName("测试HelloWorld")
    void testHelloWorld() {
        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                Application.byId("1").addItem(
                        Tenant.byId("10").addItem(
                                new Role("deptMgr", false).addItem(
                                        User.byId("100"),
                                        new Permission("abc"),
                                        new Permission("def"),
                                        new Permission("xyz")
                                ),
                                new Role("deptEmployee", false).addItem(
                                        User.byId("101"),
                                        User.byId("102"),
                                        new Permission("abc"),
                                        new Permission("def")
                                )
                        )
                )
        );

        TestUtils.extractAndPrint("testHelloWorld", applicationGroup);
    }

    //整合测试模拟代码
    void x() {
        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                Application.byId("1").addItem(
                        Tenant.byId("10").addItem(
                                new Role("deptMgr", false).addItem(
                                        User.byId("100"),
                                        new Permission("abc"),
                                        new Permission("def"),
                                        new Permission("xyz")
                                ),
                                new Role("deptEmployee", false).addItem(
                                        User.byId("101"),
                                        User.byId("102"),
                                        new Permission("abc"),
                                        new Permission("def")
                                )
                        )
                )
        );

        DirectAccessControlSource source = TestUtils.extractAndPrint("x", applicationGroup);

        DirectAccessControlCacheProvider cacheProvider = DirectAccessControlCacheProvider.createFrom(source);

        AccessControlCalculator cpu = new AccessControlCalculator(
                cacheProvider.new OuterObjectCachePool(),
                cacheProvider.new ApplicationCachePool(),
                cacheProvider.new StatusSynchronizer(new ClassicChangeRecorder())
        );

        PermissionResult result = cpu.checkPermission(IdUtils.createUserId("100")
                , IdUtils.createApplicationId("1"),
                IdUtils.createTenantId("100"), Arrays.asList(
                        new IdType(20L)
                )
        );
    }
}
