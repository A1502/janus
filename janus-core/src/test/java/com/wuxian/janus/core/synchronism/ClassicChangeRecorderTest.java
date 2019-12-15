package com.wuxian.janus.core.synchronism;

import com.wuxian.janus.IdBuilder;
import com.wuxian.janus.core.cache.BaseApplicationCachePool;
import com.wuxian.janus.core.cache.BaseOuterObjectTypeCachePool;
import com.wuxian.janus.core.synchronism.data.AccessControlCacheProvider01;
import com.wuxian.janus.entity.*;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;
import com.wuxian.janus.entity.primary.UserIdType;
import com.wuxian.janus.entity.prototype.base.JanusPrototype;
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

        IdType entityId = cacheProvider.getDefaultEntityId();

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
        List<OuterObjectTypeEntity> typeList = outerObjectTypeCachePool.getOuterObjectTypeMap().getAll();
        List<OuterObjectEntity> outerObjectList = outerObjectTypeCachePool.getOuterObjectTypeCache(entityId).getOuterObject().getAll();
        List<UserOuterObjectXEntity> userOuterObjectList = outerObjectTypeCachePool.getOuterObjectTypeCache(entityId).getUserOuterObjectX().getAll();

        //3.验证
        testList(typeList, entityId, firstLastModifiedBy);
        testList(outerObjectList, entityId, firstLastModifiedBy);
        testList(userOuterObjectList, entityId, firstLastModifiedBy);

        //4-1.模拟更新数据源 =>所有表的数据的最后修改人属性都改为secondLastModifiedBy
        cacheProvider.setLastModifiedBy(secondLastModifiedBy);
        //4-2.更新缓存标志位
        ChangeStatus status = new ChangeStatus();
        status.setOuterObjectTypeStatus(true);
        status.changeOuterObjectTypeCacheStatus(entityId, OuterObjectTypeCacheChangePart.OUTER_OBJECT, true);
        //故意不更新下面这行的USER_OUTER_OBJECT
        //status.changeOuterObjectTypeCacheStatus(firstLastModifiedBy, OuterObjectTypeCacheChangePart.USER_OUTER_OBJECT, true);
        recorder.accept(status);

        //5.刷新缓存
        statusSynchronizer.refresh(applicationId, tenantId, outerObjectTypeCachePool, applicationCachePool);

        //6.再取数据
        typeList = outerObjectTypeCachePool.getOuterObjectTypeMap().getAll();
        outerObjectList = outerObjectTypeCachePool.getOuterObjectTypeCache(entityId).getOuterObject().getAll();
        userOuterObjectList = outerObjectTypeCachePool.getOuterObjectTypeCache(entityId).getUserOuterObjectX().getAll();

        //7-1.再验证，应该lastModifiedBy都是最新的,即secondLastModifiedBy
        testList(typeList, entityId, secondLastModifiedBy);
        testList(outerObjectList, entityId, secondLastModifiedBy);
        //7-2.因为没有更新，所以还是firstLastModifiedBy
        testList(userOuterObjectList, entityId, firstLastModifiedBy);
    }

    @Test
    @DisplayName("数据标志位更新引发缓存更新的测试:Role and Permission")
    public void testRoleAndPermission() {

        ClassicChangeRecorder recorder = new ClassicChangeRecorder();
        AccessControlCacheProvider01 cacheProvider = new AccessControlCacheProvider01();

        IdType entityId = cacheProvider.getDefaultEntityId();

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
        List<RoleEntity> singleRoleList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRole().getAll();
        List<RoleEntity> multipleRoleList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRole().getAll();
        List<PermissionEntity> singlePermissionList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSinglePermission().getAll();
        List<PermissionTemplateEntity> permissionTemplateList = applicationCachePool.getApplicationCache(applicationId).getPermissionTemplateMap().getAll();

        //3.验证
        testList(singleRoleList, entityId, firstLastModifiedBy);
        testList(multipleRoleList, entityId, firstLastModifiedBy);
        testList(singlePermissionList, entityId, firstLastModifiedBy);
        testList(permissionTemplateList, entityId, firstLastModifiedBy);

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
        List<RoleEntity> singleRoleListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRole().getAll();
        List<RoleEntity> multipleRoleListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRole().getAll();
        List<PermissionEntity> singlePermissionListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSinglePermission().getAll();
        List<PermissionTemplateEntity> permissionTemplateListLater = applicationCachePool.getApplicationCache(applicationId).getPermissionTemplateMap().getAll();

        //7-1.再验证，singleRoleLists,multipleRoleLists应该lastModifiedBy是最新的,即secondLastModifiedBy
        testList(singleRoleListLater, entityId, secondLastModifiedBy);
        testList(multipleRoleListLater, entityId, secondLastModifiedBy);
        testList(singlePermissionListLater, entityId, firstLastModifiedBy);
        testList(permissionTemplateListLater, entityId, secondLastModifiedBy);
    }

    @Test
    @DisplayName("数据标志位更新引发缓存更新的测试:All the roles")
    public void testAllRoles() {

        ClassicChangeRecorder recorder = new ClassicChangeRecorder();
        AccessControlCacheProvider01 cacheProvider = new AccessControlCacheProvider01();

        IdType entityId = cacheProvider.getDefaultEntityId();

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
        List<RoleEntity> singleRoleList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRole().getAll();
        List<RoleEntity> multipleRoleList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRole().getAll();

        List<RoleOtherXEntity> singleRoleOtherList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleOtherX().getAll();
        List<RoleOtherXEntity> multipleRoleOtherList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleOtherX().getAll();

        List<RolePermissionXEntity> singleRolePermList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRolePermissionX().getAll();
        List<RolePermissionXEntity> multipleRolePermList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRolePermissionX().getAll();

        List<RoleUserGroupXEntity> singleRoleUserGList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleUserGroupX().getAll();
        List<RoleUserGroupXEntity> multipleRoleUserGList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleUserGroupX().getAll();

        List<RoleUserXEntity> singleRoleUserXList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleUserX().getAll();
        List<RoleUserXEntity> multipleRoleUserXList = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleUserX().getAll();

        //3.验证
        testList(singleRoleList, entityId, firstLastModifiedBy);
        testList(multipleRoleList, entityId, firstLastModifiedBy);

        testList(singleRoleOtherList, entityId, firstLastModifiedBy);
        testList(multipleRoleOtherList, entityId, firstLastModifiedBy);

        testList(singleRolePermList, entityId, firstLastModifiedBy);
        testList(multipleRolePermList, entityId, firstLastModifiedBy);

        testList(singleRoleUserGList, entityId, firstLastModifiedBy);
        testList(multipleRoleUserGList, entityId, firstLastModifiedBy);

        testList(singleRoleUserXList, entityId, firstLastModifiedBy);
        testList(multipleRoleUserXList, entityId, firstLastModifiedBy);

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
        List<RoleEntity> singleRoleListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRole().getAll();
        List<RoleEntity> multipleRoleListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRole().getAll();

        List<RoleOtherXEntity> singleRoleOtherListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleOtherX().getAll();
        List<RoleOtherXEntity> multipleRoleOtherListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleOtherX().getAll();

        List<RolePermissionXEntity> singleRolePermListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRolePermissionX().getAll();
        List<RolePermissionXEntity> multipleRolePermListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRolePermissionX().getAll();

        List<RoleUserGroupXEntity> singleRoleUserGListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleUserGroupX().getAll();
        List<RoleUserGroupXEntity> multipleRoleUserGListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleUserGroupX().getAll();

        List<RoleUserXEntity> singleRoleUserXListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getSingleRoleUserX().getAll();
        List<RoleUserXEntity> multipleRoleUserXListLater = applicationCachePool.getApplicationCache(applicationId).getTenantCache(tenantId).getMultipleRoleUserX().getAll();

        //7-1.再验证，singleRoleLists,multipleRoleLists应该lastModifiedBy是最新的,即secondLastModifiedBy
        testList(singleRoleListLater, entityId, secondLastModifiedBy);
        testList(multipleRoleListLater, entityId, secondLastModifiedBy);

        testList(singleRoleOtherListLater, entityId, secondLastModifiedBy);
        testList(multipleRoleOtherListLater, entityId, secondLastModifiedBy);

        testList(singleRolePermListLater, entityId, secondLastModifiedBy);
        testList(multipleRolePermListLater, entityId, secondLastModifiedBy);

        testList(singleRoleUserGListLater, entityId, secondLastModifiedBy);
        testList(multipleRoleUserGListLater, entityId, secondLastModifiedBy);

        testList(singleRoleUserXListLater, entityId, secondLastModifiedBy);
        testList(multipleRoleUserXListLater, entityId, secondLastModifiedBy);
    }

    <T extends JanusPrototype> void testList(List<T> list, IdType entityId, UserIdType lastModifiedBy) {
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.stream().filter(t -> entityId.getValue().equals(t.getId())
                && lastModifiedBy.getValue().equals(t.getLastModifiedBy())).count(), 1);
    }
}
