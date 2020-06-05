package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.cache.model.source.Tenant;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Application;
import com.wuxian.janus.cache.model.source.UserGroup;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.struct.layer1.UserGroupUserXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;

import java.util.List;

public class UserGroupUserExtractor {

    private class UserGroupUserX {

        UserGroupUserXStruct struct;
        List<String> scope;
    }

    public static void extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {
        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractUserGroupUser(application, idGenerator, result);
        }
    }

    private static void extractUserGroupUser(Application application, IdGenerator idGenerator, DirectAccessControlSource result) {
        ApplicationIdType applicationId = IdUtils.createApplicationId(application.getId());
        //来源1

        for (Tenant tenant : application.getTenants()) {

            for (UserGroup userGroup : tenant.getUserGroups()) {
                userGroup.getUsers();
            }
        }

    }
}
