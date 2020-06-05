package com.wuxian.janus.core.synchronism;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.IdBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeStatusTest {

    @Test
    @DisplayName("01模拟")
    void test01() {

        ChangeStatus edit = new ChangeStatus();
        //模拟创建一个应用
        edit.setApplicationIdTenantIdRangeStatus(false);
        //模拟为应用创建角色
        edit.changeApplicationStatus(IdBuilder.aId(1), true);
        edit.setApplicationIdTenantIdRangeStatus(true);
        edit.changeTenantStatus(IdBuilder.aId(1), IdBuilder.tId(10), TenantChangePart.SINGLE_ROLE, true);

        ChangeStatus baseLine = new ChangeStatus();
        baseLine.accept(edit);
        List<ApplicationIdType> appIds = baseLine.fetchChangedApplication();
        List<ChangeStatus.TenantChangePartStatus> tenantStatus = baseLine.fetchChangedTenant();

        //判断ApplicationIdTenantIdRangeStatus
        assertTrue(baseLine.getApplicationIdTenantIdRangeStatus());

        //判断Tenant以及关联的application
        assertTrue(tenantStatus.size() == 1);
        assertTrue(tenantStatus.stream().filter(e -> StrictUtils.equals(e.applicationId, IdBuilder.aId(1))
                && StrictUtils.equals(e.tenantId, IdBuilder.tId(10))
                && e.value.contains(TenantChangePart.SINGLE_ROLE))
                .count() == 1);

        //判断application
        assertTrue(appIds.size() == 1);
        assertTrue(appIds.contains(IdBuilder.aId(1)));
    }

    @Test
    @DisplayName("02模拟")
    void test02() {

        ChangeStatus edit = new ChangeStatus();
        //模拟创建一个应用
        edit.setApplicationIdTenantIdRangeStatus(true);
        //模拟为应用创建角色
        edit.changeApplicationStatus(IdBuilder.aId(2), true);
        //假数据
        edit.changeApplicationStatus(IdBuilder.aId(3), true);
        edit.setApplicationIdTenantIdRangeStatus(true);
        edit.changeTenantStatus(IdBuilder.aId(1), IdBuilder.tId(10), TenantChangePart.SINGLE_ROLE, true);
        //假数据
        edit.changeTenantStatus(IdBuilder.aId(10), IdBuilder.tId(1), TenantChangePart.SINGLE_ROLE, false);
        edit.changeTenantStatus(IdBuilder.aId(100), IdBuilder.tId(11), TenantChangePart.USER_GROUP, true);

        ChangeStatus baseLine = new ChangeStatus();
        baseLine.accept(edit);
        List<ApplicationIdType> appIds = baseLine.fetchChangedApplication();
        List<ChangeStatus.TenantChangePartStatus> tenantStatus = baseLine.fetchChangedTenant();

        //判断ApplicationIdTenantIdRangeStatus
        assertTrue(baseLine.getApplicationIdTenantIdRangeStatus());

        //判断Tenant以及关联的application
        assertTrue(tenantStatus.size() == 2);

        assertTrue(tenantStatus.stream().filter(e -> StrictUtils.equals(e.applicationId, IdBuilder.aId(1))
                && StrictUtils.equals(e.tenantId, IdBuilder.tId(10))
                && e.value.contains(TenantChangePart.SINGLE_ROLE))
                .count() == 1);

        assertTrue(tenantStatus.stream().filter(e -> StrictUtils.equals(e.applicationId, IdBuilder.aId(100))
                && StrictUtils.equals(e.tenantId, IdBuilder.tId(11))
                && e.value.contains(TenantChangePart.USER_GROUP))
                .count() == 1);

        //判断application
        assertTrue(appIds.size() == 2);
        assertTrue(appIds.contains(IdBuilder.aId(2)));
        assertTrue(appIds.contains(IdBuilder.aId(3)));
    }

    @Test
    @DisplayName("03模拟")
    void test03() {

        ChangeStatus edit = new ChangeStatus();
        //模拟创建一个应用
        edit.setApplicationIdTenantIdRangeStatus(true);
        //模拟为应用创建角色
        edit.changeApplicationStatus(IdBuilder.aId(2), true);
        edit.changeApplicationStatus(IdBuilder.aId(3), true);
        edit.setApplicationIdTenantIdRangeStatus(true);

        edit.changeTenantStatus(IdBuilder.aId(1), IdBuilder.tId(10), TenantChangePart.MULTIPLE_ROLE, true);
        //假数据
        edit.changeTenantStatus(IdBuilder.aId(10), IdBuilder.tId(1), TenantChangePart.SCOPE_MULTIPLE_ROLE_USER, false);
        edit.changeTenantStatus(IdBuilder.aId(100), IdBuilder.tId(11), TenantChangePart.MULTIPLE_ROLE_USER_GROUP, true);

        edit.changeOuterObjectTypeCacheStatus(IdBuilder.id(1), OuterObjectTypeCacheChangePart.OUTER_OBJECT, true);

        ChangeStatus baseLine = new ChangeStatus();
        baseLine.accept(edit);
        List<ApplicationIdType> appIds = baseLine.fetchChangedApplication();
        List<ChangeStatus.TenantChangePartStatus> tenantStatus = baseLine.fetchChangedTenant();
        Map<IdType, List<OuterObjectTypeCacheChangePart>> outerObject = baseLine.fetchChangedOuterObjectTypeCache();

        //判断ApplicationIdTenantIdRangeStatus
        assertTrue(baseLine.getApplicationIdTenantIdRangeStatus());

        //判断Tenant以及关联的application
        assertTrue(tenantStatus.size() == 2);

        assertTrue(tenantStatus.stream().filter(e -> StrictUtils.equals(e.applicationId, IdBuilder.aId(1))
                && StrictUtils.equals(e.tenantId, IdBuilder.tId(10))
                && e.value.contains(TenantChangePart.MULTIPLE_ROLE))
                .count() == 1);

        assertTrue(tenantStatus.stream().filter(e -> StrictUtils.equals(e.applicationId, IdBuilder.aId(100))
                && StrictUtils.equals(e.tenantId, IdBuilder.tId(11))
                && e.value.contains(TenantChangePart.MULTIPLE_ROLE_USER_GROUP))
                .count() == 1);

        //判断application
        assertTrue(appIds.size() == 2);
        assertTrue(appIds.contains(IdBuilder.aId(2)));
        assertTrue(appIds.contains(IdBuilder.aId(3)));

        //判断outerObject
        assertTrue(outerObject.size() == 1);
        assertTrue(outerObject.values().stream().filter(e -> e.contains(OuterObjectTypeCacheChangePart.OUTER_OBJECT)).count() == 1);
    }

    @Test
    @DisplayName("04模拟")
    void test04() {

        ChangeStatus edit = new ChangeStatus();
        //模拟创建一个应用
        edit.setApplicationIdTenantIdRangeStatus(true);
        //模拟为应用创建角色
        edit.changeApplicationStatus(IdBuilder.aId(2), true);
        edit.changeApplicationStatus(IdBuilder.aId(3), true);
        edit.setApplicationIdTenantIdRangeStatus(false);

        edit.changeTenantStatus(IdBuilder.aId(1), IdBuilder.tId(10), TenantChangePart.MULTIPLE_ROLE, true);
        //重复数据
        edit.changeTenantStatus(IdBuilder.aId(100), IdBuilder.tId(11), TenantChangePart.MULTIPLE_ROLE_USER_GROUP, true);
        edit.changeTenantStatus(IdBuilder.aId(100), IdBuilder.tId(11), TenantChangePart.MULTIPLE_ROLE_USER_GROUP, true);

        edit.changeOuterObjectTypeCacheStatus(IdBuilder.id(1), OuterObjectTypeCacheChangePart.USER_OUTER_OBJECT, true);

        ChangeStatus baseLine = new ChangeStatus();
        baseLine.accept(edit);
        List<ApplicationIdType> appIds = baseLine.fetchChangedApplication();
        List<ChangeStatus.TenantChangePartStatus> tenantStatus = baseLine.fetchChangedTenant();
        Map<IdType, List<OuterObjectTypeCacheChangePart>> outerObject = baseLine.fetchChangedOuterObjectTypeCache();

        //判断ApplicationIdTenantIdRangeStatus
        assertTrue(!baseLine.getApplicationIdTenantIdRangeStatus());

        //判断Tenant以及关联的application
        assertTrue(tenantStatus.size() == 2);

        assertTrue(tenantStatus.stream().filter(e -> StrictUtils.equals(e.applicationId, IdBuilder.aId(1))
                && StrictUtils.equals(e.tenantId, IdBuilder.tId(10))
                && e.value.contains(TenantChangePart.MULTIPLE_ROLE))
                .count() == 1);

        assertTrue(tenantStatus.stream().filter(e -> StrictUtils.equals(e.applicationId, IdBuilder.aId(100))
                && StrictUtils.equals(e.tenantId, IdBuilder.tId(11))
                && e.value.contains(TenantChangePart.MULTIPLE_ROLE_USER_GROUP))
                .count() == 1);

        //判断application
        assertTrue(appIds.size() == 2);
        assertTrue(appIds.contains(IdBuilder.aId(2)));
        assertTrue(appIds.contains(IdBuilder.aId(3)));

        //判断outerObject
        assertTrue(outerObject.size() == 1);
        assertTrue(outerObject.values().stream().filter(e -> e.contains(OuterObjectTypeCacheChangePart.USER_OUTER_OBJECT)).count() == 1);

    }

    @DisplayName("测试TenantChangePart所有参数")
    @ParameterizedTest
    @EnumSource(TenantChangePart.class)
    public void testTenantChangeParts(TenantChangePart testTenantChangePart) {
        assertNotNull(testTenantChangePart);

        ChangeStatus edit = new ChangeStatus();
        edit.changeTenantStatus(IdBuilder.aId(1), IdBuilder.tId(10), testTenantChangePart, true);

        ChangeStatus baseLine = new ChangeStatus();
        baseLine.accept(edit);

        List<ChangeStatus.TenantChangePartStatus> tenantStatus = baseLine.fetchChangedTenant();

        //判断Tenant以及关联的application
        assertTrue(tenantStatus.size() == 1);
        assertTrue(tenantStatus.stream().filter(e -> StrictUtils.equals(e.applicationId, IdBuilder.aId(1))
                && StrictUtils.equals(e.tenantId, IdBuilder.tId(10))
                && e.value.contains(testTenantChangePart))
                .count() == 1);
    }

}
