package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.entity.PermissionTemplateEntity;
import com.wuxian.janus.entity.primary.ApplicationIdType;
import com.wuxian.janus.entity.primary.IdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionTemplateExtractor {

    /**
     * STEP3:提取permissionTemplate
     */
    static void extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {

        //准备template的id生成
        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        for (Application application : applicationGroup.getApplications()) {
            extractPermissionTemplate(application, idGenerator, result);
        }
    }

    private static void extractPermissionTemplate(Application application, IdGenerator templateIdGenerator, DirectAccessControlSource result) {

        ApplicationIdType applicationId = IdUtils.createApplicationId(application.getId());

        //来源1
        List<PermissionTemplate> from1 = application.getPermissionTemplates();

        List<PermissionTemplate> from2 = new ArrayList<>();
        List<PermissionTemplate> from3 = new ArrayList<>();
        List<PermissionTemplate> from4 = new ArrayList<>();
        for (Tenant tenant : application.getTenants()) {
            //来源2
            for (Permission permission : tenant.getPermissions()) {
                if (permission.getPermissionTemplateCode() != null) {
                    from2.add(new PermissionTemplate(permission.getPermissionTemplateCode()));
                }
            }

            for (Role role : tenant.getRoles()) {
                //来源3
                if (role.getPermissionTemplateCode() != null) {
                    from3.add(new PermissionTemplate(role.getPermissionTemplateCode()));
                }
                for (Permission permission : role.getPermissions()) {
                    //来源4
                    if (permission.getPermissionTemplateCode() != null) {
                        from4.add(new PermissionTemplate(permission.getPermissionTemplateCode()));
                    }
                }
            }
        }

        //提取来源4之前，来源1234合并获取allIds
        List<PermissionTemplate> all = new ArrayList<>(from1);
        all.addAll(from2);
        all.addAll(from3);
        all.addAll(from4);

        Set<IdType> allIds = all.stream().filter(o -> o.getId() != null).map(o ->
                IdUtils.createId(o.getId().toString())).collect(Collectors.toSet());
        //修正id生成起点
        templateIdGenerator.addUsed(allIds);

        //来源5
        List<PermissionTemplate> from5 = application.buildNativePermissionTemplate(templateIdGenerator);

        //全部来源合并
        all.addAll(from5);
        ExtractUtils.fixIdAndKeyFields(all, templateIdGenerator);
        Map<IdType, PermissionTemplateEntity> map = ExtractUtils.groupByIdAndMergeToEntity(all,
                //在model上面有applicationId,所以在生成entity时补上这个属性通过merge进入到结果中
                (model) -> {
                    PermissionTemplateEntity entity = new PermissionTemplateEntity();
                    entity.setApplicationId(applicationId.getValue());
                    return entity;
                });

        //添加到结果集
        result.getPermissionTemplate().add(IdUtils.createApplicationId(application.getId()), map);
    }

    static PermissionTemplateEntity findByPermissionTemplateCode(DirectAccessControlSource source
            , Application application, String permissionTemplateCode, String findByDesc) {

        Map<IdType, PermissionTemplateEntity> map =
                source.getPermissionTemplate().get(IdUtils.createApplicationId(application.getId()));

        String findDesc = "PermissionTemplate";
        if (map == null) {
            throw ErrorFactory.createNothingFoundError(findByDesc, findDesc);
        }

        PermissionTemplateEntity entity = ExtractUtils.findFirst(map.values(),
                o -> StrictUtils.equals(permissionTemplateCode, o.getCode()));
        if (entity != null) {
            return entity;
        } else {
            throw ErrorFactory.createNothingFoundError(findByDesc, findDesc);
        }
    }
}
