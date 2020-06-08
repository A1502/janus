package com.wuxian.janus.core.cache;

import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.cache.data.*;
import com.wuxian.janus.core.cache.provider.BaseAccessControlCacheProvider;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.calculate.AccessControlCalculator;
import com.wuxian.janus.core.calculate.PermissionInfo;
import com.wuxian.janus.core.calculate.PermissionPackage;
import com.wuxian.janus.core.calculate.PermissionResult;
import com.wuxian.janus.core.calculate.error.ErrorDataRecorder;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.core.synchronism.ClassicChangeRecorder;
import com.wuxian.janus.struct.layer1.RoleStruct;
import com.wuxian.janus.struct.primary.IdType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AccessControlCalculatorTest Test
 * Date: 2019/08/26
 *
 * @author wuxian
 */
public class AccessControlCalculatorTest {

    Map<Class, AccessControlCalculator> calculatorMap = new HashMap<>();

    private <T extends BaseAccessControlCacheProvider> AccessControlCalculator getCalculator(Class<T> serviceDataType) {
        T data;
        if (!StrictUtils.containsKey(calculatorMap, serviceDataType)) {
            try {
                data = serviceDataType.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
            AccessControlCalculator service = new AccessControlCalculator(data.new OuterObjectCachePool()
                    , data.new ApplicationCachePool()
                    , data.new StatusSynchronizer(new ClassicChangeRecorder()));
            calculatorMap.put(serviceDataType, service);
        }
        return StrictUtils.get(calculatorMap, serviceDataType);
    }

    private List<IdType> fromPermissionInfo(List<PermissionInfo> list) {
        return list.stream().map(o -> new IdType(o.getTemplate().getId()))
                .sorted().collect(Collectors.toList());
    }

    private List<IdType> fromRoleStruct(List<RoleStruct> list) {
        return list.stream().map(o -> new IdType(o.getId()))
                .sorted().collect(Collectors.toList());
    }

    /*
     *    【数据源AccessControlCacheProvider01】
     * 1. 正向获取role-user,execute_Access为true--返回一条role
     * 2. 正向获取role-user,execute_Access为false
     * 3. 反向测试role-user关系表中，roleId不存在--error message 1
     */
    @Test
    @DisplayName("role-user关系")
    public void testRoleUserX() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider01.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role1");
        assertEquals(recorder.errorList.size(), 1);
    }

