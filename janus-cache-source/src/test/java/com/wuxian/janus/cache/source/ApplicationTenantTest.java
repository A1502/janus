package com.wuxian.janus.cache.source;

import com.wuxian.janus.cache.source.extract.SourceExtractor;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.AccessControlCalculator;
import com.wuxian.janus.core.PermissionResult;
import com.wuxian.janus.core.cache.provider.DirectAccessControlCacheProvider;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.synchronism.ClassicChangeRecorder;
import com.wuxian.janus.entity.primary.IdType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ApplicationTenantTest {
    @Test
    @DisplayName("测试Application模型基本数据构建")
    void testInitApplicationTenant() {
        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                OuterObjectType.byId("2", "MyOrganization"),
                OuterObjectType.byId("1", "AAAOutGroup"))
                .addItem(
                        OuterObject.byId("12", "ref8002", "MyOrganization"),
                        OuterObject.byId("10", "ref100", "AAAOutGroup").addItem(User.byId("1")),
                        OuterObject.byId("11", "ref8001", "MyOrganization"));
        Application app1 = Application.byId("1")
                .setNative(NativeRoleEnum.ALL_PERMISSION, "1")
                .setNative(NativeRoleEnum.APPLICATION_MAINTAINER, "2")
                .setNative(NativePermissionTemplateEnum.CREATE_PERMISSION_TEMPLATE, "1", "1")
                .setNative(NativePermissionTemplateEnum.INIT_TENANT, "2", "2")
                .setNative(NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION, "3")
                .setNative(NativePermissionTemplateEnum.CREATE_OUTER_GROUP, "4")
                .setNative(NativePermissionTemplateEnum.CREATE_INNER_GROUP, "5")
                .setNative(NativePermissionTemplateEnum.CREATE_ROLE, "6")
                .addItem(
                        PermissionTemplate.byId("14", "p-14-synchronism"),
                        PermissionTemplate.byId("15", "p-15-del"),
                        PermissionTemplate.byId("16", "p-16-add"),
                        PermissionTemplate.byId("17", "p-17-edit"),
                        PermissionTemplate.byId("18", "p-18-del-org", "MyOrganization"),
                        PermissionTemplate.byId("19", "p-19-update-org", "MyOrganization"),
                        PermissionTemplate.byId("100", "p-100-add-org", "MyOrganization")
                )
                .addItem(
                        Tenant.byId("10")
                                .setNative(NativeRoleEnum.TENANT_MAINTAINER, "3")
                                .setNative(NativeRoleEnum.TENANT_DATA_OWNER, "4")
                                .setNative(NativePermissionTemplateEnum.CREATE_MULTIPLE_PERMISSION, "3")
                                .setNative(NativePermissionTemplateEnum.CREATE_OUTER_GROUP, "4")
                                .setNative(NativePermissionTemplateEnum.CREATE_INNER_GROUP, "205")
                                .setNative(NativePermissionTemplateEnum.CREATE_ROLE, "206")
                                .addItem(
                                        UserGroup.byId("6", "stdUG", "AAAOutGroup", "ref100").addItem(Role.byId("5", "xxxRole", false)),
                                        UserGroup.byId("7", "bbbUG").addItem(
                                                Role.byId("6", "yyyRole", false), Role.byId("7", "zzzRole", true), User.byId("1")))
                                .addItem(
                                        new Role(NativeRoleEnum.ALL_PERMISSION).addItem(User.byId("1")),
                                        new Role(NativeRoleEnum.TENANT_MAINTAINER).addItem(
                                                Permission.byId("715", "p-15-del"), Permission.byId("205", NativePermissionTemplateEnum.CREATE_INNER_GROUP),
                                                Permission.byId("206", NativePermissionTemplateEnum.CREATE_ROLE), User.byId("1")
                                        ),
                                        Role.byId("5", "xxxRole", false).addItem(
                                                Permission.byId("715", "p-15-del"), Permission.byId("716", "p-16-add")
                                        ),
                                        Role.byId("6", "yyyRole", false).addItem(
                                                Permission.byId("716", "p-16-add"), Permission.byId("717", "p-17-edit")
                                        ),
                                        Role.byId("7", "zzzRole", true, "MyOrganization", "ref8002", null).addItem(
                                                Permission.byId("719", "p-19-update-org", "MyOrganization", "ref8002")))
                                .addItem(
                                        Permission.byId("714", "p-14-synchronism"),
                                        Permission.byId("718", "p-18-del-org", "MyOrganization", "ref8002"),
                                        Permission.byId("1000", "p-100-add-org", "MyOrganization", "ref8001"))
                );
        applicationGroup.addItem(app1);

        SourceExtractor sourceExtractor = new SourceExtractor(new LongIdGeneratorFactory());
        DirectAccessControlSource source = sourceExtractor.extract(applicationGroup);
        System.out.println("==============testInitApplicationTenant===============");
        source.print(System.out);
        System.out.println("--------------------------------------------------------");
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

        SourceExtractor sourceExtractor = new SourceExtractor(new LongIdGeneratorFactory());
        DirectAccessControlSource source = sourceExtractor.extract(applicationGroup);

        System.out.println("==============testHelloWorld===============");
        source.print(System.out);
        System.out.println("--------------------------------------------------------");
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

        SourceExtractor sourceExtractor = new SourceExtractor(new LongIdGeneratorFactory());
        DirectAccessControlSource source = sourceExtractor.extract(applicationGroup);

        System.out.println("==============testInitApplicationTenant===============");
        source.print(System.out);
        System.out.println("--------------------------------------------------------");

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
