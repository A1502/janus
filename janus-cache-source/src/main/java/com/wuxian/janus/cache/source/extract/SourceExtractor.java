package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.model.ApplicationGroup;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;

public class SourceExtractor {

    private IdGeneratorFactory idGeneratorFactory;

    public SourceExtractor(IdGeneratorFactory idGeneratorFactory) {
        this.idGeneratorFactory = idGeneratorFactory;
    }

    public DirectAccessControlSource extract(ApplicationGroup applicationGroup) {
        DirectAccessControlSource result = new DirectAccessControlSource();

        //STEP1:提取outerObjectType DONE
        OuterObjectTypeExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP2-1:提取outerObject DONE
        //STEP2-2:提取userOuterObjectX DONE
        UserAndOuterObjectExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP3:提取permissionTemplate
        PermissionTemplateExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP4:提取permission
        PermissionExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP5:提取role
        RoleExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP6:提取userGroup
        UserGroupExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP7:提取roleRelation
        RoleRelationExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP8:提取userGroupUser
        UserGroupUserExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        return result;
    }
}