    /*
     *    【数据源AccessControlCacheProvider02】
     * 1. 正向获取role-other,execute_Access为true--返回一条role
     * 2. 正向获取role-other,execute_Access为false
     * 3. 反向测试role-other关系表中，roleId不存在--error message 1
     */
    @Test
    @DisplayName("role-other关系")
    public void testRoleOtherX() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider02.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role2");
        assertEquals(recorder.errorList.size(), 1);
    }

    /*
     *    【数据源AccessControlCacheProvider03】
     * 1. 正向获取Inner-UserGroup,execute_Access为true
     * 2. 正向获取Inner-UserGroup,execute_Access为false
     * 4. 正向获取RoleUserGroupX,execute_Access为true--返回一条role
     * 5. 正向获取RoleUserGroupX,execute_Access为false
     * 6. 反向测试UserGroupUserXStruct关系表中，GroupId不存在--error message 1
     * 7. 反向测试Inner-UserGroup关系表中，roleId不存在--error message 2
     */
    @Test
    @DisplayName("role-Inner-UserGroup关系")
    public void testInnerUserGroup() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider03.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role1");
        assertEquals(recorder.errorList.size(), 2);
    }

    /*
     *    【数据源AccessControlCacheProvider04】
     * 1. 正向获取RoleUserGroupX,execute_Access为true--返回一条role
     * 2. 正向获取RoleUserGroupX,execute_Access为false
     * 3. 反向测试RoleUserGroupXStruct关系表中，GroupId不存在, 不会报错
     * 4. 反向测试RoleUserGroupXStruct关系表中，RoleId不存在, 会报错-error message 1
     * 5. 反向测试UserOutObject中不存在outObjectId,不会报错
     */
    @Test
    @DisplayName("role-Outer-UserGroup1关系")
    public void testOuterGroup1() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider04.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role1");
        assertEquals(recorder.errorList.size(), 1);
    }

    /*
     *    【数据源AccessControlCacheProvider05】
     * 1. 正向获取RoleUserGroupX,execute_Access为true--返回一条role
     * 2. 正向获取RoleUserGroupX,execute_Access为false
     * 3. 反向测试RoleUserGroupXStruct关系表中，GroupId不存在, 不会报错
     * 4. 反向测试RoleUserGroupXStruct关系表中，RoleId不存在, 会报错--error message 1
     * 5. 反向测试UserOutObject中不存在outObjectId,会报错--error message 2
     * 6. 反向测试outGroupList:"1,2,3,"尾部有,号,不会报错
     */
    @Test
    @DisplayName("role-Outer-UserGroup2关系")
    public void testOuterGroup2() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider05.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role1");
        assertEquals(recorder.errorList.size(), 2);
    }

    /**
     * 【数据源AccessControlCacheProvider06】
     * 1. 反向测试outerObjectTypeId不存在,获取不到role,且根据outGroupList的size大小决定报多少个errors*OuterObjectTypeStruct--当前是2*2=4个error messages
     */
    @Test
    @DisplayName("role-Outer-UserGroup3关系")
    public void testOuterGroup3() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider06.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role1");
        assertEquals(recorder.errorList.size(), 4);
    }

    /*
     *    【数据源AccessControlCacheProvider07】
     * 1. 正向获取role-user,execute_Access为true--返回一条role
     * 2. 正向获取role-user,execute_Access为false
     * 3. 反向测试role-user关系表中，roleId不存在--error message1
     * 4. 反向测试userId与传入的值不一致的情况，不会报错，会直接过滤掉
     */
    @Test
    @DisplayName("Multiple-role-User关系")
    public void testMultipleRoleUserX() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider07.class).getUserExecuteAccessMultipleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role1");
        assertEquals(recorder.errorList.size(), 1);
    }

    /*
     *    【数据源AccessControlCacheProvider08】
     * 1. 正向获取role-other,execute_Access为true--返回一条role
     * 2. 正向获取role-other,execute_Access为false
     * 3. 反向测试role-other关系表中，roleId不存在--error message1
     */
    @Test
    @DisplayName("Multiple-role-Other关系")
    public void testMultipleRoleOtherX() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider08.class).getUserExecuteAccessMultipleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role2");
        assertEquals(recorder.errorList.size(), 1);
    }

    /*
     *    【数据源AccessControlCacheProvider09】
     * 1. 正向获取Inner-UserGroup,execute_Access为true--返回一条role
     * 2. 正向获取Inner-UserGroup,execute_Access为false
     * 4. 正向获取RoleUserGroupX,execute_Access为true
     * 5. 正向获取RoleUserGroupX,execute_Access为false
     * 6. 反向测试UserGroupUserXStruct关系表中，GroupId不存在--error message 1
     * 7. 反向测试outerObject id不存在--error message 2
     * 8. 反向测试role的outerObject id不等于null--error message 3
     * 9. 反向测试Inner-UserGroup关系表中，roleId不存在--error message 4
     */
    @Test
    @DisplayName("Multiple-role-Outer-Group关系")
    public void testMultipleInnerUserGroup() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider09.class).getUserExecuteAccessMultipleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(recorder.errorList.size(), 4);
    }

    /*
     *    【数据源AccessControlCacheProvider17】
     * 1. role-user,role-userGroup,role-other,role-outGroup 4种组合查询roles集合
     * 2. 数据来自前面几个case的组合,且覆盖之前的逻辑分支
     */
    @Test
    @DisplayName("Single-role与User,Inner,Outer-Group,Other关系集合")
    public void testMixRole() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider17.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 4);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role1");
        assertEquals(recorder.errorList.size(), 3);
    }


    /*
     *    【数据源AccessControlCacheProvider10】
     * 1. 用户加入内置用户组获取ag_root角色,从而获得2级的all permission--第一条role
     * 2. 用户组不存在-userGroup2--error message 1
     * 3. 让用户加入一个外部userGroup4--error message 2
     * 4. 用户重复加入两个用户组，都获取了ag_root角色所对应的权限--all permission
     * 5. 用户既加入内部组,又加入外部组的,需要累加在一起返回 --外部组有一条role,total返回2条roles
     * 6. 外部用户组对应的role id不存在--error message 3
     */
    @Test
    @DisplayName("用户加入ag_root获取权限")
    public void testGetAllPermissionRoleByAGRoot() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider10.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 2);
        assertEquals(StrictUtils.get(roles, 0).getCode(), NativeRoleEnum.ALL_PERMISSION.getCode());
        assertEquals(recorder.errorList.size(), 3);
    }

    /*
     *    【数据源AccessControlCacheProvider11】
     * 1. 用户加入内置用户组获取ag_admin角色,从而获得2.5级的all permission --3级控制,自己用--role1
     * 2. 用户组不存在-userGroup2-- error message 1
     * 3. 让用户加入一个外部userGroup4--error message 2
     * 4. 用户既加入内部组,又加入外部组的,需要累加在一起返回 ----外部组有一条role2,total返回2条roles
     * 5. 外部用户组对应的role id不存在--error message 3
     */
    @Test
    @DisplayName("用户加入ag_admin获取权限")
    public void testGetAllPermissionRoleByApplicationMaintainer() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider11.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 2);
        assertEquals(StrictUtils.get(roles, 0).getCode(), NativeRoleEnum.APPLICATION_MAINTAINER.getCode());
        assertEquals(recorder.errorList.size(), 3);
    }

    /*
     *    【数据源AccessControlCacheProvider12】
     * 1. 用户加入内置用户组获取ag_root角色,但由于是Multiple,无法返回all permission
     * 2. 用户组不存在-userGroup2--error message 1
     * 3. 让用户加入一个外部组userGroup4--error message 2
     * 4. 用户既加入内部组,又加入外部组的,需要累加在一起返回 --外部组有一条role,total返回1条role
     * 5. 外部用户组对应的role id不存在--error message 3
     */
    @Test
    @DisplayName("Multiple:用户加入ag_root获取权限")
    public void testGetMultipleAllPermissionRoleByAGRoot() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider12.class).getUserExecuteAccessMultipleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role5");
        assertEquals(recorder.errorList.size(), 3);
    }

    /*
     *    【数据源AccessControlCacheProvider13】
     * 1. 用户加入内置用户组获取ag_admin角色,从而获得2.5级的all permission --3级是只能自己用,但multiple不返回role
     * 2. 用户组不存在-userGroup2-- error message 1
     * 3. 让用户加入一个外部userGroup4--error message 2
     * 4. 用户既加入内部组,又加入外部组的,需要累加在一起返回 ----外部组有一条role,total返回1条role
     * 5. 外部用户组对应的role id不存在--error message 3
     */
    @Test
    @DisplayName("Multiple:用户加入ag_admin获取权限")
    public void testGetMultipleAllPermissionRoleByApplicationMaintainer() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider13.class).getUserExecuteAccessMultipleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 1);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "role5");
        assertEquals(recorder.errorList.size(), 3);
    }

    /*
     *    【数据源AccessControlCacheProvider14】
     * 1. 用户加入内置用户组获取ag_admin角色,从而获得3级的janus_ar:application_maintainer --3级控制,自己用--role1
     * 2. 用户加入内置用户组获取sg_root角色,从而获得2.5级的权限--No error and no role
     * 3. 用户加入内置用户组获取ag_root角色,返回all permission--role2
     * 4. 用户组不存在-userGroup2-- error message 1
     * 5. 让用户加入一个外部userGroup4--error message 2
     * 6. 用户既加入内部组,又加入外部组的,需要累加在一起返回 ----外部组有一条role3,total返回3条roles
     * 7. 外部用户组对应的role id不存在--error message 3
     */
    @Test
    @DisplayName("用户加入ag_root,ag_admin,tg_root角色获取权限")
    public void testGetAllPermissionRoleByApplicationMaintainerAndAGRootAndTGRoot() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles = getCalculator(AccessControlCacheProvider14.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder).getRoles();
        assertEquals(roles.size(), 3);
        assertEquals(StrictUtils.get(roles, 0).getCode(), "janus_ar:all_permission");
        assertEquals(recorder.errorList.size(), 3);
    }

    /*
     *    【数据源AccessControlCacheProvider15】
     *    1.验证singlePermissions计算正确
     *    2.验证multiplePermissions计算正确
     *    3.验证nativePermissions计算正确
     *    4.验证hasAllPermission计算正确
     */

    @DisplayName("通过外部+内部用户组+操作级+数据级角色的多种情况获取权限")
    @Test
    public void testGetPermissionsByOuterInnerUserGroupAndSingleMultipleRole() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();

        AccessControlCalculator serverTmp01 = getCalculator(AccessControlCacheProvider18.class);

        //打印到控制台
        DirectAccessControlSource.dumpFrom(new AccessControlCacheProvider18()).print(System.out);

        //single权限
        PermissionPackage singlePermissions = serverTmp01.getSinglePermission(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder);

        assertIterableEquals(IdBuilder.ids(5, 6, 15, 16, 17), this.fromPermissionInfo(singlePermissions.getPermissions()));
        assertEquals(recorder.errorList.size(), 0);

        //multiple权限
        PermissionPackage multiplePermissions = serverTmp01.getMultiplePermission(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), recorder);
        assertIterableEquals(IdBuilder.ids(19), this.fromPermissionInfo(multiplePermissions.getPermissions()));
        assertEquals(recorder.errorList.size(), 0);

        //native权限(属于single)
        PermissionPackage nativePermissions = serverTmp01.getSinglePermission(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10), null, true, recorder);

        assertIterableEquals(IdBuilder.ids(5, 6), this.fromPermissionInfo(nativePermissions.getPermissions()));
        assertEquals(recorder.errorList.size(), 0);

        //check permission
        PermissionResult permissionResult = serverTmp01.checkPermission(
                IdBuilder.uId(1),
                IdBuilder.aId(1),
                IdBuilder.tId(10),
                IdBuilder.ids(5, 6, 14, 15, 16, 17, 18, 888));

        //虽然18是multiple,但是由于获得了all_permission，因为有为true

        assertNull(permissionResult.getDetail().get(IdBuilder.id(888)));
        long trueCount = permissionResult.getDetail().values()
                .stream()
                .filter(e -> e == null ? false : e)
                .count();

        assertEquals(trueCount, (long) 7);
    }

    /*
     *    【数据源AccessControlCacheProvider15】
     */

    @DisplayName("较为复杂的多种条件下的权限检查")
    @Test
    public void testCheckPermission() {
        AccessControlCalculator serverTmp15 = getCalculator(AccessControlCacheProvider15.class);

        //single权限测试
        PermissionResult singlePermissionResult = serverTmp15.checkPermission(IdBuilder.uId(1),
                IdBuilder.aId(1),
                IdBuilder.tId(10),
                IdBuilder.ids(5, 6, 14, 15, 16, 17, 18, 888));
        assertNull(StrictUtils.get(singlePermissionResult.getDetail(), IdBuilder.id(888)));
        long singleTrueCount = singlePermissionResult.getDetail().values()
                .stream()
                .filter(e -> e == null ? false : e)
                .count();
        assertEquals(singleTrueCount, (long) 5);

        //第一次，multiple权限测试
        //OuterObjectId 12 存在
        PermissionResult multiplePermissionResult0 = serverTmp15.checkPermission(IdBuilder.uId(1),
                IdBuilder.aId(1),
                IdBuilder.tId(10),
                IdBuilder.ids(5, 19, 999), IdBuilder.id(12));
        assertNull(StrictUtils.get(multiplePermissionResult0.getDetail(), IdBuilder.id(999)));
        assertFalse(StrictUtils.get(multiplePermissionResult0.getDetail(), IdBuilder.id(5)));
        long multipleTrueCount0 = multiplePermissionResult0.getDetail().values()
                .stream()
                .filter(e -> e == null ? false : e)
                .count();
        assertEquals(multipleTrueCount0, (long) 1);

        //再次，multiple权限测试
        //OuterObjectId 222并不存在
        PermissionResult multiplePermissionResult1 = serverTmp15.checkPermission(IdBuilder.uId(1),
                IdBuilder.aId(1),
                IdBuilder.tId(10),
                IdBuilder.ids(5, 19, 999), IdBuilder.id(222));
        assertNull(StrictUtils.get(multiplePermissionResult1.getDetail(), IdBuilder.id(999)));
        assertFalse(StrictUtils.get(multiplePermissionResult1.getDetail(), IdBuilder.id(5)));
        long multipleTrueCount1 = multiplePermissionResult1.getDetail().values()
                .stream()
                .filter(e -> e == null ? false : e)
                .count();
        assertEquals(multipleTrueCount1, (long) 0);
    }

    /*
     *    【数据源AccessControlCacheProvider16】
     */
    @DisplayName("测试身份切换:Scope不同获取的Role也不同的效果")
    @Test
    public void testScopeSingle() {
        ErrorDataRecorder recorder = new ErrorDataRecorder();
        List<RoleStruct> roles0 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                new ArrayList<>(), false, recorder).getRoles();
        assertEquals(roles0.size(), 0);
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles1 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                null, false, recorder).getRoles();
        assertEquals(roles1.size(), 3);
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles2 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Arrays.asList(new String[]{null}), false, recorder).getRoles();
        assertEquals(roles2.size(), 1);
        assertEquals(StrictUtils.get(roles2, 0).getId(), IdBuilder.id(4).getValue());
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles3 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Collections.singletonList("B"), false, recorder).getRoles();
        assertEquals(roles3.size(), 1);
        assertEquals(StrictUtils.get(roles3, 0).getId(), IdBuilder.id(3).getValue());
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles4 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Collections.singletonList("A"), false, recorder).getRoles();
        assertEquals(roles4.size(), 1);
        assertEquals(StrictUtils.get(roles4, 0).getId(), IdBuilder.id(4).getValue());
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles5 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Collections.singletonList("Y"), false, recorder).getRoles();
        assertEquals(roles5.size(), 3);
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles6 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Collections.singletonList("X"), false, recorder).getRoles();
        List<IdType> result6 = fromRoleStruct(roles6);
        assertIterableEquals(result6, IdBuilder.ids(3, 4));
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles7 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Arrays.asList("X", "C"), false, recorder).getRoles();
        assertEquals(roles7.size(), 3);
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles8 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Arrays.asList("A", "NOT_EXISTS"), false, recorder).getRoles();
        assertEquals(roles8.size(), 1);
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles9 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Arrays.asList("A", "B", "C", "X", "Y", null), false, recorder).getRoles();
        assertEquals(roles9.size(), 3);
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();


        List<RoleStruct> roles10 = getCalculator(AccessControlCacheProvider16.class).getUserExecuteAccessSingleRoles(IdBuilder.uId(1), IdBuilder.aId(1), IdBuilder.tId(10),
                Arrays.asList("B", "C"), false, recorder).getRoles();
        List<IdType> result10 = roles10.stream().map(o -> new IdType(o.getId())).sorted().collect(Collectors.toList());
        assertIterableEquals(result10, IdBuilder.ids(3, 5));
        assertEquals(recorder.errorList.size(), 0);
        recorder.errorList.clear();
    }
}
