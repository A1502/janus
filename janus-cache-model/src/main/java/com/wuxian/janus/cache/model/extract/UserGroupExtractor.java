package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.core.critical.DimensionEnum;
import com.wuxian.janus.core.critical.NativeUserGroupEnum;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.UserGroupStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserGroupExtractor {
    public static TenantMap<IdType, UserGroup> extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {

        TenantMap<IdType, UserGroup> resultTenantMap = new TenantMap<>();

        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractUserGroup(application, idGenerator, resultTenantMap, result);
        }

        return resultTenantMap;
    }

    private static void extractUserGroup(Application application, IdGenerator idGenerator
            , TenantMap<IdType, UserGroup> resultTenantMap, DirectAccessControlSource result) {
        ApplicationIdType applicationId = IdUtils.createApplicationId(application.getId());
        //来源1
        List<UserGroup> from1Application = application.buildNativeApplicationUserGroup();

        for (Tenant tenant : application.getTenants()) {

            TenantIdType tenantId = IdUtils.createTenantId(tenant.getId());

            List<UserGroup> all = gather(from1Application, applicationId, tenantId, tenant);

            ExtractUtils.fixIdAndKeyFields(all, idGenerator);

            //这是返回值，后续流程需要
            Map<IdType, UserGroup> resultModelMap = ExtractUtils.groupByIdAndMerge(all);

            Map<IdType, UserGroupStruct> structMap = resultModelMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            (entry) -> {
                                UserGroup role = entry.getValue();
                                Function<BaseModel<UserGroupStruct>, UserGroupStruct> lambda =
                                        model -> convertToStruct((UserGroup) model, application, result);
                                return role.buildStruct(lambda);
                            }
                    ));

            result.getUserGroup().add(applicationId, tenantId, structMap);

            resultTenantMap.add(applicationId, tenantId, resultModelMap);
        }
    }

    private static List<UserGroup> gather(List<UserGroup> fromApplication
            , ApplicationIdType applicationId, TenantIdType tenantId, Tenant tenant) {
        //来源2
        List<UserGroup> from2Tenant = tenant.buildNativeTenantUserGroup(applicationId);

        //来源3
        List<UserGroup> from3Tenant = new ArrayList<>(tenant.getUserGroups());

        //tenant来源合并,并数据加工
        List<UserGroup> allTenant = new ArrayList<>(from2Tenant);
        allTenant.addAll(from3Tenant);
        appendStructTenantId(allTenant, tenantId);

        //来源合并
        List<UserGroup> result = new ArrayList<>(fromApplication);
        result.addAll(allTenant);

        return result;
    }

    private static UserGroupStruct convertToStruct(UserGroup userGroupModel, Application application
            , DirectAccessControlSource source) {
        UserGroupStruct struct = new UserGroupStruct();
        struct.setApplicationId(IdUtils.createApplicationId(application.getId()).getValue());

        //OuterObjectTypeCode + OuterObjectCode  --> OuterObjectTypeId
        //注意下面这个条件不可以用keyFieldsHasValue代替
        if (userGroupModel.getOuterObjectCode() != null) {
            OuterObjectStruct outerObjectStruct =
                    UserAndOuterObjectExtractor.findByOuterObjectTypeCodeAndOuterObjectCode(source,
                            userGroupModel.getOuterObjectTypeCode()
                            , userGroupModel.getOuterObjectCode()
                            , userGroupModel.toHashString()).outerObjectStruct;
            struct.setOuterObjectId(outerObjectStruct.getId());
        }
        return struct;
    }

    private static void appendStructTenantId(List<UserGroup> list, TenantIdType tenantId) {
        for (UserGroup userGroup : list) {
            if (userGroup.getStruct() == null) {
                userGroup.setStruct(new UserGroupStruct());
            }
            NativeUserGroupEnum nativeUserGroup = NativeUserGroupEnum.getByCode(userGroup.getCode());
            if (nativeUserGroup != null && nativeUserGroup.getDimensionTypeEnum().equals(DimensionEnum.APPLICATION)) {
                continue;
            }
            userGroup.getStruct().setTenantId(tenantId.getValue());
        }
    }
}
