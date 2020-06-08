package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Role;
import com.wuxian.janus.cache.model.source.User;
import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.struct.layer1.ScopeRoleUserXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.HashMap;
import java.util.HashSet;
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


        Map<IdType, ScopeRoleUserXStruct> scopeSingleRoleUserX = new HashMap<>();
        Map<IdType, ScopeRoleUserXStruct> scopeMultipleRoleUserX = new HashMap<>();

        for (Role role : roleMap.values()) {

            Map<UserIdType, Set<String>> userIdScopeMap = new HashMap<>();
            Map<UserIdType, AccessControl> userIdAccessControlMap = new HashMap<>();

            for (Map.Entry<User, AccessControl> entry : role.getUsers().entrySet()) {

                UserIdType userId = IdUtils.createUserId(entry.getKey().getId());

                //填充userIdScopeMap
                if (!StrictUtils.containsKey(userIdScopeMap, userId)) {
                    userIdScopeMap.put(userId, new HashSet<>());
                }
                StrictUtils.get(userIdScopeMap, userId).addAll(entry.getKey().getScopes());

                //填充userIdAccessControlMap
                if (!StrictUtils.containsKey(userIdAccessControlMap, userId)) {
                    userIdAccessControlMap.put(userId, entry.getValue());
                } else {
                    StrictUtils.get(userIdAccessControlMap, userId).union(entry.getValue());
                }
            }
        }


        //role.getAccess()
        //(10)SingleRoleOtherX,
        //(16)MultipleRoleOtherX,

        //(1)ScopeSingleRoleUserX,
        //(2)ScopeMultipleRoleUserX
        //(12)SingleRoleUserX
        //(18)MultipleRoleUserX

        //extractScopeRoleUserX(applicationId,tenantId,roleMap);

    }

    private static void extractScopeRoleUserX(ApplicationIdType applicationId
            , TenantIdType tenantId, Role role, Map<UserIdType, Set<String>> userIdScopeMap
            , IdGenerator roleUserXIdGenerator, IdGenerator scopeRoleUserXIdGenerator
            , DirectAccessControlSource result) {


    }
}
