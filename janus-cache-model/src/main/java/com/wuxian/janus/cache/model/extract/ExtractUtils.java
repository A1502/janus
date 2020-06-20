package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.extract.id.IdGenerator;
import com.wuxian.janus.cache.model.extract.id.IdUtils;
import com.wuxian.janus.cache.model.source.BaseModel;
import com.wuxian.janus.cache.model.source.User;
import com.wuxian.janus.util.StrictUtils;
import com.wuxian.janus.core.cache.provider.TenantMap;
import com.wuxian.janus.core.cache.provider.TenantMapElement;
import com.wuxian.janus.core.critical.AccessControl;
import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.IdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import com.wuxian.janus.struct.primary.UserIdType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ExtractUtils {
    /**
     * list中有id，缺keyFields的补keyFields
     * list中有keyFields，缺id的补id
     * 并保证id相等的，keyFields必然相等;反之亦然
     */
    static <T extends BaseModel> void fixIdAndKeyFields(List<T> list, IdGenerator idGenerator) {

        List<T> idList = new ArrayList<>();
        List<T> keyList = new ArrayList<>();

        Map<String, T> idMap = new HashMap<>();
        Map<String, T> keyMap = new HashMap<>();

        for (T item : list) {
            if (item.getId() != null && item.keyFieldsHasValue()) {
                //id
                if (!StrictUtils.containsKey(idMap, item.getId())) {
                    idMap.put(item.getId(), item);
                } else {
                    T refItem = StrictUtils.get(idMap, item.getId());
                    if (!refItem.getKeyFieldsHash().equals(item.getKeyFieldsHash())) {
                        //id相等，keyFieldsHash却不等的是异常数据
                        throw ErrorFactory.createKeyFieldsConflictError(item.toString());
                    }
                }

                //keyFields
                if (!StrictUtils.containsKey(keyMap, item.getKeyFieldsHash())) {
                    keyMap.put(item.getKeyFieldsHash(), item);
                } else {
                    T refItem = StrictUtils.get(keyMap, item.getKeyFieldsHash());
                    if (!refItem.getId().equals(item.getId())) {
                        //keyFieldsHash相等，id却不等的是异常数据
                        throw ErrorFactory.createIdConflictError(item.toString());
                    }
                }
            } else if (item.getId() != null && !item.keyFieldsHasValue()) {
                idList.add(item);
            } else if (item.getId() == null && item.keyFieldsHasValue()) {
                keyList.add(item);
            } else {
                //这种数据不会存在 do nothing,除非可以调用setId(null),所以抛出如下IdNull异常
                throw ErrorFactory.createIdCannotBeNullError();
            }
        }

        //给idList补全keyFields
        for (T item : idList) {
            if (StrictUtils.containsKey(idMap, item.getId())) {
                //补全keyFields，这个是关键；fillKeyFields是个重要的抽象方法
                item.fillKeyFields(StrictUtils.get(idMap, item.getId()));
            }
        }
        //给keyList补全id
        for (T item : keyList) {
            if (StrictUtils.containsKey(keyMap, item.getKeyFieldsHash())) {
                item.setId(StrictUtils.get(keyMap, item.getKeyFieldsHash()).getId());
            }
        }

        //id为null的项的hash集合
        Set<String> hashesOfNullIdItem = new HashSet<>();
        //id为null的项的集合
        List<T> nullIdItemList = new ArrayList<>();
        for (T item : list) {
            if (item.getId() != null) {
                //提取所有id给idGenerator，登记为已使用
                idGenerator.addUsed(Collections.singleton(IdUtils.createId(item.getId())));
            } else {
                hashesOfNullIdItem.add(item.getKeyFieldsHash());
                nullIdItemList.add(item);
            }
        }

        //为每个没有id的keyFieldsHash生成配对的id
        Map<String, IdType> keyFieldsIdMapForNullItems = hashesOfNullIdItem.stream()
                .collect(Collectors.toMap(Function.identity(), o -> idGenerator.generate()));

        //最后一轮给没有id的补id
        nullIdItemList.forEach(o -> o.setId(keyFieldsIdMapForNullItems.get(o.getKeyFieldsHash()).getValue().toString()));
    }

    /**
     * By id进行分组,同组执行BaseModel的mergeFrom动作，每组只保留一个，并检查每组合并结果一定填充了关键字段，
     * 最后以map形式返回
     */
    static <E, T extends BaseModel<E>> Map<IdType, T> groupByIdAndMerge(List<T> list) {
        Map<IdType, T> result = new HashMap<>();
        Map<String, List<T>> map = list.stream().collect(Collectors.groupingBy(o -> o.getId()));

        for (String id : map.keySet()) {
            List<T> sameIdList = StrictUtils.get(map, id);
            T firstModel = sameIdList.get(0);
            for (int i = 1; i < sameIdList.size(); i++) {
                T otherModel = sameIdList.get(i);
                try {
                    firstModel.mergeFrom(otherModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            result.put(firstModel.buildIdType(), firstModel);
        }

        //检查是否都具备keyFields
        for (T item : result.values()) {
            if (!item.keyFieldsHasValue()) {
                throw ErrorFactory.createKeyFieldsNoValueError(item.toString());
            }
        }
        return result;
    }

    /**
     * group和merge后，再把每个model转struct返回
     */
    static <E, T extends BaseModel<E>> Map<IdType, E> groupByIdAndMergeToStruct(List<T> list, Function<BaseModel<E>, E> otherFieldsBuilder) {
        Map<IdType, T> map = groupByIdAndMerge(list);
        Map<IdType, E> result =
                map.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().buildStruct(otherFieldsBuilder)));
        return result;
    }

    /**
     * 找到第一个满足findBy条件的元素。(list中不可含有为null的项)
     */
    static <T> T findFirst(Iterable<T> list, Predicate<T> findBy) {
        for (T item : list) {
            if (findBy.test(item)) {
                return item;
            }
        }
        return null;
    }

    static <V> void loopTenantMapElement(TenantMap<IdType, V> tenantMap,
                                         Consumer<TenantMapElement<IdType, V>> processor) {

        Map<ApplicationIdType, Set<TenantIdType>> map = tenantMap.getIds();
        for (ApplicationIdType applicationId : map.keySet()) {

            Set<TenantIdType> tenantIds = StrictUtils.get(map, applicationId);
            for (TenantIdType tenantId : tenantIds) {
                processor.accept(tenantMap.getElement(applicationId, tenantId));
            }
        }
    }


    static Map<UserIdType, AccessControl> extractUserAccessControlMap(Map<User, AccessControl> users) {
        Map<UserIdType, AccessControl> result = new HashMap<>();

        for (Map.Entry<User, AccessControl> entry : users.entrySet()) {

            UserIdType userId = IdUtils.createUserId(entry.getKey().getId());

            //填充userIdAccessControlMap
            if (!StrictUtils.containsKey(result, userId)) {
                result.put(userId, entry.getValue());
            } else {
                StrictUtils.get(result, userId).union(entry.getValue());
            }
        }
        return result;
    }

    static Map<UserIdType, Set<String>> extractUserScopeMap(Map<User, AccessControl> users) {
        Map<UserIdType, Set<String>> result = new HashMap<>();

        for (Map.Entry<User, AccessControl> entry : users.entrySet()) {

            UserIdType userId = IdUtils.createUserId(entry.getKey().getId());

            //填充userIdScopeMap
            if (!StrictUtils.containsKey(result, userId)) {
                result.put(userId, new HashSet<>());
            }
            StrictUtils.get(result, userId).addAll(entry.getKey().getScopes());
        }

        return result;
    }
}