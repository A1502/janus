package com.wuxian.janus.cache.source.extract;

import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.cache.source.ErrorFactory;
import com.wuxian.janus.cache.source.IdGenerator;
import com.wuxian.janus.cache.source.IdUtils;
import com.wuxian.janus.cache.source.model.CodeModel;
import com.wuxian.janus.cache.source.model.BaseModel;
import com.wuxian.janus.core.StrictUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ExtractUtils {
    /**
     * list中缺Id和Code的项补全
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
                        throw ErrorFactory.createKeyFieldsConflictError(item.toString());
                    }
                }

                //keyFields
                if (!StrictUtils.containsKey(keyMap, item.getKeyFieldsHash())) {
                    keyMap.put(item.getKeyFieldsHash(), item);
                } else {
                    T refItem = StrictUtils.get(keyMap, item.getKeyFieldsHash());
                    if (!refItem.getId().equals(item.getId())) {
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
                item.fillKeyFields(StrictUtils.get(idMap, item.getId()));
            }
        }
        //给keyList补全id
        for (T item : keyList) {
            if (StrictUtils.containsKey(keyMap, item.getKeyFieldsHash())) {
                item.setId(StrictUtils.get(keyMap, item.getKeyFieldsHash()).getId());
            }
        }

        //提取所有id给idGenerator，登记为已使用（"Used"）
        Set<IdType> allIds = list.stream().filter(o -> o.getId() != null).map(o ->
                IdUtils.createId(o.getId().toString())).collect(Collectors.toSet());
        idGenerator.addUsed(allIds);

        Set<String> idleKeyFieldsSet = new HashSet<>();
        List<T> idleKeyFieldsItemList = new ArrayList<>();
        for (T item : list) {
            if (item.getId() != null) {
                idGenerator.addUsed(Collections.singleton(IdUtils.createId(item.getId())));
            } else {
                idleKeyFieldsSet.add(item.getKeyFieldsHash());
                idleKeyFieldsItemList.add(item);
            }
        }

        //为每个没有id的keyFieldsHas分配好id
        Map<String, IdType> idleKeyFieldsSetMap = idleKeyFieldsSet.stream()
                .collect(Collectors.toMap(Function.identity(), o -> idGenerator.generate()));

        //最后一轮给没有id的补id
        idleKeyFieldsItemList.forEach(o -> o.setId(idleKeyFieldsSetMap.get(o.getKeyFieldsHash()).getValue().toString()));
    }

    /**
     * By id进行分组,同组执行BaseModel的mergeFrom动作，每组只保留一个，并检查每组合并结果一定填充了关键字段，最后以map形式返回
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
     * group和merge后，再把每个model转entity返回
     */
    static <E, T extends BaseModel<E>> Map<IdType, E> groupByIdAndMergeToEntity(List<T> list, Function<BaseModel<E>, E> otherFiledBuilder) {
        Map<IdType, T> map = groupByIdAndMerge(list);
        Map<IdType, E> result =
                map.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().buildEntity(otherFiledBuilder)));
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
}
