package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.calculate.PermissionTemplateUtils;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.layer2.PermissionStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PermissionExtractor {
    private PermissionExtractor() {
    }

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
            appendStructTenantId(allTenant, tenantId);

            //来源合并
            List<Permission> all = new ArrayList<>(from1Application);
            all.addAll(allTenant);

            ExtractUtils.fixIdAndKeyFields(all, permissionIdGenerator);
            Map<IdType, PermissionStruct> map = ExtractUtils.groupByIdAndMergeToStruct(all,
                    //在model上面有applicationId,所以在生成struct时补上这个属性通过merge进入到结果中
                    (model) -> {
                        Permission permissionModel = (Permission) model;
                        PermissionStruct struct = new PermissionStruct();

                        //PermissionTemplateCode  -->  PermissionTemplateId
                        PermissionTemplateStruct permissionTemplateStruct
                                = PermissionTemplateExtractor.findByPermissionTemplateCode(result
                                , application, permissionModel.getPermissionTemplateCode()
                                , permissionModel.toHashString());
                        struct.setPermissionTemplateId(permissionTemplateStruct.getId());

                        //验证权限模板和权限是否是同类的outerObjectType
                        checkOuterObjectTypeMatch(result, permissionTemplateStruct, permissionModel);

                        //OuterObjectTypeCode + OuterObjectCode  --> OuterObjectTypeId
                        //注意下面这个条件不可以用keyFieldsHasValue代替
                        if (permissionModel.getOuterObjectCode() != null) {
                            OuterObjectStruct outerObjectStruct =
                                    UserAndOuterObjectExtractor.findByOuterObjectTypeCodeAndOuterObjectCode(result,
                                            permissionModel.getOuterObjectTypeCode()
                                            , permissionModel.getOuterObjectCode()
                                            , permissionModel.toHashString()).outerObjectStruct;
                            struct.setOuterObjectId(outerObjectStruct.getId());
                        }
                        return struct;
                    });

            //按single,multiple收录到结果中
            Map<IdType, PermissionStruct> singles = new HashMap<>();
            Map<IdType, PermissionStruct> multiples = new HashMap<>();
            for (Map.Entry<IdType, PermissionStruct> entry : map.entrySet()) {
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

    /**
     * 验证permissionTemplate和permission的outerObjectType要一致
     */
    private static void checkOuterObjectTypeMatch(DirectAccessControlSource source
            , PermissionTemplateStruct permissionTemplateStruct
            , Permission permissionModel) {

        String outerObjectTypeCodeOfPermissionTemplate = null;
        if (permissionTemplateStruct.getOuterObjectTypeId() != null) {
            OuterObjectTypeStruct outerObjectTypeStruct = OuterObjectTypeExtractor.findByOuterObjectTypeId(source
                    , IdUtils.createId(permissionTemplateStruct.getOuterObjectTypeId().toString())
                    , permissionModel.toHashString());
            outerObjectTypeCodeOfPermissionTemplate = outerObjectTypeStruct.getCode();
        }

        boolean allowed = PermissionTemplateUtils.relationAllowed(outerObjectTypeCodeOfPermissionTemplate
                , permissionModel.getOuterObjectTypeCode());

        if (!allowed) {
            throw ErrorFactory.createOuterObjectTypeNotMatchError(
                    PermissionTemplate.byId(permissionTemplateStruct.getId().toString()
                            , permissionTemplateStruct.getCode()).toHashString()
                    , permissionModel.toHashString()
            );
        }
    }

    private static void appendStructTenantId(List<Permission> list, TenantIdType tenantId) {
        for (Permission permission : list) {
            if (permission.getStruct() == null) {
                permission.setStruct(new PermissionStruct());
            }
            permission.getStruct().setTenantId(tenantId.getValue());
        }
    }
}
