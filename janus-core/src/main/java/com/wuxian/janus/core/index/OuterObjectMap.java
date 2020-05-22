package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.OuterObjectEntity;
import com.wuxian.janus.entity.primary.IdType;

/**
 * @author wuxian
 */

public class OuterObjectMap extends AutoFillMultipleIndexesMap<IdType, OuterObjectEntity> {

    public OuterObjectMap(SourceLoader<IdType, OuterObjectEntity> sourceLoader) {
        super(sourceLoader);
    }
}