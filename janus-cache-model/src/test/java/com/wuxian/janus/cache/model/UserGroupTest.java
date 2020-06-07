package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.source.User;
import com.wuxian.janus.cache.model.source.UserGroup;
import com.wuxian.janus.core.basis.ErrorCodeException;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.LevelEnum;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserGroupTest {


    @Test
    @DisplayName("测试正常合并")
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

    @Test
    @DisplayName("测试Access重复的合并,应异常")
    void testMergeAccessConflict() {

        UserGroup one = UserGroup.byId("0");
        one.setAccess(new Access(true, false, false, false, false));
        //id相同才可合并，否则报错
        UserGroup another = UserGroup.byId("0");
        another.setAccess(new Access(true, true, false, false, false));

        //Access在one和another中被设置了两次应该会冲突，无法合并的
        assertThrows(ErrorCodeException.class
                , () -> one.mergeFrom(another)
                , "未抛出ErrorCodeException异常");
    }
}





















