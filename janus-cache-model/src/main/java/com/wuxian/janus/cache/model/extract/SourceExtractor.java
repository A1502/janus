package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;

public class SourceExtractor {

    private IdGeneratorFactory idGeneratorFactory;

    public SourceExtractor(IdGeneratorFactory idGeneratorFactory) {
        this.idGeneratorFactory = idGeneratorFactory;
    }

    public DirectAccessControlSource extract(ApplicationGroup applicationGroup) {
        DirectAccessControlSource result = new DirectAccessControlSource();

        //STEP1:提取(19)outerObjectType DONE
        OuterObjectTypeExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP2-1:提取(20)outerObject DONE
        //STEP2-2:提取(21)userOuterObjectX DONE
        UserAndOuterObjectExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP3:提取(18)permissionTemplate DONE
        PermissionTemplateExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP4:提取(6)singlePermission,(12)multiplePermission DONE
        PermissionExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP5:提取(8)singleRole,(14)multipleRole DONE
        //(7)singleRolePermission,(13)multipleRolePermission DONE
        RoleExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP6:提取(4)userGroup DONE
        UserGroupExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP7:提取roleRelation TODO
        RoleRelationExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP8:提取userGroupUser TODO
        UserGroupUserExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        return result;
    }
}
