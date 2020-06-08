package com.wuxian.janus.cache.model;

import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.core.critical.LevelEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.struct.layer1.RoleUserXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoleTest {

    @Test
    @DisplayName("测试RoleUserX多次关系合并")
    void testMergeRole() {

        String appId = "1";
        String tId = "10";

        String user100 = "100";
        String user101 = "101";

        String roleAId = "80";
        String roleBId = "81";
        String roleAllPermissionId = "88";

        ApplicationGroup applicationGroup = new ApplicationGroup().addItem(
                Application.byId(appId).addItem(
                        Tenant.byId(tId).addItem(
                                Role.byId(roleAId, "roleA", false)
                                        //所有人可见的角色
                                        .setAccess(new Access(new boolean[]{true, false, false, false, false}))
                                        .addItem(
                                                //role关联user默认是THREE
                                                //加入了a,b两个scope
                                                User.byId(user100, "a_scope", "b_scope"),

                                                //new Permission只是用来做陪衬，提高仿真度
                                                new Permission("abc"),
                                                new Permission("def"),
                                                new Permission("xyz")
                                        )
                                        .addItem(
                                                //再次指定role关联user，且指定了AccessControl细节
                                                //加入了a,c两个scope
                                                User.byId(user100, "a_scope", "c_scope")
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
                                        User.byId(user100, "d_scope"), LevelEnum.FULL).addItem(
                                        new Permission("abc"))

                        )
                )
        );

        DirectAccessControlSource source = TestUtils.extractAndPrint("testMergeRole", applicationGroup);

        ApplicationIdType applicationId = IdUtils.createApplicationId(appId);
        TenantIdType tenantId = IdUtils.createTenantId(tId);

        Map<IdType, RoleUserXStruct> singleRoleUserXMap = source.getSingleRoleUserX().get(applicationId, tenantId);
        Map<IdType, RoleUserXStruct> multipleRoleUserXMap = source.getMultipleRoleUserX().get(applicationId, tenantId);

        List<RoleUserXStruct> roleAUser100 = getRoleUserXStruct(singleRoleUserXMap, roleAId, user100);
        Assert.assertEquals(roleAUser100.size(), 1);

        List<RoleUserXStruct> roleAUser101 = getRoleUserXStruct(singleRoleUserXMap, roleAId, user101);
        Assert.assertEquals(roleAUser101.size(), 0);

        List<RoleUserXStruct> roleBUser100 = getRoleUserXStruct(multipleRoleUserXMap, roleBId, user100);
        Assert.assertEquals(roleBUser100.size(), 1);

        List<RoleUserXStruct> roleBUser101 = getRoleUserXStruct(multipleRoleUserXMap, roleBId, user101);
        Assert.assertEquals(roleBUser101.size(), 1);

        List<RoleUserXStruct> roleAllPermissionIdUser100 = getRoleUserXStruct(singleRoleUserXMap
                , roleAllPermissionId, user100);
        Assert.assertEquals(roleAllPermissionIdUser100.size(), 1);
    }

    private List<RoleUserXStruct> getRoleUserXStruct(Map<IdType, RoleUserXStruct> roleUserXMap, String roleId, String userId) {
        List<RoleUserXStruct> result = roleUserXMap.
                values().stream().filter(
                o -> o.getUserId().equals(IdUtils.createUserId(userId).getValue())
                        && o.getRoleId().equals(IdUtils.createId(roleId).getValue()))
                .collect(Collectors.toList());
        return result;
    }
}





















