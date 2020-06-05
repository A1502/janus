package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;

import java.util.*;
import java.util.stream.Collectors;

public class PermissionTemplateExtractor {

    /**
     * STEP3:提取permissionTemplate
     * 为STEP4提供PermissionTemplateExtractor.findByPermissionTemplateCode
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
                    from2.add(createTempPermissionTemplate(permission.getPermissionTemplateCode()));
                }
            }

            for (Role role : tenant.getRoles()) {
                //来源3
                if (role.getPermissionTemplateCode() != null) {
                    from3.add(createTempPermissionTemplate(role.getPermissionTemplateCode()));
                }
                for (Permission permission : role.getPermissions()) {
                    //来源4
                    if (permission.getPermissionTemplateCode() != null) {
                        from4.add(createTempPermissionTemplate(permission.getPermissionTemplateCode()));
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
                IdUtils.createId(o.getId())).collect(Collectors.toSet());
        //修正id生成起点
        templateIdGenerator.addUsed(allIds);

        //来源5
        List<PermissionTemplate> from5 = application.buildNativePermissionTemplate(templateIdGenerator);

        //全部来源合并
        all.addAll(from5);
        ExtractUtils.fixIdAndKeyFields(all, templateIdGenerator);

        Map<IdType, PermissionTemplateStruct> map = ExtractUtils.groupByIdAndMergeToStruct(all,
                //在model上面有applicationId,所以在生成struct时补上这个属性通过merge进入到结果中
                (model) -> {
                    PermissionTemplate permissionTemplateModel = (PermissionTemplate) model;

                    PermissionTemplateStruct struct = new PermissionTemplateStruct();
                    struct.setApplicationId(applicationId.getValue());

                    String outerObjectTypeCode = permissionTemplateModel.getOuterObjectTypeCode();
                    if (outerObjectTypeCode != null) {
                        OuterObjectTypeStruct typeStruct = OuterObjectTypeExtractor.findByOuterObjectTypeCode(result,
                                outerObjectTypeCode, permissionTemplateModel.toHashString());
                        struct.setOuterObjectTypeId(typeStruct.getId());
                        struct.setMultiple(permissionTemplateModel.getMultiple());
                    }
                    return struct;
                });

        //还原temp逻辑对结果中multiple的影响
        processTempMultiple(map.values());

        //添加到结果集
        result.getPermissionTemplate().add(IdUtils.createApplicationId(application.getId()), map);
    }

    /**
     * multiple为null是为了在合并时降低自己优先级，以其他数据为准。若multiple为false则会视为确定值不可再被合并逻辑修改
     */
    private static PermissionTemplate createTempPermissionTemplate(String permissionTemplateCode) {
        PermissionTemplate result = new PermissionTemplate(permissionTemplateCode);
        result.setMultiple(null);
        return result;
    }

    /**
     * 将临时multiple按null处理的，全部转false作为最终值
     */
    private static void processTempMultiple(Collection<PermissionTemplateStruct> input) {
        input.stream().forEach(o -> {
            if (o.getMultiple() == null) {
                o.setMultiple(false);
            }
        });
    }

    static PermissionTemplateStruct findByPermissionTemplateCode(DirectAccessControlSource source
            , Application application, String permissionTemplateCode, String context) {

        Map<IdType, PermissionTemplateStruct> map =
                source.getPermissionTemplate().get(IdUtils.createApplicationId(application.getId()));

        String targetDesc = "Map<IdType, PermissionTemplateStruct>";
        if (map == null) {
            throw ErrorFactory.createNothingFoundError(targetDesc
                    , "applicationId = " + application.getId(),
                    context);
        }

        targetDesc = "PermissionTemplate";
        PermissionTemplateStruct struct = ExtractUtils.findFirst(map.values(),
                o -> StrictUtils.equals(permissionTemplateCode, o.getCode()));
        if (struct != null) {
            return struct;
        } else {
            throw ErrorFactory.createNothingFoundError(targetDesc
                    , "applicationId = " + application.getId() + ", permissionTemplateCode = " + permissionTemplateCode
                    , context);
        }
    }
}
