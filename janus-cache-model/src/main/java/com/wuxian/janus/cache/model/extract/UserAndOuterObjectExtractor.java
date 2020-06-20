package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.source.*;
import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.util.StrictUtils;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.layer6.UserOuterObjectXStruct;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class UserAndOuterObjectExtractor {

    static class OuterObjectPair {
        OuterObjectTypeStruct outerObjectTypeStruct;
        OuterObjectStruct outerObjectStruct;
    }

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
        List<OuterObject> from5 = new ArrayList<>();

        for (Application application : applicationGroup.getApplications()) {
            for (Tenant tenant : application.getTenants()) {
                //来源2
                for (Permission permission : tenant.getPermissions()) {
                    fill(from2, permission.getOuterObjectTypeCode(), permission.getOuterObjectCode());
                }

                //来源3
                for (UserGroup userGroup : tenant.getUserGroups()) {
                    fill(from3, userGroup.getOuterObjectTypeCode(), userGroup.getOuterObjectCode());
                }

                //来源4
                for (Role role : tenant.getRoles()) {
                    fill(from4, role.getOuterObjectTypeCode(), role.getOuterObjectCode());
                    for (Permission permission : role.getPermissions()) {
                        //来源5
                        fill(from5, permission.getOuterObjectTypeCode(), permission.getOuterObjectCode());
                    }
                }
            }
        }

        //来源合并
        List<OuterObject> all = new ArrayList<>(from1);
        all.addAll(from2);
        all.addAll(from3);
        all.addAll(from4);
        all.addAll(from5);
        ExtractUtils.fixIdAndKeyFields(all, idGenerator);

        //modelMap最后计算UserOuterObjectX时要用
        Map<IdType, OuterObject> modelMap = ExtractUtils.groupByIdAndMerge(all);

        Map<IdType, OuterObjectStruct> structMap = modelMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().buildStruct(
                        //下面的拉曼达完成outerObjectCode到Id的转换，结果存入returnStruct
                        (model) -> {
                            OuterObject outerObject = (OuterObject) model;
                            OuterObjectStruct returnStruct = new OuterObjectStruct();
                            OuterObjectTypeStruct typeStruct = OuterObjectTypeExtractor.findByOuterObjectTypeCode(
                                    result, outerObject.getOuterObjectTypeCode(), outerObject.toHashString());
                            returnStruct.setOuterObjectTypeId(typeStruct.getId());
                            returnStruct.setReferenceCode(outerObject.getCode());
                            return returnStruct;
                        }
                )));

        //以OuterObjectTypeId分组
        Map<IdType, List<OuterObjectStruct>> collect = structMap.values()
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

    static void fill(List<OuterObject> list, String outerObjectTypeCode, String outerObjectCode) {
        if (outerObjectCode != null) {
            list.add(new OuterObject(outerObjectCode, outerObjectTypeCode));
        }
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
            //model转struct
            OuterObjectTypeStruct typeStruct = OuterObjectTypeExtractor.findByOuterObjectTypeCode(result,
                    list.get(0).getOuterObjectTypeCode(), list.get(0).toHashString());
            IdType outerObjectTypeId = new IdType(typeStruct.getId());

            Map<IdType, UserOuterObjectXStruct> data = extractUserOuterObjectX(outerObjectTypeId, list, idGenerator)
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
    private static List<UserOuterObjectXStruct> extractUserOuterObjectX(IdType outerObjectTypeId, Collection<OuterObject> outerObjects, IdGenerator idGenerator) {
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

        List<UserOuterObjectXStruct> result = new ArrayList<>();
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

                UserOuterObjectXStruct xStruct = new UserOuterObjectXStruct();
                xStruct.setId(idGenerator.generate().getValue());
                xStruct.setScope(scope);
                xStruct.setUserId(uId.getValue());
                xStruct.setOuterObjectIdList(oIdLink);
                xStruct.setOuterObjectTypeId(outerObjectTypeId.getValue());
                result.add(xStruct);
            }
        }
        return result;
    }

    static OuterObjectPair findByOuterObjectTypeCodeAndOuterObjectCode(DirectAccessControlSource source
            , String outerObjectTypeCode, String outerObjectCode, String context) {

        OuterObjectTypeStruct outerObjectTypeStruct = OuterObjectTypeExtractor.findByOuterObjectTypeCode(
                source, outerObjectTypeCode, new OuterObject(outerObjectCode, outerObjectTypeCode).toHashString());

        Map<IdType, OuterObjectStruct> map = source.getOuterObject().get(IdUtils.createId(outerObjectTypeStruct.getId().toString()));

        String targetDesc = "Map<IdType, OuterObjectStruct>";
        if (map == null) {
            throw ErrorFactory.createNothingFoundError(targetDesc
                    , "outerObjectTypeId = " + outerObjectTypeStruct.getId()
                    , context);
        }

        targetDesc = "OuterObject";
        OuterObjectStruct struct = ExtractUtils.findFirst(map.values(),
                o -> StrictUtils.equals(outerObjectCode, o.getReferenceCode()));
        if (struct != null) {
            OuterObjectPair result = new OuterObjectPair();
            result.outerObjectTypeStruct = outerObjectTypeStruct;
            result.outerObjectStruct = struct;
            return result;
        } else {
            throw ErrorFactory.createNothingFoundError(targetDesc
                    , "outerObjectTypeCode = " + outerObjectTypeCode + ", outerObjectCode" + outerObjectCode
                    , context);
        }
    }
}
