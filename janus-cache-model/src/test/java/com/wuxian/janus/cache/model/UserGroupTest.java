package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.source.User;
import com.wuxian.janus.cache.model.source.UserGroup;
import com.wuxian.janus.core.critical.LevelEnum;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserGroupTest {


    @Test
    @DisplayName("测试合并")
    void testMerge() {

        UserGroup one = UserGroup.byId("0");
        one.addItem(User.byId("10"));

        //id相同才可合并，否则报错
        UserGroup another = UserGroup.byId("0");
        one.addItem(User.byId("10"), LevelEnum.ONE);
        another.addItem(User.byId("20"));

        one.mergeFrom(another);

        //users也以Map方式合并了
        Assert.assertEquals(one.getUsers().size(), 3);
    }
}





















