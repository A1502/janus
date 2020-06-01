package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.RolePermissionUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.critical.CoverageTypeEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.entity.OuterObjectEntity;
import com.wuxian.janus.entity.OuterObjectTypeEntity;
import com.wuxian.janus.entity.PermissionTemplateEntity;
import com.wuxian.janus.entity.RoleEntity;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoleExtractor {

    static void extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {
        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractRole(application, idGenerator, result);
        }
    }

    private static void extractRole(Application application, IdGenerator roleIdGenerator, DirectAccessControlSource result) {

        ApplicationIdType applicationId = IdUtils.createApplicationId(application.getId());
        //来源1
        List<Role> from1Application = application.buildNativeApplicationRole();

        for (Tenant tenant : application.getTenants()) {

            TenantIdType tenantId = IdUtils.createTenantId(tenant.getId());

            //来源2
            List<Role> from2Tenant = tenant.buildNativeTenantRole(applicationId);

            //来源3
            List<Role> from3Tenant = new ArrayList<>(tenant.getRoles());

            //来源4
            List<Role> from4Tenant = new ArrayList<>();
            for (UserGroup userGroup : tenant.getUserGroups()) {
                from4Tenant.addAll(userGroup.getRoles());
            }

            //tenant来源合并,并数据加工
            List<Role> allTenant = new ArrayList<>(from2Tenant);
            allTenant.addAll(from3Tenant);
            allTenant.addAll(from4Tenant);
            appendEntityTenantId(allTenant, tenantId);

            //来源合并
            List<Role> all = new ArrayList<>(from1Application);
            all.addAll(allTenant);

            ExtractUtils.fixIdAndKeyFields(all, roleIdGenerator);

            //后续计算SinglePermissionX和MultiplePermissionX需要
            Map<IdType, Role> modelMap = ExtractUtils.groupByIdAndMerge(all);

            //SingleRole,MultipleRole需要
            Map<IdType, RoleEntity> entityMap = modelMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            (entry) -> {
                                Role role = entry.getValue();
                                Function<BaseModel<RoleEntity>, RoleEntity> lambda =
                                        model -> convertToEntity((Role) model, application, result);
                                return role.buildEntity(lambda);
                            }
                    ));

            //按single,multiple收录到结果中
            Map<IdType, RoleEntity> singles = new HashMap<>();
            Map<IdType, RoleEntity> multiples = new HashMap<>();
            for (Map.Entry<IdType, RoleEntity> entry : entityMap.entrySet()) {
                if (!entry.getValue().getMultiple()) {
                    singles.put(entry.getKey(), entry.getValue());
                } else {
                    multiples.put(entry.getKey(), entry.getValue());
                }
            }

            result.getSingleRole().add(applicationId, tenantId, singles);
            result.getMultipleRole().add(applicationId, tenantId, multiples);

            //提取(7)singleRolePermission,(13)multipleRolePermission
            extractRolePermission(application, tenant, modelMap, result);
        }
    }

    private static RoleEntity convertToEntity(Role roleModel, Application application, DirectAccessControlSource source) {
        RoleEntity entity = new RoleEntity();
        //在roleModel上面有applicationId,所以在生成entity时补上这个属性通过merge进入到结果中
        entity.setApplicationId(application.getId());

        //PermissionTemplateCode  -->  PermissionTemplateId
        if (roleModel.getPermissionTemplateCode() != null) {
            PermissionTemplateEntity permissionTemplateEntity
                    = PermissionTemplateExtractor.findByPermissionTemplateCode(source
                    , application, roleModel.getPermissionTemplateCode()
                    , roleModel.toHashString());
            entity.setPermissionTemplateId(permissionTemplateEntity.getId());
        }

        //OuterObjectTypeCode  --> OuterObjectTypeId
        if (roleModel.getOuterObjectTypeCode() != null) {
            OuterObjectTypeEntity typeEntity = OuterObjectTypeExtractor.findByOuterObjectTypeCode(source
                    , roleModel.getOuterObjectTypeCode(), roleModel.toHashString());
            entity.setOuterObjectTypeId(typeEntity.getId());
        }

        //OuterObjectTypeCode + OuterObjectCode  --> OuterObjectId
        if (roleModel.getOuterObjectCode() != null) {
            OuterObjectEntity outerObjectEntity =
                    UserAndOuterObjectExtractor.findByOuterObjectTypeCodeAndOuterObjectCode(source
                            , roleModel.getOuterObjectTypeCode()
                            , roleModel.getOuterObjectCode()
                            , roleModel.toHashString()).outerObjectEntity;
            entity.setOuterObjectId(outerObjectEntity.getId());
        }
        return entity;
    }

    private static void extractRolePermission(Application application
            , Tenant tenant, Map<IdType, Role> roleMap, DirectAccessControlSource result) {
        if (true) {
            return;
        }
        for (Role role : roleMap.values()) {

            for (Permission permission : role.getPermissions()) {

                PermissionTemplateEntity templateEntity
                        = PermissionTemplateExtractor.findByPermissionTemplateCode(result
                        , application
                        , permission.getPermissionTemplateCode()
                        , permission.toHashString());

                permission的信息量不一定全，要转permissionEntity才全
                String outerObjectCode

                UserAndOuterObjectExtractor.OuterObjectPair outerObjectPair =
                        UserAndOuterObjectExtractor.findByOuterObjectTypeCodeAndOuterObjectCode(result
                                , permission.getOuterObjectTypeCode()
                                , permission.getOuterObjectCode()
                                , permission.toHashString());

                boolean allowed = RolePermissionUtils.relationAllowed(role.getMultiple()
                        , role.getOuterObjectTypeCode()
                        , role.getOuterObjectCode()
                        , templateEntity.getCode()
                        , outerObjectPair.outerObjectEntity.getReferenceCode());
                 if not allowed TODO

            }
        }
    }

    private static void appendEntityTenantId(List<Role> list, TenantIdType tenantId) {
        for (Role role : list) {
            if (role.getEntity() == null) {
                role.setEntity(new RoleEntity());
            }
            NativeRoleEnum nativeRole = NativeRoleEnum.getByCode(role.getCode());
            if (nativeRole != null && nativeRole.getCoverageType().equals(CoverageTypeEnum.APPLICATION)) {
                continue;
            }
            role.getEntity().setTenantId(tenantId.getValue());
        }
    }

}


//role.PermissionTemplateId = role.permissions.any().permissionTemplateId
//role.Permission

