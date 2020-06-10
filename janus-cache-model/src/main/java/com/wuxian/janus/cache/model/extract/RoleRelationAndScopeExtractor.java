package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Role;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.core.calculate.AccessControlUtils;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.struct.layer1.RoleOtherXStruct;
import com.wuxian.janus.struct.layer1.RoleUserXStruct;
import com.wuxian.janus.struct.layer1.ScopeRoleUserXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 提取role-user,role-userGroup,role-other关系
 */
public class RoleRelationAndScopeExtractor {

    public static void extract(TenantMap<IdType, Role> roleTenantMap, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {

        //经过前面的环节，roleTenantMap里的role都必然已经填充好id和keyFields

        IdGenerator roleUserXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);
        IdGenerator roleOtherXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);
        IdGenerator scopeRoleUserXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        ExtractUtils.loopTenantMapElement(roleTenantMap,
                ele -> extractRoleRelation(ele.getApplicationId(), ele.getTenantId()
                        , ele.getElement()
                        , roleUserXIdGenerator
                        , roleOtherXIdGenerator
                        , scopeRoleUserXIdGenerator
                        , result));
    }

    private static void extractRoleRelation(ApplicationIdType applicationId
            , TenantIdType tenantId, Map<IdType, Role> roleMap
            , IdGenerator roleUserXIdGenerator, IdGenerator roleOtherXIdGenerator
            , IdGenerator scopeRoleUserXIdGenerator, DirectAccessControlSource result) {

        //临时结果
        Map<IdType, ScopeRoleUserXStruct> scopeSingleRoleUserX = new HashMap<>();
        Map<IdType, ScopeRoleUserXStruct> scopeMultipleRoleUserX = new HashMap<>();

        Map<IdType, RoleUserXStruct> singleRoleUserX = new HashMap<>();
        Map<IdType, RoleUserXStruct> multipleRoleUserX = new HashMap<>();

        Map<IdType, RoleOtherXStruct> singleRoleOtherXStruct = new HashMap<>();
        Map<IdType, RoleOtherXStruct> multipleRoleOtherXStruct = new HashMap<>();

        for (Role role : roleMap.values()) {

            IdType roleId = IdUtils.createId(role.getId());

            Map<UserIdType, Set<String>> userIdScopeMap = ExtractUtils.extractUserScopeMap(role.getUsers());
            Map<UserIdType, AccessControl> userIdAccessControlMap = ExtractUtils.extractUserAccessControlMap((role.getUsers()));

            //scopeRoleUser
            Map<IdType, ScopeRoleUserXStruct> scopeRoleUserPart
                    = createScopeRoleUserXStructs(roleId, userIdScopeMap, scopeRoleUserXIdGenerator);
            //roleUser
            Map<IdType, RoleUserXStruct> roleUserPart
                    = createRoleUserXStructs(roleId, userIdAccessControlMap, roleUserXIdGenerator);
            //roleOther
            RoleOtherXStruct roleOtherXStructItem = null;
            IdType roleOtherXStructId = null;
            if (role.getAccess() != null) {
                roleOtherXStructId = roleOtherXIdGenerator.generate();
                roleOtherXStructItem = createRoleOtherXStruct(roleOtherXStructId, roleId, role.getAccess());
            }
            //阶段性收录 by multiple
            if (!role.getMultiple()) {
                scopeSingleRoleUserX.putAll(scopeRoleUserPart);
                singleRoleUserX.putAll(roleUserPart);
                if (roleOtherXStructItem != null) {
                    singleRoleOtherXStruct.put(roleOtherXStructId, roleOtherXStructItem);
                }
            } else {
                scopeMultipleRoleUserX.putAll(scopeRoleUserPart);
                multipleRoleUserX.putAll(roleUserPart);
                if (roleOtherXStructItem != null) {
                    multipleRoleOtherXStruct.put(roleOtherXStructId, roleOtherXStructItem);
                }
            }

        }

        //最后收录到result中
        result.getScopeSingleRoleUserX().add(applicationId, tenantId, scopeSingleRoleUserX);
        result.getScopeMultipleRoleUserX().add(applicationId, tenantId, scopeMultipleRoleUserX);
        result.getSingleRoleUserX().add(applicationId, tenantId, singleRoleUserX);
        result.getMultipleRoleUserX().add(applicationId, tenantId, multipleRoleUserX);
        result.getSingleRoleOtherX().add(applicationId, tenantId, singleRoleOtherXStruct);
        result.getMultipleRoleOtherX().add(applicationId, tenantId, multipleRoleOtherXStruct);
    }

    private static Map<IdType, ScopeRoleUserXStruct> createScopeRoleUserXStructs(IdType roleId
            , Map<UserIdType, Set<String>> userIdScopeMap, IdGenerator idGenerator) {
        Map<IdType, ScopeRoleUserXStruct> result = new HashMap<>();

        for (Map.Entry<UserIdType, Set<String>> entry : userIdScopeMap.entrySet()) {
            for (String scope : entry.getValue()) {
                IdType id = idGenerator.generate();

                ScopeRoleUserXStruct item = new ScopeRoleUserXStruct();
                item.setId(id.getValue());
                item.setRoleId(roleId.getValue());
                item.setUserId(entry.getKey().getValue());
                item.setScope(scope);
                result.put(id, item);
            }
        }
        return result;
    }

    private static Map<IdType, RoleUserXStruct> createRoleUserXStructs(IdType roleId
            , Map<UserIdType, AccessControl> userIdAccessControlMap, IdGenerator idGenerator) {
        Map<IdType, RoleUserXStruct> result = new HashMap<>();

        for (Map.Entry<UserIdType, AccessControl> entry : userIdAccessControlMap.entrySet()) {
            IdType id = idGenerator.generate();
            AccessControl accessControl = entry.getValue();

            RoleUserXStruct item = new RoleUserXStruct();
            item.setId(id.getValue());
            item.setRoleId(roleId.getValue());
            item.setUserId(entry.getKey().getValue());

            AccessControlUtils.fillByAccessControl(item, accessControl);

            result.put(id, item);
        }
        return result;
    }

    private static RoleOtherXStruct createRoleOtherXStruct(IdType structId, IdType roleId
            , Access access) {
        RoleOtherXStruct result = new RoleOtherXStruct();
        result.setId(structId.getValue());
        result.setRoleId(roleId.getValue());

        AccessControlUtils.fillByAccess(result, access);

        return result;
    }
}
