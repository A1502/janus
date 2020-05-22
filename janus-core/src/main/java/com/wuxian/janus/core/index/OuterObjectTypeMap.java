package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.OuterObjectTypeEntity;
import com.wuxian.janus.entity.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class OuterObjectTypeMap extends AutoFillMultipleIndexesMap<IdType, OuterObjectTypeEntity> {

    private final static String USED_BY_USER_GROUP = "usedByUserGroup";

    public OuterObjectTypeMap(SourceLoader<IdType, OuterObjectTypeEntity> sourceLoader) {
        super(sourceLoader);
        this.createIndex(OuterObjectTypeEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, USED_BY_USER_GROUP);
    }

    public List<OuterObjectTypeEntity> getByUsedByUserGroup(boolean usedByUserGroup) {
        return this.getByCondition((OuterObjectTypeEntity) new OuterObjectTypeEntity()
                        .setUsedByUserGroup(usedByUserGroup),
                USED_BY_USER_GROUP);
    }
}