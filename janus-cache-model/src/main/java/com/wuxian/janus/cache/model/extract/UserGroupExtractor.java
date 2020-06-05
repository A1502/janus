package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.cache.model.source.Tenant;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Application;
import com.wuxian.janus.cache.model.source.UserGroup;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.critical.CoverageTypeEnum;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.UserGroupStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserGroupExtractor {
    public static void extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {
        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractUserGroup(application, idGenerator, result);
        }
    }

    private static void extractUserGroup(Application application, IdGenerator idGenerator, DirectAccessControlSource result) {
        ApplicationIdType applicationId = IdUtils.createApplicationId(application.getId());
        //来源1
        List<UserGroup> from1Application = application.buildNativeApplicationUserGroup();

        for (Tenant tenant : application.getTenants()) {

            TenantIdType tenantId = IdUtils.createTenantId(tenant.getId());

            //来源2
            List<UserGroup> from2Tenant = tenant.buildNativeTenantUserGroup(applicationId);

            //来源3
            List<UserGroup> from3Tenant = new ArrayList<>(tenant.getUserGroups());

            //tenant来源合并,并数据加工
            List<UserGroup> allTenant = new ArrayList<>(from2Tenant);
            allTenant.addAll(from3Tenant);
            appendStructTenantId(allTenant, tenantId);

            //来源合并
            List<UserGroup> all = new ArrayList<>(from1Application);
            all.addAll(allTenant);

            ExtractUtils.fixIdAndKeyFields(all, idGenerator);
            Map<IdType, UserGroupStruct> map = ExtractUtils.groupByIdAndMergeToStruct(all,
                    //在model上面有applicationId,所以在生成struct时补上这个属性通过merge进入到结果中
                    (model) -> {
                        UserGroup userGroupModel = (UserGroup) model;
                        UserGroupStruct struct = new UserGroupStruct();
                        struct.setApplicationId(applicationId.getValue());

                        //OuterObjectTypeCode + OuterObjectCode  --> OuterObjectTypeId
                        //注意下面这个条件不可以用keyFieldsHasValue代替
                        if (userGroupModel.getOuterObjectCode() != null) {
                            OuterObjectStruct outerObjectStruct =
                                    UserAndOuterObjectExtractor.findByOuterObjectTypeCodeAndOuterObjectCode(result,
                                            userGroupModel.getOuterObjectTypeCode()
                                            , userGroupModel.getOuterObjectCode()
                                            , userGroupModel.toHashString()).outerObjectStruct;
                            struct.setOuterObjectId(outerObjectStruct.getId());
                        }
                        return struct;
                    });

            result.getUserGroup().add(applicationId, tenantId, map);
        }
    }

    private static void appendStructTenantId(List<UserGroup> list, TenantIdType tenantId) {
        for (UserGroup userGroup : list) {
            if (userGroup.getStruct() == null) {
                userGroup.setStruct(new UserGroupStruct());
            }
            NativeUserGroupEnum nativeUserGroup = NativeUserGroupEnum.getByCode(userGroup.getCode());
            if (nativeUserGroup != null && nativeUserGroup.getCoverageType().equals(CoverageTypeEnum.APPLICATION)) {
                continue;
            }
            userGroup.getStruct().setTenantId(tenantId.getValue());
        }
    }
}
