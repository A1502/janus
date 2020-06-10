package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.basis.ErrorCodeException;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.LevelEnum;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
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


    @Test
    @DisplayName("测试RoleUserX实战多次关系合并")
    void testMergeUserGroupByApplicationGroup() {
        String appId = "1";
        String tId = "10";

        String user100 = "100";
        String user101 = "101";

        String roleAId = "700";
        String roleBId = "701";
        String roleCId = "702";

        String groupAId = "80";
        String groupAppAdminId = "23";
        String groupTenantRootId = "91";

        String oot0 = "outerObjectType0";
        String oo0 = "outerObject0";

        String scope_a = "scope_a";
        String scope_b = "scope_b";
        String scope_c = "scope_c";
        String scope_d = "scope_d";
        String scope_null = null;

        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                Application.byId(appId)
                        .setNative(NativeUserGroupEnum.APPLICATION_ADMIN, groupAppAdminId)
                        .addItem(
                                Tenant.byId(tId).setNative(NativeUserGroupEnum.TENANT_ROOT, groupTenantRootId).addItem(
                                        new UserGroup(NativeUserGroupEnum.TENANT_ROOT).addItem(
                                                User.byId(user100), LevelEnum.FULL)
                                        , UserGroup.byId(groupAppAdminId, NativeUserGroupEnum.APPLICATION_ADMIN)
                                                .setAccess(new Access(new boolean[]{true, true})).addItem(
                                                        User.byId(user100, scope_null, scope_c), LevelEnum.FULL).addItem(
                                                        User.byId(user100, scope_a, scope_b), LevelEnum.NONE).addItem(
                                                        Role.byId(roleAId, "roleA", true)
                                                        , Role.byId(roleBId, "roleB", false)
                                                        , Role.byId(roleCId, "roleC", false)
                                                )
                                        , UserGroup.byId(groupAId, "groupA", oot0, oo0).addItem(
                                                Role.byId(roleBId, false)
                                                , User.byId(user101, scope_d))
                                )
                        )
        );

        DirectAccessControlSource source = TestUtils.extractAndPrint("testMergeUserGroupByApplicationGroup", applicationGroup);


    }
}





















