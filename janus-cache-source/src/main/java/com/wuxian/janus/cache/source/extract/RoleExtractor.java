package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.RolePermissionUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.critical.CoverageTypeEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.entity.*;
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
        IdGenerator roleIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);
        IdGenerator rolePermissionXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractRole(application, roleIdGenerator, rolePermissionXIdGenerator, result);
        }
    }

    private static void extractRole(Application application, IdGenerator roleIdGenerator
            , IdGenerator rolePermissionXIdGenerator, DirectAccessControlSource result) {

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
                if (!isMultiple(entry.getValue())) {
                    singles.put(entry.getKey(), entry.getValue());
                } else {
                    multiples.put(entry.getKey(), entry.getValue());
                }
            }

            result.getSingleRole().add(applicationId, tenantId, singles);
            result.getMultipleRole().add(applicationId, tenantId, multiples);

            //提取(7)singleRolePermission,(13)multipleRolePermission
            extractRolePermission(application, tenant, modelMap, rolePermissionXIdGenerator, result);
        }
    }

    private static boolean isMultiple(Role role) {
        //这里必须要booleanValue(),让getMultiple == null 变成异常，而不是隐瞒问题
        return role.getMultiple().booleanValue();
    }

    private static boolean isMultiple(RoleEntity role) {
        //这里必须要booleanValue(),让getMultiple == null 变成异常，而不是隐瞒问题
        return role.getMultiple().booleanValue();
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
            , Tenant tenant, Map<IdType, Role> roleMap, IdGenerator idGenerator, DirectAccessControlSource result) {

        Map<IdType, RolePermissionXEntity> singles = new HashMap<>();
        Map<IdType, RolePermissionXEntity> multiples = new HashMap<>();

        for (Role role : roleMap.values()) {
            boolean isMultiple = isMultiple(role);
            for (Permission permission : role.getPermissions()) {

                //permission是经过了PermissionExtractor的fillKeyFields处理的，所以能保证
                //permission.getOuterObjectTypeCode(),getOuterObjectCode()值有效（哪怕是null)

                boolean allowed = RolePermissionUtils.relationAllowed(role.getMultiple()
                        , role.getOuterObjectTypeCode()
                        , role.getOuterObjectCode()
                        //这里本来应取permissionTemplate的outerObjectCode
                        //但可直接用permission.getOuterObjectTypeCode()简化
                        //因为PermissionExtractor的checkOuterObjectTypeMatch方法保证了
                        //permission和permissionTemplate的outerObjectCode一致
                        , permission.getOuterObjectTypeCode()
                        , permission.getOuterObjectCode());

                if (!allowed) {
                    throw ErrorFactory.createRoleAndPermissionRelationNotAllowedError(
                            role.toHashString(), permission.toHashString());
                }

                IdType xIdType = idGenerator.generate();

                RolePermissionXEntity xEntity = new RolePermissionXEntity();
                xEntity.setId(xIdType.getValue());
                xEntity.setRoleId(IdUtils.createId(role.getId()).getValue());
                xEntity.setPermissionId(IdUtils.createId(permission.getId()).getValue());

                if (isMultiple) {
                    multiples.put(xIdType, xEntity);
                } else {
                    singles.put(xIdType, xEntity);
                }
            }
        }

        ApplicationIdType appId = IdUtils.createApplicationId(application.getId());
        TenantIdType tId = IdUtils.createTenantId(tenant.getId());

        result.getSingleRolePermissionX().add(appId, tId, singles);
        result.getMultipleRolePermissionX().add(appId, tId, multiples);
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

