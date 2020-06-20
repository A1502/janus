package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer1.*;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.BaseIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.util.StrictUtils;
import com.wuxian.janus.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DirectAccessControlCacheProvider extends BaseAccessControlCacheProvider {

    /**
     * 所有数据的内存存储区，通过不同的key计算来区隔数据
     */
    private Map<String, SourceLoader> sourceLoaderMap = new HashMap<>();

    private Map<ApplicationIdType, Set<TenantIdType>> applicationIdTenantIdRange = new HashMap<>();

    /**
     * 记录且合并使用过的ApplicationId和TenantId
     *
     * @param input
     */
    private void fillIdRange(Map<ApplicationIdType, Set<TenantIdType>> input) {
        for (ApplicationIdType aId : input.keySet()) {
            Set<TenantIdType> inputTenantIds = StrictUtils.get(input, aId);
            if (inputTenantIds.size() == 0) {
                continue;
            }

            if (!StrictUtils.containsKey(applicationIdTenantIdRange, aId)) {
                applicationIdTenantIdRange.put(aId, new HashSet<>());
            }
            StrictUtils.get(applicationIdTenantIdRange, aId).addAll(inputTenantIds);
        }
    }

    private void fillIdRange(Set<ApplicationIdType> ids) {
        for (ApplicationIdType aId : ids) {
            if (!StrictUtils.containsKey(applicationIdTenantIdRange, aId)) {
                applicationIdTenantIdRange.put(aId, new HashSet<>());
            }
        }
    }

    private String createKey(Class clazz, Boolean single, BaseIdType firstId, BaseIdType secondId) {
        String singleStr = single == null ? "" : single.toString();
        return StringUtils.safeJoinStrings(clazz.toString(), singleStr, firstId.toString(), secondId.toString());
    }

    private <T> T getFromSourceLoaderMap(Class clazz, Boolean single, BaseIdType firstId, BaseIdType secondId) {
        String key = createKey(clazz, single, firstId, secondId);
        if (!StrictUtils.containsKey(sourceLoaderMap, key)) {
            return null;
        } else {
            return (T) StrictUtils.get(sourceLoaderMap, key);
        }
    }

    private <K, V> void fillSourceLoaderMap(TenantMap<K, V> briefMap, Class<V> vClass, Boolean single) {

        Map<ApplicationIdType, Set<TenantIdType>> ids = briefMap.getIds();
        fillIdRange(ids);
        for (ApplicationIdType aId : ids.keySet()) {
            Set<TenantIdType> tenantIds = StrictUtils.get(ids, aId);

            for (TenantIdType tId : tenantIds) {
                Map<K, V> map = briefMap.get(aId, tId);
                String key = createKey(vClass, single, aId, tId);
                sourceLoaderMap.put(key, () -> new HashMap<>(map));
            }
        }
    }

    private <K, V> void fillSourceLoaderMap(ApplicationMap<K, V> briefMap, Class<V> vClass) {

        Set<ApplicationIdType> ids = briefMap.getIds();
        fillIdRange(ids);
        for (ApplicationIdType aId : ids) {
            Map<K, V> map = briefMap.get(aId);
            String key = createKey(vClass, null, aId, null);
            sourceLoaderMap.put(key, () -> new HashMap<>(map));
        }
    }

    private <K, V> void fillSourceLoaderMap(JanusMap<K, V> briefMap, Class<V> vClass) {

        Set<IdType> ids = briefMap.getIds();

        for (IdType id : ids) {
            Map<K, V> map = briefMap.get(id);
            String key = createKey(vClass, null, id, null);
            sourceLoaderMap.put(key, () -> new HashMap<>(map));
        }
    }

    private <K, V> void fillSourceLoaderMap(Map<K, V> map, Class<V> vClass) {
        String key = createKey(vClass, null, null, null);
        sourceLoaderMap.put(key, () -> new HashMap<>(map));
    }

    private DirectAccessControlCacheProvider() {

    }

    public static DirectAccessControlCacheProvider createFrom(DirectAccessControlSource source) {
        DirectAccessControlCacheProvider provider = new DirectAccessControlCacheProvider();

        //1
        provider.fillSourceLoaderMap(source.getScopeSingleRoleUserX(), ScopeRoleUserXStruct.class, true);

        //2
        provider.fillSourceLoaderMap(source.getScopeMultipleRoleUserX(), ScopeRoleUserXStruct.class, false);

        //3
        provider.fillSourceLoaderMap(source.getScopeUserGroupUserX(), ScopeUserGroupUserXStruct.class, null);

        //4
        provider.fillSourceLoaderMap(source.getUserGroup(), UserGroupStruct.class, null);

        //5
        provider.fillSourceLoaderMap(source.getUserGroupUserX(), UserGroupUserXStruct.class, null);

        //6
        provider.fillSourceLoaderMap(source.getUserGroupOtherX(), UserGroupOtherXStruct.class, null);

        //7
        provider.fillSourceLoaderMap(source.getSinglePermission(), PermissionStruct.class, true);

        //8
        provider.fillSourceLoaderMap(source.getSingleRolePermissionX(), RolePermissionXStruct.class, true);

        //9
        provider.fillSourceLoaderMap(source.getSingleRole(), RoleStruct.class, true);

        //10
        provider.fillSourceLoaderMap(source.getSingleRoleOtherX(), RoleOtherXStruct.class, true);

        //11
        provider.fillSourceLoaderMap(source.getSingleRoleUserGroupX(), RoleUserGroupXStruct.class, true);

        //12
        provider.fillSourceLoaderMap(source.getSingleRoleUserX(), RoleUserXStruct.class, true);

        //13
        provider.fillSourceLoaderMap(source.getMultiplePermission(), PermissionStruct.class, false);

        //14
        provider.fillSourceLoaderMap(source.getMultipleRolePermissionX(), RolePermissionXStruct.class, false);

        //15
        provider.fillSourceLoaderMap(source.getMultipleRole(), RoleStruct.class, false);

        //16
        provider.fillSourceLoaderMap(source.getMultipleRoleOtherX(), RoleOtherXStruct.class, false);

        //17
        provider.fillSourceLoaderMap(source.getMultipleRoleUserGroupX(), RoleUserGroupXStruct.class, false);

        //18
        provider.fillSourceLoaderMap(source.getMultipleRoleUserX(), RoleUserXStruct.class, false);

        //19
        provider.fillSourceLoaderMap(source.getPermissionTemplate(), PermissionTemplateStruct.class);

        //20
        provider.fillSourceLoaderMap(source.getOuterObjectType(), OuterObjectTypeStruct.class);

        //21
        provider.fillSourceLoaderMap(source.getOuterObject(), OuterObjectStruct.class);

        //22
        provider.fillSourceLoaderMap(source.getUserOuterObjectX(), UserOuterObjectXStruct.class);

        return provider;
    }

    //<editor-fold desc="父类成员">

    @Override
    protected Map<ApplicationIdType, Set<TenantIdType>> loadApplicationIdTenantIdRange() {
        return applicationIdTenantIdRange;
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(ScopeRoleUserXStruct.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXStruct> createScopeMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(ScopeRoleUserXStruct.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, ScopeUserGroupUserXStruct> createScopeUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(ScopeUserGroupUserXStruct.class, null, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, UserGroupStruct> createUserGroupLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(UserGroupStruct.class, null, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, UserGroupUserXStruct> createUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(UserGroupUserXStruct.class, null, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, UserGroupOtherXStruct> createUserGroupOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(UserGroupOtherXStruct.class, null, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, PermissionStruct> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(PermissionStruct.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXStruct> createSingleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RolePermissionXStruct.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleStruct> createSingleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleStruct.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXStruct> createSingleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleOtherXStruct.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXStruct> createSingleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleUserGroupXStruct.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleUserXStruct> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleUserXStruct.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, PermissionStruct> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(PermissionStruct.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXStruct> createMultipleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RolePermissionXStruct.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleStruct> createMultipleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleStruct.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXStruct> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleStruct.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXStruct> createMultipleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleUserGroupXStruct.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleUserXStruct> createMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleUserXStruct.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, PermissionTemplateStruct> createPermissionTemplateLoader(ApplicationIdType applicationId) {
        return getFromSourceLoaderMap(RoleUserXStruct.class, null, applicationId, null);
    }

    @Override
    protected SourceLoader<IdType, OuterObjectTypeStruct> createOuterObjectTypeLoader() {
        return getFromSourceLoaderMap(OuterObjectTypeStruct.class, null, null, null);
    }

    @Override
    protected SourceLoader<IdType, OuterObjectStruct> createOuterObjectLoader(IdType outerObjectTypeId) {
        return getFromSourceLoaderMap(OuterObjectStruct.class, null, outerObjectTypeId, null);
    }

    @Override
    protected SourceLoader<IdType, UserOuterObjectXStruct> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        return getFromSourceLoaderMap(UserOuterObjectXStruct.class, null, outerObjectTypeId, null);
    }

    //</editor-fold>
}
