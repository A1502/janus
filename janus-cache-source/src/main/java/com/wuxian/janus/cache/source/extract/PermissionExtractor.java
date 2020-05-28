package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.entity.OuterObjectEntity;
import com.wuxian.janus.entity.OuterObjectTypeEntity;
import com.wuxian.janus.entity.PermissionEntity;
import com.wuxian.janus.entity.PermissionTemplateEntity;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PermissionExtractor {
    static void extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {

        //准备template的id生成
        IdGenerator templateIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);
        IdGenerator permissionIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractPermission(application, templateIdGenerator, permissionIdGenerator, result);
        }
    }

    private static void extractPermission(Application application, IdGenerator templateIdGenerator,
                                          IdGenerator permissionIdGenerator, DirectAccessControlSource result) {

        ApplicationIdType applicationId = IdUtils.createApplicationId(application.getId());
        //来源1
        List<Permission> from1Application = application.buildNativeApplicationPermission(templateIdGenerator);

        for (Tenant tenant : application.getTenants()) {

            TenantIdType tenantId = IdUtils.createTenantId(tenant.getId());

            //来源2:指定了permissionTemplateId的native permission
            //因为application级只能设置templateId
            List<Permission> from2Tenant = application.buildNativeTenantPermission(tenantId, templateIdGenerator);
            //来源3:指定了permissionId的native permission
            //因为tenant级只能设置permissionId
            List<Permission> from3Tenant = tenant.buildNativeTenantPermission();

            List<Permission> from4Tenant = new ArrayList<>();
            List<Permission> from5Tenant = new ArrayList<>();

            //来源4
            from4Tenant.addAll(tenant.getPermissions());
            for (Role role : tenant.getRoles()) {
                //来源5
                from5Tenant.addAll(role.getPermissions());
            }

            //tenant来源合并,并数据加工
            List<Permission> allTenant = new ArrayList<>(from2Tenant);
            allTenant.addAll(from3Tenant);
            allTenant.addAll(from4Tenant);
            allTenant.addAll(from5Tenant);
            //只需要补全tenantId
            appendEntityTenantId(allTenant, tenantId);

            //来源合并
            List<Permission> all = new ArrayList<>(from1Application);
            all.addAll(allTenant);

            ExtractUtils.fixIdAndKeyFields(all, permissionIdGenerator);
            Map<IdType, PermissionEntity> map = ExtractUtils.groupByIdAndMergeToEntity(all,
                    //在model上面有applicationId,所以在生成entity时补上这个属性通过merge进入到结果中
                    (model) -> {
                        Permission permissionModel = (Permission) model;
                        PermissionEntity entity = new PermissionEntity();

                        //PermissionTemplateCode  -->  PermissionTemplateId
                        PermissionTemplateEntity permissionTemplateEntity
                                = PermissionTemplateExtractor.findByPermissionTemplateCode(result
                                , application, permissionModel.getPermissionTemplateCode()
                                , permissionModel.toHashString());
                        entity.setPermissionTemplateId(permissionTemplateEntity.getId());

                        //验证权限模板和权限是否是同类的outerObjectType
                        checkOuterObjectTypeMatch(result, permissionTemplateEntity, permissionModel);

                        //OuterObjectTypeCode + OuterObjectCode  --> OuterObjectTypeId
                        //注意下面这个条件不可以用keyFieldsHasValue代替
                        if (permissionModel.getOuterObjectCode() != null) {
                            OuterObjectEntity outerObjectEntity =
                                    UserAndOuterObjectExtractor.findByOuterObjectTypeCodeAndOuterObjectCode(result,
                                            permissionModel.getOuterObjectTypeCode()
                                            , permissionModel.getOuterObjectCode()
                                            , permissionModel.toHashString());
                            entity.setOuterObjectId(outerObjectEntity.getId());
                        }
                        return entity;
                    });

            //按single,multiple收录到结果中
            Map<IdType, PermissionEntity> singles = new HashMap<>();
            Map<IdType, PermissionEntity> multiples = new HashMap<>();
            for (Map.Entry<IdType, PermissionEntity> entry : map.entrySet()) {
                if (entry.getValue().getOuterObjectId() == null) {
                    singles.put(entry.getKey(), entry.getValue());
                } else {
                    multiples.put(entry.getKey(), entry.getValue());
                }
            }
            result.getSinglePermission().add(applicationId, tenantId, singles);
            result.getMultiplePermission().add(applicationId, tenantId, multiples);
        }
    }

    //验证permissionTemplate和permission的outerObjectType要一致
    private static void checkOuterObjectTypeMatch(DirectAccessControlSource source
            , PermissionTemplateEntity permissionTemplateEntity
            , Permission permissionModel) {

        boolean notMatch = false;
        if (permissionTemplateEntity.getOuterObjectTypeId() != null) {
            OuterObjectTypeEntity outerObjectTypeEntity = OuterObjectTypeExtractor.findByOuterObjectTypeId(source
                    , IdUtils.createId(permissionTemplateEntity.getOuterObjectTypeId().toString())
                    , permissionModel.toHashString());
            //二者outerObjectTypeCode不一致
            if (permissionModel.getOuterObjectTypeCode() != outerObjectTypeEntity.getCode()) {
                notMatch = true;
            }
        } else {
            //二者outerObjectType一个为null，一个不为null
            if (permissionModel.getOuterObjectTypeCode() != null) {
                notMatch = true;
            }
        }
        if (notMatch) {
            throw ErrorFactory.createOuterObjectTypeNotMatchError(
                    PermissionTemplate.byId(permissionTemplateEntity.getId().toString()
                            , permissionTemplateEntity.getCode()).toHashString()
                    , permissionModel.toHashString()
            );
        }
    }

    private static void appendEntityTenantId(List<Permission> list, TenantIdType tenantId) {
        for (Permission permission : list) {
            if (permission.getEntity() == null) {
                permission.setEntity(new PermissionEntity());
            }
            permission.getEntity().setTenantId(tenantId.getValue());
        }
    }
}
