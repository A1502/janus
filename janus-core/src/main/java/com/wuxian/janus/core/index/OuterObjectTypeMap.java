package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer1.OuterObjectTypeStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class OuterObjectTypeMap extends SimpleIndexesMap<IdType, OuterObjectTypeStruct> {

    private final static String USED_BY_USER_GROUP = "usedByUserGroup";

    public OuterObjectTypeMap(SourceLoader<IdType, OuterObjectTypeStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(OuterObjectTypeStruct.class, USED_BY_USER_GROUP);
    }

    public List<OuterObjectTypeStruct> getByUsedByUserGroup(boolean usedByUserGroup) {
        return this.getByCondition((OuterObjectTypeStruct) new OuterObjectTypeStruct()
                        .setUsedByUserGroup(usedByUserGroup),
                USED_BY_USER_GROUP);
    }
}