package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.critical.CoverageTypeEnum;
import com.wuxian.janus.core.critical.NativeRoleEnum;
import com.wuxian.janus.entity.OuterObjectEntity;
import com.wuxian.janus.entity.PermissionTemplateEntity;
import com.wuxian.janus.entity.RoleEntity;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.TenantIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Map<IdType, RoleEntity> map = ExtractUtils.groupByIdAndMergeToEntity(all,
                    //在model上面有applicationId,所以在生成entity时补上这个属性通过merge进入到结果中
                    (model) -> {
                        Role roleModel = (Role) model;
                        RoleEntity entity = new RoleEntity();
                        entity.setApplicationId(applicationId.getValue());

                        //PermissionTemplateCode  -->  PermissionTemplateId
                        if (roleModel.getPermissionTemplateCode() != null) {
                            PermissionTemplateEntity permissionTemplateEntity
                                    = PermissionTemplateExtractor.findByPermissionTemplateCode(result
                                    , application, roleModel.getPermissionTemplateCode()
                                    , roleModel.getKeyFieldsHash());
                            entity.setPermissionTemplateId(permissionTemplateEntity.getId());
                        }

                        //OuterObjectTypeCode + OuterObjectCode  --> OuterObjectTypeId
                        //注意下面这个条件不可以用keyFieldsHasValue代替
                        if (roleModel.getOuterObjectCode() != null) {
                            OuterObjectEntity outerObjectEntity =
                                    UserAndOuterObjectExtractor.findByOuterObjectTypeCodeAndOuterObjectCode(result,
                                            roleModel.getOuterObjectTypeCode()
                                            , roleModel.getOuterObjectCode()
                                            , roleModel.toString());
                            entity.setOuterObjectId(outerObjectEntity.getId());
                        }
                        return entity;
                    });

            //按single,multiple收录到结果中
            Map<IdType, RoleEntity> singles = new HashMap<>();
            Map<IdType, RoleEntity> multiples = new HashMap<>();
            for (Map.Entry<IdType, RoleEntity> entry : map.entrySet()) {
                if (entry.getValue().getOuterObjectId() == null) {
                    singles.put(entry.getKey(), entry.getValue());
                } else {
                    multiples.put(entry.getKey(), entry.getValue());
                }
            }
            result.getSingleRole().add(applicationId, tenantId, singles);
            result.getMultipleRole().add(applicationId, tenantId, multiples);
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
