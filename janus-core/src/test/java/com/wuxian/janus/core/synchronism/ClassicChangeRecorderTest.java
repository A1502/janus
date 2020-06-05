package com.wuxian.janus.core.synchronism;

import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.cache.BaseApplicationCachePool;
import com.wuxian.janus.core.cache.BaseOuterObjectTypeCachePool;
import com.wuxian.janus.core.synchronism.data.AccessControlCacheProvider01;
import com.wuxian.janus.struct.layer1.*;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.primary.UserIdType;
import com.wuxian.janus.struct.prototype.JanusPrototype;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ClassicChangeRecorderTest {

    private ApplicationIdType applicationId = IdBuilder.aId(1);
    private TenantIdType tenantId = IdBuilder.tId(10);
    private UserIdType firstLastModifiedBy = IdBuilder.uId(5);
    private UserIdType secondLastModifiedBy = IdBuilder.uId(8);

    @Test
    @DisplayName("数据标志位更新引发缓存更新的测试:OuterObject")
    void testOuterObject() {
        ClassicChangeRecorder recorder = new ClassicChangeRecorder();
        AccessControlCacheProvider01 cacheProvider = new AccessControlCacheProvider01();

        IdType structId = cacheProvider.getDefaultStructId();

        //outerObject数据源
        BaseOuterObjectTypeCachePool outerObjectTypeCachePool = cacheProvider.new OuterObjectCachePool();
        //applicationTenant数据源
        BaseApplicationCachePool applicationCachePool = cacheProvider.new ApplicationCachePool();
        //状态同步器
        BaseStatusSynchronizer statusSynchronizer = cacheProvider.new StatusSynchronizer(recorder);

        //1.准备数据，=>所有表的数据的最后修改人属性都改为firstLastModifiedBy
        cacheProvider.setLastModifiedBy(firstLastModifiedBy);

        //1.首次刷新，将触发数据全部加载的动作，观察控制台日志
        statusSynchronizer.refresh(applicationId, tenantId, outerObjectTypeCachePool, applicationCachePool);

        //2.先取数据出来，id应该为5【因为cacheProvider.setLastModifiedBy(5)】
        List<OuterObjectTypeStruct> typeList = outerObjectTypeCachePool.getOuterObjectTypeMap().getAll();
        List<OuterObjectStruct> outerObjectList = outerObjectTypeCachePool.getOuterObjectTypeCache(structId).getOuterObject().getAll();
        List<UserOuterObjectXStruct> userOuterObjectList = outerObjectTypeCachePool.getOuterObjectTypeCache(structId).getUserOuterObjectX().getAll();

        //3.验证
        testList(typeList, structId, firstLastModifiedBy);
        testList(outerObjectList, structId, firstLastModifiedBy);
        testList(userOuterObjectList, structId, firstLastModifiedBy);

        //4-1.模拟更新数据源 =>所有表的数据的最后修改人属性都改为secondLastModifiedBy
        cacheProvider.setLastModifiedBy(secondLastModifiedBy);
        //4-2.更新缓存标志位
        ChangeStatus status = new ChangeStatus();
        status.setOuterObjectTypeStatus(true);
        status.changeOuterObjectTypeCacheStatus(structId, OuterObjectTypeCacheChangePart.OUTER_OBJECT, true);
        //故意不更新下面这行的USER_OUTER_OBJECT
        //status.changeOuterObjectTypeCacheStatus(firstLastModifiedBy, OuterObjectTypeCacheChangePart.USER_OUTER_OBJECT, true);
        recorder.accept(status);

        //5.刷新缓存
        statusSynchronizer.refresh(applicationId, tenantId, outerObjectTypeCachePool, applicationCachePool);

        //6.再取数据
        typeList = outerObjectTypeCachePool.getOuterObjectTypeMap().getAll();
        outerObjectList = outerObjectTypeCachePool.getOuterObjectTypeCache(structId).getOuterObject().getAll();
        userOuterObjectList = outerObjectTypeCachePool.getOuterObjectTypeCache(structId).getUserOuterObjectX().getAll();

        //7-1.再验证，应该lastModifiedBy都是最新的,即secondLastModifiedBy
        testList(typeList, structId, secondLastModifiedBy);
        testList(outerObjectList, structId, secondLastModifiedBy);
        //7-2.因为没有更新，所以还是firstLastModifiedBy
        testList(userOuterObjectList, structId, firstLastModifiedBy);
    }

    @Test
    @DisplayName("数据标志位更新引发缓存更新的测试:Role and Permission")
    public void testRoleAndPermission() {

        ClassicChangeRecorder recorder = new ClassicChangeRecorder();
        AccessControlCacheProvider01 cacheProvider = new AccessControlCacheProvider01();

        IdType structId = cacheProvider.getDefaultStructId();

        //outerObject数据源
        BaseOuterObjectTypeCachePool outerObjectTypeCachePool = cacheProvider.new OuterObjectCachePool();
        //applicationTenant数据源
        BaseApplicationCachePool applicationCachePool = cacheProvider.new ApplicationCachePool();
        //状态同步器
        BaseStatusSynchronizer statusSynchronizer = cacheProvider.new StatusSynchronizer(recorder);

        //1.准备数据，=>所有表的数据的最后修改人属性都改为firstLastModifiedBy
        cacheProvider.setLastModifiedBy(firstLastModifiedBy);

        //1.首次刷新，将触发数据全部加载的动作，观察控制台日志
        statusSynchronizer.refresh(applicationId, tenantId, outerObjectTypeCachePool, applicationCachePool);

        //2.先取数据出来，id应该为5【因为cacheProvider.setLastModifiedBy(5)】
        List<RoleStruct> singleRoleList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRole().getAll();
        List<RoleStruct> multipleRoleList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRole().getAll();
        List<PermissionStruct> singlePermissionList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSinglePermission().getAll();
        List<PermissionTemplateStruct> permissionTemplateList = applicationCachePool.getApplicationCache(applicationId).getPermissionTemplateMap().getAll();

        //3.验证
        testList(singleRoleList, structId, firstLastModifiedBy);
        testList(multipleRoleList, structId, firstLastModifiedBy);
        testList(singlePermissionList, structId, firstLastModifiedBy);
        testList(permissionTemplateList, structId, firstLastModifiedBy);

        //4-1.模拟更新数据源 =>所有表的数据的最后修改人属性都改为secondLastModifiedBy
        cacheProvider.setLastModifiedBy(secondLastModifiedBy);
        //4-2.更新缓存标志位
        ChangeStatus status = new ChangeStatus();
        status.changeApplicationStatus(applicationId, true);


        //故意重复设置
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_ROLE, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_ROLE, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.MULTIPLE_ROLE, true);
        status.setApplicationIdTenantIdRangeStatus(true);

        //故意不更新下面这行的SINGLE_PERMISSION
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_PERMISSION, false);
        recorder.accept(status);

        //故意接受后,再设置--应该是无效的
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_PERMISSION, true);

        //5.刷新缓存
        statusSynchronizer.refresh(applicationId, tenantId, outerObjectTypeCachePool, applicationCachePool);

        //5.1 refresh后再设置,然后再接受基线,但不在refresh,此时应该是无效的
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_PERMISSION, true);
        recorder.accept(status);

        //6.再取数据出来，id应该为8【因为cacheProvider.setLastModifiedBy(8)】
        List<RoleStruct> singleRoleListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRole().getAll();
        List<RoleStruct> multipleRoleListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRole().getAll();
        List<PermissionStruct> singlePermissionListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSinglePermission().getAll();
        List<PermissionTemplateStruct> permissionTemplateListLater = applicationCachePool.getApplicationCache(applicationId).getPermissionTemplateMap().getAll();

        //7-1.再验证，singleRoleLists,multipleRoleLists应该lastModifiedBy是最新的,即secondLastModifiedBy
        testList(singleRoleListLater, structId, secondLastModifiedBy);
        testList(multipleRoleListLater, structId, secondLastModifiedBy);
        testList(singlePermissionListLater, structId, firstLastModifiedBy);
        testList(permissionTemplateListLater, structId, secondLastModifiedBy);
    }

    @Test
    @DisplayName("数据标志位更新引发缓存更新的测试:All the roles")
    public void testAllRoles() {

        ClassicChangeRecorder recorder = new ClassicChangeRecorder();
        AccessControlCacheProvider01 cacheProvider = new AccessControlCacheProvider01();

        IdType structId = cacheProvider.getDefaultStructId();

        //outerObject数据源
        BaseOuterObjectTypeCachePool outerObjectTypeCachePool = cacheProvider.new OuterObjectCachePool();
        //applicationTenant数据源
        BaseApplicationCachePool applicationCachePool = cacheProvider.new ApplicationCachePool();
        //状态同步器
        BaseStatusSynchronizer statusSynchronizer = cacheProvider.new StatusSynchronizer(recorder);

        //1.准备数据，=>所有表的数据的最后修改人属性都改为firstLastModifiedBy
        cacheProvider.setLastModifiedBy(firstLastModifiedBy);

        //1.首次刷新，将触发数据全部加载的动作，观察控制台日志
        statusSynchronizer.refresh(applicationId, tenantId, outerObjectTypeCachePool, applicationCachePool);

        //2.先取数据出来，id应该为5【因为cacheProvider.setLastModifiedBy(5)】
        List<RoleStruct> singleRoleList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRole().getAll();
        List<RoleStruct> multipleRoleList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRole().getAll();

        List<RoleOtherXStruct> singleRoleOtherList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleOtherX().getAll();
        List<RoleOtherXStruct> multipleRoleOtherList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleOtherX().getAll();

        List<RolePermissionXStruct> singleRolePermList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRolePermissionX().getAll();
        List<RolePermissionXStruct> multipleRolePermList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRolePermissionX().getAll();

        List<RoleUserGroupXStruct> singleRoleUserGList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleUserGroupX().getAll();
        List<RoleUserGroupXStruct> multipleRoleUserGList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleUserGroupX().getAll();

        List<RoleUserXStruct> singleRoleUserXList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleUserX().getAll();
        List<RoleUserXStruct> multipleRoleUserXList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleUserX().getAll();

        //3.验证
        testList(singleRoleList, structId, firstLastModifiedBy);
        testList(multipleRoleList, structId, firstLastModifiedBy);

        testList(singleRoleOtherList, structId, firstLastModifiedBy);
        testList(multipleRoleOtherList, structId, firstLastModifiedBy);

        testList(singleRolePermList, structId, firstLastModifiedBy);
        testList(multipleRolePermList, structId, firstLastModifiedBy);

        testList(singleRoleUserGList, structId, firstLastModifiedBy);
        testList(multipleRoleUserGList, structId, firstLastModifiedBy);

        testList(singleRoleUserXList, structId, firstLastModifiedBy);
        testList(multipleRoleUserXList, structId, firstLastModifiedBy);

        //4-1.模拟更新数据源 =>所有表的数据的最后修改人属性都改为secondLastModifiedBy
        cacheProvider.setLastModifiedBy(secondLastModifiedBy);
        //4-2.更新缓存标志位
        ChangeStatus status = new ChangeStatus();
        status.changeApplicationStatus(applicationId, true);


        //故意重复设置
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_ROLE, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.MULTIPLE_ROLE, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_ROLE_USER, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.MULTIPLE_ROLE_USER, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_ROLE_OTHER, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.MULTIPLE_ROLE_OTHER, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_ROLE_USER_GROUP, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.MULTIPLE_ROLE_USER_GROUP, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_ROLE_PERMISSION, true);
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.MULTIPLE_ROLE_PERMISSION, true);
        status.setApplicationIdTenantIdRangeStatus(true);

        //故意不更新下面这行的SINGLE_PERMISSION
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_PERMISSION, false);
        recorder.accept(status);

        //故意接受后,再设置--应该是无效的
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_PERMISSION, true);

        //5.刷新缓存
        statusSynchronizer.refresh(applicationId, tenantId, outerObjectTypeCachePool, applicationCachePool);

        //5.1 refresh后再设置,然后再接受基线,但不在refresh,此时应该是无效的
        status.changeTenantStatus(applicationId, tenantId, TenantChangePart.SINGLE_PERMISSION, true);
        recorder.accept(status);

        //6.再取数据出来，id应该为8【因为cacheProvider.setLastModifiedBy(8)】
        List<RoleStruct> singleRoleListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRole().getAll();
        List<RoleStruct> multipleRoleListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRole().getAll();

        List<RoleOtherXStruct> singleRoleOtherListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleOtherX().getAll();
        List<RoleOtherXStruct> multipleRoleOtherListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleOtherX().getAll();

        List<RolePermissionXStruct> singleRolePermListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRolePermissionX().getAll();
        List<RolePermissionXStruct> multipleRolePermListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRolePermissionX().getAll();

        List<RoleUserGroupXStruct> singleRoleUserGListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleUserGroupX().getAll();
        List<RoleUserGroupXStruct> multipleRoleUserGListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleUserGroupX().getAll();

        List<RoleUserXStruct> singleRoleUserXListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleUserX().getAll();
        List<RoleUserXStruct> multipleRoleUserXListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleUserX().getAll();

        //7-1.再验证，singleRoleLists,multipleRoleLists应该lastModifiedBy是最新的,即secondLastModifiedBy
        testList(singleRoleListLater, structId, secondLastModifiedBy);
        testList(multipleRoleListLater, structId, secondLastModifiedBy);

        testList(singleRoleOtherListLater, structId, secondLastModifiedBy);
        testList(multipleRoleOtherListLater, structId, secondLastModifiedBy);

        testList(singleRolePermListLater, structId, secondLastModifiedBy);
        testList(multipleRolePermListLater, structId, secondLastModifiedBy);

        testList(singleRoleUserGListLater, structId, secondLastModifiedBy);
        testList(multipleRoleUserGListLater, structId, secondLastModifiedBy);

        testList(singleRoleUserXListLater, structId, secondLastModifiedBy);
        testList(multipleRoleUserXListLater, structId, secondLastModifiedBy);
    }

    <T extends JanusPrototype> void testList(List<T> list, IdType structId, UserIdType lastModifiedBy) {
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.stream().filter(t -> structId.getValue().equals(t.getId())
                && lastModifiedBy.getValue().equals(t.getLastModifiedBy())).count(), 1);
    }
}
