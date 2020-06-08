package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Role;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.Map;

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

        //TODO
    }
}
