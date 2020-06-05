package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer1.OuterObjectStruct;
import com.wuxian.janus.struct.primary.IdType;

/**
 * @author wuxian
 */

public class OuterObjectMap extends AutoFillMultipleIndexesMap<IdType, OuterObjectStruct> {

    public OuterObjectMap(SourceLoader<IdType, OuterObjectStruct> sourceLoader) {
        super(sourceLoader);
    }
}