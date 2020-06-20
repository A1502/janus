package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.primary.IdType;

/**
 * @author wuxian
 */

public class OuterObjectMap extends SimpleIndexesMap<IdType, OuterObjectStruct> {

    public OuterObjectMap(SourceLoader<IdType, OuterObjectStruct> sourceLoader) {
        super(sourceLoader);
    }
}