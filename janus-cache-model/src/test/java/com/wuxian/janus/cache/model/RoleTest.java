package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.calculate.AccessControlUtils;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.core.critical.LevelEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.struct.layer1.RoleOtherXStruct;
import com.wuxian.janus.struct.layer1.RoleUserXStruct;
import com.wuxian.janus.struct.layer1.ScopeRoleUserXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.prototype.layer1.ScopeRoleUserXPrototype;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleTest {

    @Test
    @DisplayName("测试Role&User&Other实战多次关系合并")
    void testMergeRoleByApplicationGroup() {

        String appId = "1";
        String tId = "10";

        String user100 = "100";
        String user101 = "101";

        String roleAId = "80";
        String roleBId = "81";
        String roleAllPermissionId = "88";

        String scope_a = "scope_a";
        String scope_b = "scope_b";
        String scope_c = "scope_c";
        String scope_d = "scope_d";
        String scope_null = null;

        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                Application.byId(appId).addItem(
                        Tenant.byId(tId).addItem(
                                Role.byId(roleAId, "roleA", false)
                                        //所有人可见的角色
                                        .setAccess(new Access(new boolean[]{true, false, false, false, false}))
                                        .addItem(
                                                //role关联user默认是THREE
                                                //加入了a,b两个scope
                                                User.byId(user100, scope_a, scope_b),

                                                //new Permission只是用来做陪衬，提高仿真度
                                                new Permission("abc"),
                                                new Permission("def"),
                                                new Permission("xyz")
                                        )
                                        .addItem(
                                                //再次指定role关联user，且指定了AccessControl细节
                                                //加入了a,c两个scope
                                                User.byId(user100, scope_a, scope_c)
                                                , new AccessControl(new boolean[]{false, false, false, false, false
                                                        , false, false, false, false, true})),

                                Role.byId(roleBId, "roleB", true).addItem(
                                        User.byId(user101),
                                        new Permission("m0", "type0", "outer0"),
                                        new Permission("m1", "type0", "outer1")
                                ).addItem(
                                        //user 100关联到roleB,scope是null
                                        User.byId(user100), LevelEnum.FOUR),
                                Role.byId(roleAllPermissionId, NativeRoleEnum.ALL_PERMISSION).addItem(
                                        //这个故意测试user 100能完全控制这个native role
                                        //经过考虑觉得超过ag:root级别的能力也是可以的,没什么不妥
                                        //由于完全控制，可能会执行删除ALL_PERMISSION,
                                        //删除后会无法正常运行
                                        User.byId(user100, scope_d), LevelEnum.FULL).addItem(
                                        new Permission("abc"))

                        )
                )
        );

        DirectAccessControlSource source = TestUtils.extractAndPrint("testMergeRoleByApplicationGroup", applicationGroup);

        ApplicationIdType applicationId = IdUtils.createApplicationId(appId);
        TenantIdType tenantId = IdUtils.createTenantId(tId);

        Map<IdType, RoleUserXStruct> singleRoleUserXMap = source.getSingleRoleUserX().get(applicationId, tenantId);
        Map<IdType, RoleUserXStruct> multipleRoleUserXMap = source.getMultipleRoleUserX().get(applicationId, tenantId);

        checkRoleUserXStruct(singleRoleUserXMap, roleAId, user100, new AccessControl(
                new boolean[]{true, true, false, false, false
                        , false, false, false, false, true}));

        checkRoleUserXStruct(singleRoleUserXMap, roleAId, user101, null);

        checkRoleUserXStruct(multipleRoleUserXMap, roleBId, user100, new AccessControl(
                new boolean[]{true, false, false, false, false
                        , false, false, false, false, false}));


        checkRoleUserXStruct(multipleRoleUserXMap, roleBId, user101, new AccessControl(
                new boolean[]{true, true, false, false, false
                        , false, false, false, false, false}));

        checkRoleUserXStruct(singleRoleUserXMap, roleAllPermissionId, user100, new AccessControl(
                new boolean[]{true, true, true, true, true
                        , true, true, true, true, true}));

        //--------------------------------------------------------------------------------------------
        Map<IdType, ScopeRoleUserXStruct> scopeSingleRoleUserXMap = source.getScopeSingleRoleUserX().get(applicationId, tenantId);
        Map<IdType, ScopeRoleUserXStruct> scopeMultipleRoleUserXMap = source.getScopeMultipleRoleUserX().get(applicationId, tenantId);

        checkScopesOfRoleUser(scopeSingleRoleUserXMap, roleAId, user100, scope_a, scope_b, scope_c);

        checkScopesOfRoleUser(scopeMultipleRoleUserXMap, roleBId, user100, scope_null);

        checkScopesOfRoleUser(scopeMultipleRoleUserXMap, roleBId, user101, scope_null);

        checkScopesOfRoleUser(scopeSingleRoleUserXMap, roleAllPermissionId, user100, scope_d);

        //--------------------------------------------------------------------------------------------
        //测试RoleOther
        Map<IdType, RoleOtherXStruct> scopeSingleRoleOtherXMap = source.getSingleRoleOtherX().get(applicationId, tenantId);
        List<RoleOtherXStruct> listOfRoleAOther = getRoleOther(scopeSingleRoleOtherXMap, roleAId);
        Assert.assertEquals(listOfRoleAOther.size(), 1);
        Assert.assertTrue(AccessControlUtils.matchWithAccess(listOfRoleAOther.get(0), new Access(
                new boolean[]{true, false, false, false, false})));
    }

    private void checkScopesOfRoleUser(Map<IdType, ScopeRoleUserXStruct> scopeRoleUserXMap
            , String roleId, String userId, String... scopes) {
        Set<String> scopesOfRoleAllPermissionUser100 = getScopesOfRoleUser(scopeRoleUserXMap, roleId, userId);
        Assert.assertTrue(TestUtils.match(scopesOfRoleAllPermissionUser100, scopes));
    }

    private void checkRoleUserXStruct(Map<IdType, RoleUserXStruct> roleUserXMap
            , String roleId, String userId, AccessControl accessControl) {
        List<RoleUserXStruct> list = getRoleUserXStruct(roleUserXMap, roleId, userId);
        if (accessControl != null) {
            Assert.assertEquals(list.size(), 1);
            Assert.assertTrue(AccessControlUtils.matchWithAccessControl(list.get(0), accessControl));
        } else {
            Assert.assertEquals(list.size(), 0);
        }
    }

    private List<RoleUserXStruct> getRoleUserXStruct(Map<IdType, RoleUserXStruct> roleUserXMap, String roleId, String userId) {
        List<RoleUserXStruct> result = roleUserXMap.
                values().stream().filter(
                o -> o.getUserId().equals(IdUtils.createUserId(userId).getValue())
                        && o.getRoleId().equals(IdUtils.createId(roleId).getValue()))
                .collect(Collectors.toList());
        return result;
    }

    private Set<String> getScopesOfRoleUser(Map<IdType, ScopeRoleUserXStruct> scopeRoleUserXMap
            , String roleId, String userId) {
        List<ScopeRoleUserXStruct> list = scopeRoleUserXMap.
                values().stream().filter(
                o -> o.getUserId().equals(IdUtils.createUserId(userId).getValue())
                        && o.getRoleId().equals(IdUtils.createId(roleId).getValue()))
                .collect(Collectors.toList());

        Set<String> result = list.stream().map(ScopeRoleUserXPrototype::getScope).collect(Collectors.toSet());
        return result;
    }

    private List<RoleOtherXStruct> getRoleOther(Map<IdType, RoleOtherXStruct> roleOtherXMap, String roleId) {
        List<RoleOtherXStruct> result = roleOtherXMap.
                values().stream().filter(
                o -> o.getRoleId().equals(IdUtils.createId(roleId).getValue()))
                .collect(Collectors.toList());
        return result;
    }
}





















