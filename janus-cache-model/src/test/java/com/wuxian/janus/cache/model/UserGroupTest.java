package com.wuxian.janus.cache.model;

import com.wuxian.janus.ErrorCodeException;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.calculate.AccessControlUtils;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.core.critical.LevelEnum;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.struct.layer1.RoleUserGroupXStruct;
import com.wuxian.janus.struct.layer1.ScopeUserGroupUserXStruct;
import com.wuxian.janus.struct.layer1.UserGroupOtherXStruct;
import com.wuxian.janus.struct.layer1.UserGroupUserXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.prototype.layer1.ScopeUserGroupUserXPrototype;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
                                                        User.byId(user100, scope_null, scope_c), LevelEnum.TWO_POINT_FIVE).addItem(
                                                        User.byId(user100, scope_a, scope_b), LevelEnum.THREE).addItem(
                                                        Role.byId(roleAId, "roleA", true)
                                                        , Role.byId(roleCId, "roleC", false)).addItem(
                                                        Role.byId(roleBId, "roleB", false), LevelEnum.HYPO_FULL
                                                )
                                        , UserGroup.byId(groupAId, "groupA", oot0, oo0).addItem(
                                                Role.byId(roleBId, false)
                                                , User.byId(user101, scope_d))
                                )
                        )
        );

        DirectAccessControlSource source = TestUtils.extractAndPrint("testMergeUserGroupByApplicationGroup", applicationGroup);

        ApplicationIdType applicationId = IdUtils.createApplicationId(appId);
        TenantIdType tenantId = IdUtils.createTenantId(tId);

        //UserGroupUserX

        Map<IdType, UserGroupUserXStruct> userGroupUserXMap = source.getUserGroupUserX().get(applicationId, tenantId);

        checkUserGroupUserXStruct(userGroupUserXMap, groupAId, user100, null);

        checkUserGroupUserXStruct(userGroupUserXMap, groupAId, user101, new AccessControl(
                new boolean[]{true, true, false, false, false
                        , false, false, false, false, false}));

        checkUserGroupUserXStruct(userGroupUserXMap, groupTenantRootId, user100, new AccessControl(
                new boolean[]{true, true, true, true, true
                        , true, true, true, true, true}));

        checkUserGroupUserXStruct(userGroupUserXMap, groupAppAdminId, user100, new AccessControl(
                new boolean[]{true, true, false, false, false
                        , true, true, false, false, false}));

        //--------------------------------------------------------------------------------------------------------------------

        //RoleUserGroupX

        Map<IdType, RoleUserGroupXStruct> singleRoleUserGroupXMap = source.getSingleRoleUserGroupX().get(applicationId, tenantId);
        Map<IdType, RoleUserGroupXStruct> multipleRoleUserGroupXMap = source.getMultipleRoleUserGroupX().get(applicationId, tenantId);

        checkRoleUserGroupXStruct(multipleRoleUserGroupXMap, groupAppAdminId, roleAId, new AccessControl(
                new boolean[]{true, true, false, false, false
                        , false, false, false, false, false})
        );

        checkRoleUserGroupXStruct(singleRoleUserGroupXMap, groupAppAdminId, roleBId, new AccessControl(
                new boolean[]{true, false, true, true, true
                        , true, true, true, true, true})
        );

        checkRoleUserGroupXStruct(singleRoleUserGroupXMap, groupAppAdminId, roleCId, new AccessControl(
                new boolean[]{true, true, false, false, false
                        , false, false, false, false, false})
        );

        checkRoleUserGroupXStruct(singleRoleUserGroupXMap, groupAId, roleBId, new AccessControl(
                new boolean[]{true, true, false, false, false
                        , false, false, false, false, false})
        );

        //--------------------------------------------------------------------------------------------------------------------

        //ScopeUserGroupUserX
        Map<IdType, ScopeUserGroupUserXStruct> scopeUserGroupUserXMap = source.getScopeUserGroupUserX().get(applicationId, tenantId);

        checkScopesOfUserGroupUser(scopeUserGroupUserXMap, groupTenantRootId, user100, scope_null);
        checkScopesOfUserGroupUser(scopeUserGroupUserXMap, groupAppAdminId, user100, scope_a, scope_b, scope_c, scope_null);
        checkScopesOfUserGroupUser(scopeUserGroupUserXMap, groupAId, user101, scope_d);

        //--------------------------------------------------------------------------------------------------------------------

        //UserGroupOtherX
        Map<IdType, UserGroupOtherXStruct> userGroupOtherXMap = source.getUserGroupOtherX().get(applicationId, tenantId);

        checkUserGroupXOther(userGroupOtherXMap, groupTenantRootId, null);
        checkUserGroupXOther(userGroupOtherXMap, groupAppAdminId, new Access(new boolean[]{true, true}));
        checkUserGroupXOther(userGroupOtherXMap, groupAId, null);
    }

    private void checkUserGroupXOther(Map<IdType, UserGroupOtherXStruct> userGroupOtherXMap, String userGroupId, Access access) {
        List<UserGroupOtherXStruct> list = getUserGroupXOther(userGroupOtherXMap, userGroupId);
        if (access != null) {
            Assert.assertEquals(list.size(), 1);
            Assert.assertTrue(AccessControlUtils.matchWithAccess(list.get(0), access));
        } else {
            Assert.assertEquals(list.size(), 0);
        }
    }

    private void checkScopesOfUserGroupUser(Map<IdType, ScopeUserGroupUserXStruct> scopeUserGroupUserXMap
            , String roleId, String userId, String... scopes) {
        Set<String> set = getScopesOfUserGroupUser(scopeUserGroupUserXMap, roleId, userId);
        Assert.assertTrue(TestUtils.match(set, scopes));
    }

    private void checkUserGroupUserXStruct(Map<IdType, UserGroupUserXStruct> userGroupUserXMap
            , String userGroupId, String userId, AccessControl accessControl) {
        List<UserGroupUserXStruct> list = getUserGroupUserXStruct(userGroupUserXMap, userGroupId, userId);
        if (accessControl != null) {
            Assert.assertEquals(list.size(), 1);
            Assert.assertTrue(AccessControlUtils.matchWithAccessControl(list.get(0), accessControl));
        } else {
            Assert.assertEquals(list.size(), 0);
        }
    }

    private void checkRoleUserGroupXStruct(Map<IdType, RoleUserGroupXStruct> roleUserGroupXMap
            , String userGroupId, String roleId, AccessControl accessControl) {
        List<RoleUserGroupXStruct> list = getRoleUserGroupXStruct(roleUserGroupXMap, userGroupId, roleId);
        if (accessControl != null) {
            Assert.assertEquals(list.size(), 1);
            Assert.assertTrue(AccessControlUtils.matchWithAccessControl(list.get(0), accessControl));
        } else {
            Assert.assertEquals(list.size(), 0);
        }
    }

    private List<UserGroupUserXStruct> getUserGroupUserXStruct(Map<IdType, UserGroupUserXStruct> userGroupUserXMap, String userGroupId, String userId) {
        List<UserGroupUserXStruct> result = userGroupUserXMap.
                values().stream().filter(
                o -> o.getUserId().equals(IdUtils.createUserId(userId).getValue())
                        && o.getUserGroupId().equals(IdUtils.createId(userGroupId).getValue()))
                .collect(Collectors.toList());
        return result;
    }

    private List<RoleUserGroupXStruct> getRoleUserGroupXStruct(Map<IdType, RoleUserGroupXStruct> roleUserGroupXMap, String userGroupId, String roleId) {
        List<RoleUserGroupXStruct> result = roleUserGroupXMap.
                values().stream().filter(
                o -> o.getRoleId().equals(IdUtils.createId(roleId).getValue())
                        && o.getUserGroupId().equals(IdUtils.createId(userGroupId).getValue()))
                .collect(Collectors.toList());
        return result;
    }

    private Set<String> getScopesOfUserGroupUser(Map<IdType, ScopeUserGroupUserXStruct> scopeUserGroupUserXMap
            , String roleId, String userId) {
        List<ScopeUserGroupUserXStruct> list = scopeUserGroupUserXMap.
                values().stream().filter(
                o -> o.getUserId().equals(IdUtils.createUserId(userId).getValue())
                        && o.getUserGroupId().equals(IdUtils.createId(roleId).getValue()))
                .collect(Collectors.toList());

        Set<String> result = list.stream().map(ScopeUserGroupUserXPrototype::getScope).collect(Collectors.toSet());
        return result;
    }

    private List<UserGroupOtherXStruct> getUserGroupXOther(Map<IdType, UserGroupOtherXStruct> userGroupOtherXMap, String userGroupId) {
        List<UserGroupOtherXStruct> result = userGroupOtherXMap.
                values().stream().filter(
                o -> o.getUserGroupId().equals(IdUtils.createId(userGroupId).getValue()))
                .collect(Collectors.toList());
        return result;
    }
}





















