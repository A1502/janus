package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Role;
import com.wuxian.janus.cache.model.source.UserGroup;
import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.core.critical.Access;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.struct.layer1.RoleUserGroupXStruct;
import com.wuxian.janus.struct.layer1.ScopeUserGroupUserXStruct;
import com.wuxian.janus.struct.layer1.UserGroupOtherXStruct;
import com.wuxian.janus.struct.layer1.UserGroupUserXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserGroupRelationAndScopeExtractor {

    public static void extract(TenantMap<IdType, UserGroup> userGroupTenantMap, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {

        IdGenerator userGroupUserXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);
        IdGenerator userGroupOtherXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);
        IdGenerator roleUserGroupXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);
        IdGenerator scopeUserGroupUserXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        ExtractUtils.loopTenantMapElement(userGroupTenantMap,
                ele -> extractUserGroupRelationAndScope(ele.getApplicationId(), ele.getTenantId()
                        , ele.getElement()
                        , userGroupUserXIdGenerator
                        , userGroupOtherXIdGenerator
                        , roleUserGroupXIdGenerator
                        , scopeUserGroupUserXIdGenerator
                        , result));
    }

    private static void extractUserGroupRelationAndScope(ApplicationIdType applicationId
            , TenantIdType tenantId, Map<IdType, UserGroup> userGroupMap
            , IdGenerator userGroupUserXIdGenerator
            , IdGenerator userGroupOtherXIdGenerator
            , IdGenerator roleUserGroupXIdGenerator
            , IdGenerator scopeUserGroupUserXIdGenerator
            , DirectAccessControlSource result) {

        //临时结果
        Map<IdType, ScopeUserGroupUserXStruct> scopeUserGroupUserX = new HashMap<>();
        Map<IdType, UserGroupUserXStruct> userGroupUserX = new HashMap<>();

        Map<IdType, RoleUserGroupXStruct> singleRoleUserGroupX = new HashMap<>();
        Map<IdType, RoleUserGroupXStruct> multipleRoleUserGroupX = new HashMap<>();

        Map<IdType, UserGroupOtherXStruct> userGroupOtherXStruct = new HashMap<>();


        for (UserGroup userGroup : userGroupMap.values()) {

            IdType userGroupId = IdUtils.createId(userGroup.getId());

            Map<UserIdType, Set<String>> userIdScopeMap = ExtractUtils.extractUserScopeMap(userGroup.getUsers());
            Map<UserIdType, AccessControl> userIdAccessControlMap = ExtractUtils.extractUserAccessControlMap((userGroup.getUsers()));

            Map<IdType, AccessControl> singleRoleIdAccessControlMap = extractRoleAccessControlMap(userGroup.getRoles(), false);
            Map<IdType, AccessControl> multipleRoleIdAccessControlMap = extractRoleAccessControlMap(userGroup.getRoles(), true);

            //scopeUserGroupUser
            Map<IdType, ScopeUserGroupUserXStruct> scopeUserGroupUserXPart
                    = createScopeUserGroupUserXStruct(userGroupId, userIdScopeMap, scopeUserGroupUserXIdGenerator);

            //userGroupUser
            Map<IdType, UserGroupUserXStruct> userGroupUserXPart
                    = createUserGroupUserXStruct(userGroupId, userIdAccessControlMap, userGroupUserXIdGenerator);

            //singleRoleUserGroup
            Map<IdType, RoleUserGroupXStruct> singleRoleUserGroupXPart
                    = createRoleUserGroupXStruct(userGroupId, singleRoleIdAccessControlMap, roleUserGroupXIdGenerator);

            Map<IdType, RoleUserGroupXStruct> multipleRoleUserGroupXPart
                    = createRoleUserGroupXStruct(userGroupId, multipleRoleIdAccessControlMap, roleUserGroupXIdGenerator);

            //userGroupOther
            UserGroupOtherXStruct userGroupOtherXStructItem = null;
            IdType userGroupOtherXStructId = null;
            if (userGroup.getAccess() != null) {
                userGroupOtherXStructId = userGroupOtherXIdGenerator.generate();
                userGroupOtherXStructItem = createUserGroupOtherXStruct(userGroupOtherXStructId, userGroupId, userGroup.getAccess());
            }

            //阶段性收录
            scopeUserGroupUserX.putAll(scopeUserGroupUserXPart);
            userGroupUserX.putAll(userGroupUserXPart);
            singleRoleUserGroupX.putAll(singleRoleUserGroupXPart);
            multipleRoleUserGroupX.putAll(multipleRoleUserGroupXPart);

            if (userGroupOtherXStructItem != null) {
                userGroupOtherXStruct.put(userGroupOtherXStructId, userGroupOtherXStructItem);
            }
        }

        //最后收录到result中
        result.getScopeUserGroupUserX().add(applicationId, tenantId, scopeUserGroupUserX);
        result.getUserGroupUserX().add(applicationId, tenantId, userGroupUserX);
        result.getSingleRoleUserGroupX().add(applicationId, tenantId, singleRoleUserGroupX);
        result.getMultipleRoleUserGroupX().add(applicationId, tenantId, multipleRoleUserGroupX);
        result.getUserGroupOtherX().add(applicationId, tenantId, userGroupOtherXStruct);
    }

    private static Map<IdType, UserGroupUserXStruct> createUserGroupUserXStruct(IdType userGroupId
            , Map<UserIdType, AccessControl> userIdAccessControlMap, IdGenerator idGenerator) {
        Map<IdType, UserGroupUserXStruct> result = new HashMap<>();

        for (Map.Entry<UserIdType, AccessControl> entry : userIdAccessControlMap.entrySet()) {
            IdType id = idGenerator.generate();
            AccessControl accessControl = entry.getValue();

            UserGroupUserXStruct item = new UserGroupUserXStruct();
            item.setId(id.getValue());
            item.setUserGroupId(userGroupId.getValue());
            item.setUserId(entry.getKey().getValue());

            accessControl.fill(item);

            result.put(id, item);
        }
        return result;
    }

    private static UserGroupOtherXStruct createUserGroupOtherXStruct(IdType structId
            , IdType userGroupId, Access access) {
        UserGroupOtherXStruct result = new UserGroupOtherXStruct();
        result.setId(structId.getValue());
        result.setUserGroupId(userGroupId.getValue());

        access.fill(result);

        return result;
    }

    //要区分single multiple
    private static Map<IdType, RoleUserGroupXStruct> createRoleUserGroupXStruct(IdType userGroupId
            , Map<IdType, AccessControl> roleIdAccessControlMap, IdGenerator idGenerator) {

        Map<IdType, RoleUserGroupXStruct> result = new HashMap<>();

        for (Map.Entry<IdType, AccessControl> entry : roleIdAccessControlMap.entrySet()) {
            IdType id = idGenerator.generate();
            AccessControl accessControl = entry.getValue();

            RoleUserGroupXStruct item = new RoleUserGroupXStruct();
            item.setId(id.getValue());
            item.setRoleId(entry.getKey().getValue());
            item.setUserGroupId(userGroupId.getValue());
            accessControl.fill(item);
            result.put(id, item);
        }
        return result;
    }

    private static Map<IdType, ScopeUserGroupUserXStruct> createScopeUserGroupUserXStruct(IdType userGroupId
            , Map<UserIdType, Set<String>> userIdScopeMap, IdGenerator idGenerator) {
        Map<IdType, ScopeUserGroupUserXStruct> result = new HashMap<>();

        for (Map.Entry<UserIdType, Set<String>> entry : userIdScopeMap.entrySet()) {
            for (String scope : entry.getValue()) {
                IdType id = idGenerator.generate();

                ScopeUserGroupUserXStruct item = new ScopeUserGroupUserXStruct();
                item.setId(id.getValue());
                item.setUserGroupId(userGroupId.getValue());
                item.setUserId(entry.getKey().getValue());
                item.setScope(scope);
                result.put(id, item);
            }
        }
        return result;
    }

    static Map<IdType, AccessControl> extractRoleAccessControlMap(Map<Role, AccessControl> roles, boolean multiple) {
        Map<IdType, AccessControl> result = new HashMap<>();

        for (Map.Entry<Role, AccessControl> entry : roles.entrySet()) {

            //提取指定multiple的role
            if (multiple == entry.getKey().getMultiple()) {
                IdType roleId = IdUtils.createId(entry.getKey().getId());

                //填充roleIdAccessControlMap
                if (!StrictUtils.containsKey(result, roleId)) {
                    result.put(roleId, entry.getValue());
                } else {
                    StrictUtils.get(result, roleId).union(entry.getValue());
                }
            }
        }
        return result;
    }
}
