package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Application;
import com.wuxian.janus.cache.model.source.Role;
import com.wuxian.janus.cache.model.source.Tenant;
import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.List;
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

        Map<ApplicationIdType, Set<TenantIdType>> map = roleTenantMap.getIds();
        for (ApplicationIdType applicationId : map.keySet()) {

            Set<TenantIdType> tenantIds = StrictUtils.get(map, applicationId);
            for (TenantIdType tenantId : tenantIds) {
                Map<IdType, Role> roleMap = roleTenantMap.get(applicationId, tenantId);
                extractRoleRelation(applicationId, tenantId, roleMap
                        , roleUserXIdGenerator, roleOtherXIdGenerator, result);
            }
        }
    }

    private static void extractRoleRelation(ApplicationIdType applicationId
            , TenantIdType tenantId, Map<IdType, Role> roleMap
            , IdGenerator roleUserXIdGenerator, IdGenerator roleOtherXIdGenerator
            , DirectAccessControlSource result) {

        //TOOD


    }
}
