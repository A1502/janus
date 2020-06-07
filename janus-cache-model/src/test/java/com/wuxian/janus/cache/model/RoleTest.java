package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.core.critical.LevelEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    @DisplayName("测试RoleUserX多次关系合并")
    void testMergeRole() {
        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                Application.byId("1").addItem(
                        Tenant.byId("10").addItem(
                                new Role("roleA", false)
                                        //所有人可见的角色
                                        .setAccess(new Access(new boolean[]{true, false, false, false, false}))
                                        .addItem(
                                                //role关联user默认是THREE
                                                //加入了a,b两个scope
                                                User.byId("100", "a_scope", "b_scope"),

                                                //new Permission只是用来做陪衬，提高仿真度
                                                new Permission("abc"),
                                                new Permission("def"),
                                                new Permission("xyz")
                                        )
                                        .addItem(
                                                //再次指定role关联user，且指定了AccessControl细节
                                                //加入了a,c两个scope
                                                User.byId("100", "a_scope", "c_scope")
                                                , new AccessControl(new boolean[]{false, false, false, false, false
                                                        , false, false, false, false, true})),

                                new Role("roleB", false).addItem(
                                        User.byId("101"),
                                        new Permission("abc"),
                                        new Permission("def")
                                ).addItem(
                                        //user 100关联到roleB,scope是null
                                        User.byId("100"), LevelEnum.FOUR),
                                new Role(NativeRoleEnum.ALL_PERMISSION).addItem(
                                        //这个故意测试user 100能完全控制这个native role
                                        //经过考虑觉得超过ag:root级别的能力也是可以的,没什么不妥
                                        //由于完全控制，可能会执行删除ALL_PERMISSION,
                                        //删除后会无法正常运行
                                        User.byId("100", "d_scope"), LevelEnum.FULL)
                        )
                )
        );

        TestUtils.extractAndPrint("testMergeRole", applicationGroup);
    }
}





















