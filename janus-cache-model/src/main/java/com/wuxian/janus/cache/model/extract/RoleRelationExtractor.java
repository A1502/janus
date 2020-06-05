package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Application;
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
