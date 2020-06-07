package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.Application;
import com.wuxian.janus.cache.model.source.Permission;
import com.wuxian.janus.cache.model.source.Role;
import com.wuxian.janus.cache.model.source.Tenant;
import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.core.calculate.RolePermissionUtils;
import com.wuxian.janus.struct.layer2.RolePermissionXStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RolePermissionExtractor {

    public static void extract(TenantMap<IdType, Role> roleTenantMap, IdGeneratorFactory idGeneratorFactory
            , DirectAccessControlSource result) {

        IdGenerator rolePermissionXIdGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        Map<ApplicationIdType, Set<TenantIdType>> map = roleTenantMap.getIds();
        for (ApplicationIdType applicationId : map.keySet()) {

            Set<TenantIdType> tenantIds = StrictUtils.get(map, applicationId);
            for (TenantIdType tenantId : tenantIds) {
                Map<IdType, Role> roleMap = roleTenantMap.get(applicationId, tenantId);
                extractRolePermission(applicationId, tenantId, roleMap, rolePermissionXIdGenerator, result);
            }
        }
    }

    private static void extractRolePermission(ApplicationIdType applicationId
            , TenantIdType tenantId, Map<IdType, Role> roleMap, IdGenerator rolePermissionXIdGenerator, DirectAccessControlSource result) {

        Map<IdType, RolePermissionXStruct> singles = new HashMap<>();
        Map<IdType, RolePermissionXStruct> multiples = new HashMap<>();

        for (Role role : roleMap.values()) {
            boolean isMultiple = role.getMultiple();
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

                IdType xIdType = rolePermissionXIdGenerator.generate();

                RolePermissionXStruct xStruct = new RolePermissionXStruct();
                xStruct.setId(xIdType.getValue());
                xStruct.setRoleId(IdUtils.createId(role.getId()).getValue());
                xStruct.setPermissionId(IdUtils.createId(permission.getId()).getValue());

                if (isMultiple) {
                    multiples.put(xIdType, xStruct);
                } else {
                    singles.put(xIdType, xStruct);
                }
            }
        }

        result.getSingleRolePermissionX().add(applicationId, tenantId, singles);
        result.getMultipleRolePermissionX().add(applicationId, tenantId, multiples);
    }
}
