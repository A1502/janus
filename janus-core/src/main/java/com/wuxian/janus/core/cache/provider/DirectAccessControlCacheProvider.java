package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.core.basis.StringUtils;
import com.wuxian.janus.entity.*;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.BaseIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;

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

    private String createKey(Class clazz, Boolean single, BaseIdType firstId, BaseIdType second) {
        String singleStr = single == null ? "" : single.toString();
        return StringUtils.safeJoinStrings(clazz.toString(), singleStr, firstId.toString(), second.toString());
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
        provider.fillSourceLoaderMap(source.getScopeSingleRoleUserX(), ScopeRoleUserXEntity.class, true);

        //2
        provider.fillSourceLoaderMap(source.getScopeMultipleRoleUserX(), ScopeRoleUserXEntity.class, false);

        //3
        provider.fillSourceLoaderMap(source.getScopeUserGroupUserX(), ScopeUserGroupUserXEntity.class, null);

        //4
        provider.fillSourceLoaderMap(source.getUserGroup(), UserGroupEntity.class, null);

        //5
        provider.fillSourceLoaderMap(source.getUserGroupUserX(), UserGroupUserXEntity.class, null);

        //6
        provider.fillSourceLoaderMap(source.getSinglePermission(), PermissionEntity.class, true);

        //7
        provider.fillSourceLoaderMap(source.getSingleRolePermissionX(), RolePermissionXEntity.class, true);

        //8
        provider.fillSourceLoaderMap(source.getSingleRole(), RoleEntity.class, true);

        //9
        provider.fillSourceLoaderMap(source.getSingleRoleOtherX(), RoleOtherXEntity.class, true);

        //10
        provider.fillSourceLoaderMap(source.getSingleRoleUserGroupX(), RoleUserGroupXEntity.class, true);

        //11
        provider.fillSourceLoaderMap(source.getSingleRoleUserX(), RoleUserXEntity.class, true);

        //12
        provider.fillSourceLoaderMap(source.getMultiplePermission(), PermissionEntity.class, false);

        //13
        provider.fillSourceLoaderMap(source.getMultipleRolePermissionX(), RolePermissionXEntity.class, false);

        //14
        provider.fillSourceLoaderMap(source.getMultipleRole(), RoleEntity.class, false);

        //15
        provider.fillSourceLoaderMap(source.getMultipleRoleOtherX(), RoleOtherXEntity.class, false);

        //16
        provider.fillSourceLoaderMap(source.getMultipleRoleUserGroupX(), RoleUserGroupXEntity.class, false);

        //17
        provider.fillSourceLoaderMap(source.getMultipleRoleUserX(), RoleUserXEntity.class, false);

        //18
        provider.fillSourceLoaderMap(source.getPermissionTemplate(), PermissionTemplateEntity.class);

        //19
        provider.fillSourceLoaderMap(source.getOuterObjectType(), OuterObjectTypeEntity.class);

        //20
        provider.fillSourceLoaderMap(source.getOuterObject(), OuterObjectEntity.class);

        //21
        provider.fillSourceLoaderMap(source.getUserOuterObjectX(), UserOuterObjectXEntity.class);

        return provider;
    }

    //<editor-fold desc="父类成员">

    @Override
    protected Map<ApplicationIdType, Set<TenantIdType>> loadApplicationIdTenantIdRange() {
        return applicationIdTenantIdRange;
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXEntity> createScopeSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(ScopeRoleUserXEntity.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, ScopeRoleUserXEntity> createScopeMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(ScopeRoleUserXEntity.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, ScopeUserGroupUserXEntity> createScopeUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(ScopeUserGroupUserXEntity.class, null, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, UserGroupEntity> createUserGroupLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(UserGroupEntity.class, null, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, UserGroupUserXEntity> createUserGroupUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(UserGroupUserXEntity.class, null, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, PermissionEntity> createSinglePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(PermissionEntity.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXEntity> createSingleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RolePermissionXEntity.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleEntity> createSingleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleEntity.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXEntity> createSingleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleOtherXEntity.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXEntity> createSingleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleUserGroupXEntity.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleUserXEntity> createSingleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleUserXEntity.class, true, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, PermissionEntity> createMultiplePermissionLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(PermissionEntity.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RolePermissionXEntity> createMultipleRolePermissionXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RolePermissionXEntity.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleEntity> createMultipleRoleLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleEntity.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleOtherXEntity> createMultipleRoleOtherXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleEntity.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleUserGroupXEntity> createMultipleRoleUserGroupXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleUserGroupXEntity.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, RoleUserXEntity> createMultipleRoleUserXLoader(ApplicationIdType applicationId, TenantIdType tenantId) {
        return getFromSourceLoaderMap(RoleUserXEntity.class, false, applicationId, tenantId);
    }

    @Override
    protected SourceLoader<IdType, PermissionTemplateEntity> createPermissionTemplateLoader(ApplicationIdType applicationId) {
        return getFromSourceLoaderMap(RoleUserXEntity.class, null, applicationId, null);
    }

    @Override
    protected SourceLoader<IdType, OuterObjectTypeEntity> createOuterObjectTypeLoader() {
        return getFromSourceLoaderMap(OuterObjectTypeEntity.class, null, null, null);
    }

    @Override
    protected SourceLoader<IdType, OuterObjectEntity> createOuterObjectLoader(IdType outerObjectTypeId) {
        return getFromSourceLoaderMap(OuterObjectEntity.class, null, outerObjectTypeId, null);
    }

    @Override
    protected SourceLoader<IdType, UserOuterObjectXEntity> createUserOuterObjectXLoader(IdType outerObjectTypeId) {
        return getFromSourceLoaderMap(UserOuterObjectXEntity.class, null, outerObjectTypeId, null);
    }

    //</editor-fold>
}
