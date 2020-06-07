package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.core.critical.LevelEnum;
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
                                        User.byId("100"), LevelEnum.FOUR)
                        )
                )
        );

        TestUtils.extractAndPrint("testMergeRole", applicationGroup);
    }
}





















