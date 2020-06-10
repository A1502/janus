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
    @DisplayName("测试RoleUserX多次关系合并")
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

        //测试RoleUserX = roleA User100
        List<RoleUserXStruct> roleAUser100 = getRoleUserXStruct(singleRoleUserXMap, roleAId, user100);
        Assert.assertEquals(roleAUser100.size(), 1);
        Assert.assertTrue(AccessControlUtils.match(new AccessControl(
                        new boolean[]{true, true, false, false, false
                                , false, false, false, false, true})
                , roleAUser100.get(0)));


        //测试RoleUserX = roleB User100
        List<RoleUserXStruct> roleAUser101 = getRoleUserXStruct(singleRoleUserXMap, roleAId, user101);
        Assert.assertEquals(roleAUser101.size(), 0);
        List<RoleUserXStruct> roleBUser100 = getRoleUserXStruct(multipleRoleUserXMap, roleBId, user100);
        Assert.assertEquals(roleBUser100.size(), 1);
        Assert.assertTrue(AccessControlUtils.match(new AccessControl(
                        new boolean[]{true, false, false, false, false
                                , false, false, false, false, false})
                , roleBUser100.get(0)));

        //测试RoleUserX = roleA User101
        List<RoleUserXStruct> roleBUser101 = getRoleUserXStruct(multipleRoleUserXMap, roleBId, user101);
        Assert.assertEquals(roleBUser101.size(), 1);
        Assert.assertTrue(AccessControlUtils.match(new AccessControl(
                        new boolean[]{true, true, false, false, false
                                , false, false, false, false, false})
                , roleBUser101.get(0)));

        //测试RoleUserX = roleAllPermission User100
        List<RoleUserXStruct> roleAllPermissionUser100 = getRoleUserXStruct(singleRoleUserXMap
                , roleAllPermissionId, user100);
        Assert.assertEquals(roleAllPermissionUser100.size(), 1);
        Assert.assertTrue(AccessControlUtils.match(new AccessControl(
                        new boolean[]{true, true, true, true, true
                                , true, true, true, true, true})
                , roleAllPermissionUser100.get(0)));

        //--------------------------------------------------------------------------------------------
        Map<IdType, ScopeRoleUserXStruct> scopeSingleRoleUserXMap = source.getScopeSingleRoleUserX().get(applicationId, tenantId);
        Map<IdType, ScopeRoleUserXStruct> scopeMultipleRoleUserXMap = source.getScopeMultipleRoleUserX().get(applicationId, tenantId);

        //测试ScopeRoleUserX = roleA User100
        Set<String> scopesOfRoleAUser100 = getScopesOfRoleUser(scopeSingleRoleUserXMap, roleAId, user100);
        Assert.assertTrue(match(scopesOfRoleAUser100, new String[]{scope_a, scope_b, scope_c}));

        //测试ScopeRoleUserX = roleB User100
        Set<String> scopesOfRoleBUser100 = getScopesOfRoleUser(scopeMultipleRoleUserXMap, roleBId, user100);
        Assert.assertTrue(match(scopesOfRoleBUser100, new String[]{scope_null}));

        //测试ScopeRoleUserX = roleB User101
        Set<String> scopesOfRoleBUser101 = getScopesOfRoleUser(scopeMultipleRoleUserXMap, roleBId, user101);
        Assert.assertTrue(match(scopesOfRoleBUser101, new String[]{scope_null}));

        //测试ScopeRoleUserX = roleAllPermission User100
        Set<String> scopesOfRoleAllPermissionUser100 = getScopesOfRoleUser(scopeSingleRoleUserXMap, roleAllPermissionId, user100);
        Assert.assertTrue(match(scopesOfRoleAllPermissionUser100, new String[]{scope_d}));

        //--------------------------------------------------------------------------------------------
        //测试RoleOther
        Map<IdType, RoleOtherXStruct> scopeSingleRoleOtherXMap = source.getSingleRoleOtherX().get(applicationId, tenantId);
        List<RoleOtherXStruct> listOfRoleAOther = getRoleOther(scopeSingleRoleOtherXMap, roleAId);
        Assert.assertEquals(listOfRoleAOther.size(), 1);
        Assert.assertTrue(AccessControlUtils.match(new Access(
                        new boolean[]{true, false, false, false, false})
                , listOfRoleAOther.get(0)));
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

    private List<RoleOtherXStruct> getRoleOther(Map<IdType, RoleOtherXStruct> scopeRoleOtherXMap, String roleId) {
        List<RoleOtherXStruct> result = scopeRoleOtherXMap.
                values().stream().filter(
                o -> o.getRoleId().equals(IdUtils.createId(roleId).getValue()))
                .collect(Collectors.toList());
        return result;
    }

    private boolean match(Set<String> set, String[] array) {

        if (set.size() != array.length) {
            return false;
        }

        for (String item : array) {
            if (!set.contains(item)) {
                return false;
            }
        }
        return true;
    }
}





















