package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class OuterObjectTypeMap extends AutoFillMultipleIndexesMap<IdType, OuterObjectTypeStruct> {

    private final static String USED_BY_USER_GROUP = "usedByUserGroup";

    public OuterObjectTypeMap(SourceLoader<IdType, OuterObjectTypeStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(OuterObjectTypeStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, USED_BY_USER_GROUP);
    }

    public List<OuterObjectTypeStruct> getByUsedByUserGroup(boolean usedByUserGroup) {
        return this.getByCondition((OuterObjectTypeStruct) new OuterObjectTypeStruct()
                        .setUsedByUserGroup(usedByUserGroup),
                USED_BY_USER_GROUP);
    }
}