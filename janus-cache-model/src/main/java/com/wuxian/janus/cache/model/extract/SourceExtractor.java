package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.source.Role;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.struct.primary.IdType;

public class SourceExtractor {

    private IdGeneratorFactory idGeneratorFactory;

    public SourceExtractor(IdGeneratorFactory idGeneratorFactory) {
        this.idGeneratorFactory = idGeneratorFactory;
    }

    public DirectAccessControlSource extract(ApplicationGroup applicationGroup) {
        DirectAccessControlSource result = new DirectAccessControlSource();

        //STEP1:提取(20)outerObjectType DONE
        OuterObjectTypeExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP2-1:提取(21)outerObject DONE
        //STEP2-2:提取(22)userOuterObjectX DONE
        UserAndOuterObjectExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP3:提取(19)permissionTemplate DONE
        PermissionTemplateExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP4:提取(7)singlePermission,(13)multiplePermission DONE
        PermissionExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP5:提取(9)singleRole,(15)multipleRole DONE
        TenantMap<IdType, Role> roleTenantMap
                = RoleExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //(8)singleRolePermissionX,(14)multipleRolePermissionX DONE
        RolePermissionExtractor.extract(roleTenantMap, idGeneratorFactory, result);

        //TODO
        //STEP6:提取(1)ScopeSingleRoleUserX,(2)ScopeMultipleRoleUserX
        //(10)SingleRoleOtherX,(12)SingleRoleUserX
        //(16)MultipleRoleOtherX,(18)MultipleRoleUserX
        RoleRelationAndScopeExtractor.extract(roleTenantMap, this.idGeneratorFactory, result);

        //STEP7:提取(4)userGroup DONE TODO 改为返回值模式
        UserGroupExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        //STEP8:提取(3)ScopeUserGroupUserX
        //(5)UserGroupUserX,(6)UserGroupOtherX
        //(11)SingleRoleUserGroupX,(17)MultipleRoleUserGroupX
        UserGroupUserExtractor.extract(applicationGroup, this.idGeneratorFactory, result);

        return result;
    }
}
