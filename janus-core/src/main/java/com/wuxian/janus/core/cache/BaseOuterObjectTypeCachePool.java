package com.wuxian.janus.core.cache;

import com.wuxian.janus.entity.OuterObjectEntity;
import com.wuxian.janus.entity.OuterObjectTypeEntity;
import com.wuxian.janus.entity.primary.IdType;
import com.wuxian.janus.core.StrictUtils;
import com.wuxian.janus.core.index.OuterObjectMap;
import com.wuxian.janus.core.index.OuterObjectTypeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Solomon
 */

public abstract class BaseOuterObjectTypeCachePool {

    //<editor-fold desc="OuterObjectTypeMap">

    private OuterObjectTypeMap outerObjectTypeMap = null;

    protected abstract OuterObjectTypeMap createOuterObjectTypeMap();

    public OuterObjectTypeMap getOuterObjectTypeMap() {
        if (outerObjectTypeMap == null) {
            outerObjectTypeMap = createOuterObjectTypeMap();
        }
        return outerObjectTypeMap;
    }

    /**
     * 根据OuterObjectTypeMap收缩数据，把无效的BaseOuterObjectTypeCache去除
     */
    public void shrink() {
        List<IdType> outerObjectTypeIds = getOuterObjectTypeMap().getAll()
                .stream().map(o -> new IdType(o.getId())).collect(Collectors.toList());

        List<IdType> keys = new ArrayList<>(baseOuterObjectTypeCachePool.keySet());
        for (IdType key : keys) {
            if (!outerObjectTypeIds.contains(key)) {
                baseOuterObjectTypeCachePool.remove(key);
            }
        }
    }

    //</editor-fold>

    //<editor-fold desc="BaseOuterObjectTypeCache">

    private Map<IdType, BaseOuterObjectTypeCache> baseOuterObjectTypeCachePool = new HashMap<>();

    protected abstract BaseOuterObjectTypeCache createOuterObjectTypeCache(IdType outerObjectTypeId);

    public BaseOuterObjectTypeCache getOuterObjectTypeCache(IdType outerObjectTypeId) {
        if (!StrictUtils.containsKey(baseOuterObjectTypeCachePool, outerObjectTypeId)) {
            BaseOuterObjectTypeCache item = createOuterObjectTypeCache(outerObjectTypeId);
            baseOuterObjectTypeCachePool.put(outerObjectTypeId, item);
            return item;
        } else {
            return StrictUtils.get(baseOuterObjectTypeCachePool, outerObjectTypeId);
        }
    }

    public OuterObjectEntity getOuterObjectInstance(IdType outerObjectId) {

        OuterObjectTypeMap outerObjectTypeMap = getOuterObjectTypeMap();

        for (OuterObjectTypeEntity outerObjectTypeEntity : outerObjectTypeMap.getAll()) {
            BaseOuterObjectTypeCache cache = getOuterObjectTypeCache(new IdType(outerObjectTypeEntity.getId()));
            OuterObjectMap outerObjectSource = cache.getOuterObject();
            OuterObjectEntity entity = outerObjectSource.getByKey(outerObjectId);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    //</editor-fold>
}
