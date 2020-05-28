package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdGeneratorFactory;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.*;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.entity.OuterObjectEntity;
import com.wuxian.janus.entity.OuterObjectTypeEntity;
import com.wuxian.janus.entity.UserOuterObjectXEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.entity.primary.UserIdType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class UserAndOuterObjectExtractor {

    static final String DELIMITER = ",";

    static void extract(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {

        Map<IdType, OuterObject> modelMap = extractOuterObject(applicationGroup, idGeneratorFactory, result);

        extractUserOuterObjectX(modelMap.values(), idGeneratorFactory, result);
    }

    /**
     * STEP2-1 需要STEP1提供outerObjectCode到Id转换的支持
     */
    private static Map<IdType, OuterObject> extractOuterObject(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {

        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        //来源1
        List<OuterObject> from1 = applicationGroup.getOuterObjects();

        List<OuterObject> from2 = new ArrayList<>();
        List<OuterObject> from3 = new ArrayList<>();
        List<OuterObject> from4 = new ArrayList<>();

        for (Application application : applicationGroup.getApplications()) {
            for (Tenant tenant : application.getTenants()) {
                //来源2
                for (Permission permission : tenant.getPermissions()) {
                    String objectCode = permission.getOuterObjectCode();
                    if (objectCode != null) {
                        from2.add(new OuterObject(objectCode, permission.getOuterObjectTypeCode()));
                    }
                }

                //来源3
                for (UserGroup userGroup : tenant.getUserGroups()) {
                    String objectCode = userGroup.getOuterObjectCode();
                    if (objectCode != null) {
                        from3.add(new OuterObject(objectCode, userGroup.getOuterObjectTypeCode()));
                    }
                }

                //来源4
                for (Role role : tenant.getRoles()) {
                    String objectCode = role.getOuterObjectCode();
                    if (objectCode != null) {
                        from4.add(new OuterObject(objectCode, role.getOuterObjectTypeCode()));
                    }
                }
            }
        }

        //来源合并
        List<OuterObject> all = new ArrayList<>(from1);
        all.addAll(from2);
        all.addAll(from3);
        all.addAll(from4);
        ExtractUtils.fixIdAndKeyFields(all, idGenerator);

        //modelMap最后计算UserOuterObjectX时要用
        Map<IdType, OuterObject> modelMap = ExtractUtils.groupByIdAndMerge(all);

        Map<IdType, OuterObjectEntity> entityMap = modelMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().buildEntity(
                        //下面的拉曼达完成outerObjectCode到Id的转换，结果存入returnEntity
                        (model) -> {
                            OuterObject outerObject = (OuterObject) model;
                            OuterObjectEntity returnEntity = new OuterObjectEntity();
                            OuterObjectTypeEntity typeEntity = OuterObjectTypeExtractor.findByOuterObjectTypeCode(
                                    result, outerObject.getOuterObjectTypeCode(), outerObject.toHashString());
                            returnEntity.setOuterObjectTypeId(typeEntity.getId());
                            returnEntity.setReferenceCode(outerObject.getCode());
                            return returnEntity;
                        }
                )));

        //以OuterObjectTypeId分组
        Map<IdType, List<OuterObjectEntity>> collect = entityMap.values()
                .stream()
                .collect(Collectors.groupingBy(o -> new IdType(o.getOuterObjectTypeId())));

        //用result直接记录计算结果
        collect.forEach((k, v) ->
                result.getOuterObject()
                        .add(k, v.stream()
                                .collect(Collectors.toMap(o -> new IdType(o.getId()), Function.identity()))));

        //再来处理UserOuterObjectX的计算
        return modelMap;
    }

    /**
     * STEP2-2:提取userOuterObjectX
     */
    private static void extractUserOuterObjectX(Collection<OuterObject> outerObjects, IdGeneratorFactory idGeneratorFactory, DirectAccessControlSource result) {
        //准备id生成
        IdGenerator idGenerator = IdUtils.createIdGenerator(idGeneratorFactory);

        //key是outerObjectTypeCode
        Map<String, List<OuterObject>> group =
                outerObjects.stream().collect(Collectors.groupingBy(OuterObject::getOuterObjectTypeCode));

        for (List<OuterObject> list : group.values()) {
            //model转entity
            OuterObjectTypeEntity typeEntity = OuterObjectTypeExtractor.findByOuterObjectTypeCode(result,
                    list.get(0).getOuterObjectTypeCode(), list.get(0).toHashString());
            IdType outerObjectTypeId = new IdType(typeEntity.getId());

            Map<IdType, UserOuterObjectXEntity> data = extractUserOuterObjectX(outerObjectTypeId, list, idGenerator)
                    .stream().collect(Collectors.toMap(o -> new IdType(o.getId()), Function.identity()));
            //转map后存储到result中
            if (data.size() > 0) {
                result.getUserOuterObjectX().add(outerObjectTypeId, data);
            }
        }
    }

    /**
     * STEP2-2子方法: 按outerObjectTypeId分组处理
     * 要求outerObjects的outerObjectTypeId全部是一致的，且和参数outerObjectTypeId值相等
     */
    private static List<UserOuterObjectXEntity> extractUserOuterObjectX(IdType outerObjectTypeId, Collection<OuterObject> outerObjects, IdGenerator idGenerator) {
        //首先按scope作为key；然后是UserIdType作为key,以其对应的OuterObject.IdType作为value
        Map<String, Map<UserIdType, Set<IdType>>> pool = new HashMap<>();

        //把outerObjects重组数据形式，放到pool中
        outerObjects.stream().forEach(
                o -> {
                    List<User> users = o.getUsers();
                    IdType oId = IdUtils.createId(o.getId());
                    for (User u : users) {
                        UserIdType uid = IdUtils.createUserId(u.getId());
                        for (String scope : u.getScopes()) {
                            if (!StrictUtils.containsKey(pool, scope)) {
                                pool.put(scope, new HashMap<>());
                            }

                            Map<UserIdType, Set<IdType>> userWithOuterObjectsMap =
                                    StrictUtils.get(pool, scope);

                            if (!StrictUtils.containsKey(userWithOuterObjectsMap, uid)) {
                                userWithOuterObjectsMap.put(uid, new HashSet<>());
                            }
                            Set<IdType> outerObjectIds = StrictUtils.get(userWithOuterObjectsMap, uid);
                            outerObjectIds.add(oId);
                        }
                    }
                }
        );

        List<UserOuterObjectXEntity> result = new ArrayList<>();
        //再打包收集到result
        for (String scope : pool.keySet()) {

            Map<UserIdType, Set<IdType>> userWithOuterObjectsMap =
                    StrictUtils.get(pool, scope);

            for (UserIdType uId : userWithOuterObjectsMap.keySet()) {

                Set<IdType> outerObjectIds = StrictUtils.get(userWithOuterObjectsMap, uId);
                List<String> outerObjectIdStrings = outerObjectIds.stream()
                        .map(o -> o.getValue().toString())
                        .collect(Collectors.toList());
                String oIdLink = String.join(DELIMITER, outerObjectIdStrings.toArray(new String[outerObjectIdStrings.size()]));

                UserOuterObjectXEntity xEntity = new UserOuterObjectXEntity();
                xEntity.setId(idGenerator.generate().getValue());
                xEntity.setScope(scope);
                xEntity.setUserId(uId.getValue());
                xEntity.setOuterObjectIdList(oIdLink);
                xEntity.setOuterObjectTypeId(outerObjectTypeId.getValue());
                result.add(xEntity);
            }
        }
        return result;
    }

    static OuterObjectEntity findByOuterObjectTypeCodeAndOuterObjectCode(DirectAccessControlSource source
            , String outerObjectTypeCode, String outerObjectCode, String context) {

        OuterObjectTypeEntity outerObjectTypeEntity = OuterObjectTypeExtractor.findByOuterObjectTypeCode(
                source, outerObjectTypeCode, new OuterObject(outerObjectCode, outerObjectTypeCode).toHashString());

        Map<IdType, OuterObjectEntity> map = source.getOuterObject().get(IdUtils.createId(outerObjectTypeEntity.getId().toString()));

        String targetDesc = "Map<IdType, OuterObjectEntity>";
        if (map == null) {
            throw ErrorFactory.createNothingFoundError(targetDesc
                    , "outerObjectTypeId = " + outerObjectTypeEntity.getId()
                    , context);
        }

        targetDesc = "OuterObject";
        OuterObjectEntity entity = ExtractUtils.findFirst(map.values(),
                o -> StrictUtils.equals(outerObjectCode, o.getReferenceCode()));
        if (entity != null) {
            return entity;
        } else {
            throw ErrorFactory.createNothingFoundError(targetDesc
                    , "outerObjectTypeCode = " + outerObjectTypeCode + ", outerObjectCode" + outerObjectCode
                    , context);
        }
    }
}
