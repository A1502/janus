package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer1.RoleOtherXStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleOtherXMap extends SimpleIndexesMap<IdType, RoleOtherXStruct> {

    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleOtherXMap(SourceLoader<IdType, RoleOtherXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleOtherXStruct.class, EXECUTE_ACCESS);
    }

    public List<RoleOtherXStruct> getByExecuteAccess(boolean executeAccess) {
        return this.getByCondition((RoleOtherXStruct) new RoleOtherXStruct()
                        .setExecuteAccess(executeAccess),
                EXECUTE_ACCESS);
    }
}
