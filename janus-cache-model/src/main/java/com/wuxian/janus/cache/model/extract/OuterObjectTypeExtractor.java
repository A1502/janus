package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.util.StrictUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class OuterObjectTypeExtractor {

    private OuterObjectTypeExtractor() {
    }

    /**
     * STEP1,排最前是为了给extractOuterObject提供outerObjectCode到Id的转换参照数据
     */
    static void extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {

        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        //来源1
        List<OuterObjectType> from1 = applicationGroup.getOuterObjectTypes();

        //来源2
        List<OuterObjectType> from2 = new ArrayList<>();
        for (OuterObject outerObject : applicationGroup.getOuterObjects()) {
            String typeCode = outerObject.getOuterObjectTypeCode();
            if (typeCode != null) {
                from2.add(new OuterObjectType(typeCode));
            }
        }

        List<OuterObjectType> from3 = new ArrayList<>();
        List<OuterObjectType> from4 = new ArrayList<>();
        List<OuterObjectType> from5 = new ArrayList<>();
        List<OuterObjectType> from6 = new ArrayList<>();
        List<OuterObjectType> from7 = new ArrayList<>();

        for (Application application : applicationGroup.getApplications()) {

            for (PermissionTemplate permissionTemplate : application.getPermissionTemplates()) {
                //来源3
                fill(from3, permissionTemplate.getOuterObjectTypeCode());
            }

            for (Tenant tenant : application.getTenants()) {
                //来源4
                for (Permission permission : tenant.getPermissions()) {
                    fill(from4, permission.getOuterObjectTypeCode());
                }

                //来源5
                for (UserGroup userGroup : tenant.getUserGroups()) {
                    fill(from5, userGroup.getOuterObjectTypeCode());
                }

                //来源6
                for (Role role : tenant.getRoles()) {
                    fill(from6, role.getOuterObjectTypeCode());
                    //来源7
                    for (Permission permission : role.getPermissions()) {
                        fill(from7, permission.getOuterObjectTypeCode());
                    }
                }
            }
        }

        //来源合并
        List<OuterObjectType> all = new ArrayList<>(from1);
        all.addAll(from2);
        all.addAll(from3);
        all.addAll(from4);
        all.addAll(from5);
        all.addAll(from6);
        all.addAll(from7);

        ExtractUtils.fixIdAndKeyFields(all, idGenerator);
        Map<IdType, OuterObjectTypeStruct> map = ExtractUtils.groupByIdAndMergeToStruct(all, null);
        result.getOuterObjectType().putAll(map);
    }

    private static void fill(List<OuterObjectType> list, String outerObjectTypeCode) {
        if (outerObjectTypeCode != null) {
            list.add(new OuterObjectType(outerObjectTypeCode));
        }
    }

    /**
     * @param source              查找的范围
     * @param outerObjectTypeCode 查找条件
     * @param context             若查找失败的相关数据
     * @return 查找的结果
     */
    static OuterObjectTypeStruct findByOuterObjectTypeCode(DirectAccessControlSource source
            , String outerObjectTypeCode, String context) {

        String targetDesc = "OuterObjectType";
        OuterObjectTypeStruct typeStruct = ExtractUtils.findFirst(source.getOuterObjectType().values(),
                o -> StrictUtils.equals(outerObjectTypeCode, o.getCode()));

        if (typeStruct != null) {
            return typeStruct;
        } else {
            throw ErrorFactory.createNothingFoundError(targetDesc
                    , "outerObjectTypeCode = " + outerObjectTypeCode
                    , context);
        }
    }

    /**
     * @param source            查找的范围
     * @param outerObjectTypeId 查找条件
     * @param context           查找上下文数据
     * @return 查找的结果
     */
    static OuterObjectTypeStruct findByOuterObjectTypeId(DirectAccessControlSource source
            , IdType outerObjectTypeId, String context) {

        String targetDesc = "OuterObjectType";

        OuterObjectTypeStruct typeStruct = StrictUtils.get(source.getOuterObjectType(), outerObjectTypeId);

        if (typeStruct != null) {
            return typeStruct;
        } else {
            throw ErrorFactory.createNothingFoundError(targetDesc
                    , "outerObjectTypeId = " + outerObjectTypeId
                    , context);
        }
    }
}
