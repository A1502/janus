package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.core.critical.DimensionEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.layer1.RoleStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoleExtractor {

    static TenantMap<IdType, Role> extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory
            , DirectAccessControlSource result) {

        TenantMap<IdType, Role> resultTenantMap = new TenantMap<>();

        IdGenerator roleIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractRole(application, roleIdGenerator, resultTenantMap, result);
        }

        return resultTenantMap;
    }

    private static void extractRole(Application application, IdGenerator roleIdGenerator
            , TenantMap<IdType, Role> resultTenantMap, DirectAccessControlSource result) {

        ApplicationIdType applicationId = IdUtils.createApplicationId(application.getId());
        //来源1
        List<Role> from1Application = application.buildNativeApplicationRole();

        for (Tenant tenant : application.getTenants()) {

            TenantIdType tenantId = IdUtils.createTenantId(tenant.getId());

            List<Role> all = gather(from1Application, applicationId, tenantId, tenant);

            ExtractUtils.fixIdAndKeyFields(all, roleIdGenerator);

            //这是返回值，后续流程需要
            Map<IdType, Role> resultModelMap = ExtractUtils.groupByIdAndMerge(all);

            //SingleRole,MultipleRole需要
            Map<IdType, RoleStruct> structMap = resultModelMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            (entry) -> {
                                Role role = entry.getValue();
                                Function<BaseModel<RoleStruct>, RoleStruct> lambda =
                                        model -> convertToStruct((Role) model, application, result);
                                return role.buildStruct(lambda);
                            }
                    ));

            //按single,multiple收录到结果中
            Map<IdType, RoleStruct> singles = new HashMap<>();
            Map<IdType, RoleStruct> multiples = new HashMap<>();
            for (Map.Entry<IdType, RoleStruct> entry : structMap.entrySet()) {
                if (!entry.getValue().getMultiple()) {
                    singles.put(entry.getKey(), entry.getValue());
                } else {
                    multiples.put(entry.getKey(), entry.getValue());
                }
            }

            result.getSingleRole().add(applicationId, tenantId, singles);
            result.getMultipleRole().add(applicationId, tenantId, multiples);

            resultTenantMap.add(applicationId, tenantId, resultModelMap);
        }
    }

    private static List<Role> gather(List<Role> fromApplication
            , ApplicationIdType applicationId, TenantIdType tenantId, Tenant tenant) {

        //来源2
        List<Role> from2Tenant = tenant.buildNativeTenantRole(applicationId);

        //来源3
        List<Role> from3Tenant = new ArrayList<>(tenant.getRoles());

        //来源4
        List<Role> from4Tenant = new ArrayList<>();
        for (UserGroup userGroup : tenant.getUserGroups()) {
            from4Tenant.addAll(userGroup.getRoles().keySet());
        }

        //tenant来源合并,并数据加工
        List<Role> allTenant = new ArrayList<>(from2Tenant);
        allTenant.addAll(from3Tenant);
        allTenant.addAll(from4Tenant);
        appendStructTenantId(allTenant, tenantId);

        //来源合并
        List<Role> result = new ArrayList<>(fromApplication);
        result.addAll(allTenant);

        return result;
    }

    private static RoleStruct convertToStruct(Role roleModel, Application application
            , DirectAccessControlSource source) {
        RoleStruct struct = new RoleStruct();
        //在roleModel上面有applicationId,所以在生成struct时补上这个属性通过merge进入到结果中
        struct.setApplicationId(IdUtils.createApplicationId(application.getId()).getValue());

        //PermissionTemplateCode  -->  PermissionTemplateId
        if (roleModel.getPermissionTemplateCode() != null) {
            PermissionTemplateStruct permissionTemplateStruct
                    = PermissionTemplateExtractor.findByPermissionTemplateCode(source
                    , application, roleModel.getPermissionTemplateCode()
                    , roleModel.toHashString());
            struct.setPermissionTemplateId(permissionTemplateStruct.getId());
        }

        //OuterObjectTypeCode  --> OuterObjectTypeId
        if (roleModel.getOuterObjectTypeCode() != null) {
            OuterObjectTypeStruct typeStruct = OuterObjectTypeExtractor.findByOuterObjectTypeCode(source
                    , roleModel.getOuterObjectTypeCode(), roleModel.toHashString());
            struct.setOuterObjectTypeId(typeStruct.getId());
        }

        //OuterObjectTypeCode + OuterObjectCode  --> OuterObjectId
        if (roleModel.getOuterObjectCode() != null) {
            OuterObjectStruct outerObjectStruct =
                    UserAndOuterObjectExtractor.findByOuterObjectTypeCodeAndOuterObjectCode(source
                            , roleModel.getOuterObjectTypeCode()
                            , roleModel.getOuterObjectCode()
                            , roleModel.toHashString()).outerObjectStruct;
            struct.setOuterObjectId(outerObjectStruct.getId());
        }
        return struct;
    }

    private static void appendStructTenantId(List<Role> list, TenantIdType tenantId) {
        for (Role role : list) {
            if (role.getStruct() == null) {
                role.setStruct(new RoleStruct());
            }
            NativeRoleEnum nativeRole = NativeRoleEnum.getByCode(role.getCode());
            if (nativeRole != null && DimensionEnum.APPLICATION.equals(nativeRole.getDimension())) {
                continue;
            }
            role.getStruct().setTenantId(tenantId.getValue());
        }
    }

}
