package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.entity.OuterObjectTypeEntity;
import com.wuxian.janus.entity.primary.IdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class OuterObjectTypeExtractor {

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

        for (Application application : applicationGroup.getApplications()) {
            for (Tenant tenant : application.getTenants()) {
                //来源3
                for (Permission permission : tenant.getPermissions()) {
                    String typeCode = permission.getOuterObjectTypeCode();
                    if (typeCode != null) {
                        from3.add(new OuterObjectType(typeCode));
                    }
                }

                //来源4
                for (UserGroup userGroup : tenant.getUserGroups()) {
                    String typeCode = userGroup.getOuterObjectTypeCode();
                    if (typeCode != null) {
                        from4.add(new OuterObjectType(typeCode));
                    }
                }

                //来源5
                for (Role role : tenant.getRoles()) {
                    String typeCode = role.getOuterObjectTypeCode();
                    if (typeCode != null) {
                        from5.add(new OuterObjectType(typeCode));
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

        ExtractUtils.fixIdAndKeyFields(all, idGenerator);
        Map<IdType, OuterObjectTypeEntity> map = ExtractUtils.groupByIdAndMergeToEntity(all, null);
        result.getOuterObjectType().putAll(map);
    }

    /**
     * @param source              查找的范围
     * @param outerObjectTypeCode 查找条件
     * @param findByDesc          查找条件的说明,用于拼装报错提示
     * @return 查找的结果
     */
    static OuterObjectTypeEntity findByOuterObjectTypeCode(DirectAccessControlSource source
            , String outerObjectTypeCode, String findByDesc) {

        String findDesc = "OuterObjectType";
        OuterObjectTypeEntity typeEntity = ExtractUtils.findFirst(source.getOuterObjectType().values(),
                o -> StrictUtils.equals(outerObjectTypeCode, o.getCode()));

        if (typeEntity != null) {
            return typeEntity;
        } else {
            throw ErrorFactory.createNothingFoundError(findByDesc, findDesc);
        }
    }
}
