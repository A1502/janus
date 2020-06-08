package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.UserGroup;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.Map;

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
            , TenantIdType tenantId, Map<IdType, UserGroup> roleMap
            , IdGenerator userGroupUserXIdGenerator
            , IdGenerator userGroupOtherXIdGenerator
            , IdGenerator roleUserGroupXIdGenerator
            , IdGenerator scopeUserGroupUserXIdGenerator
            , DirectAccessControlSource result) {
    }
}
