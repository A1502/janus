package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.Application;
import com.wuxian.janus.cache.source.model.ApplicationGroup;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;

/**
 * 提取role-user,role-userGroup,role-other关系
 */
public class RoleRelationExtractor {
    public static void extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {
        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractRoleRelation(application, idGenerator, result);
        }
    }

    private static void extractRoleRelation(Application application, IdGenerator idGenerator, DirectAccessControlSource result) {
        
    }
}
