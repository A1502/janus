package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer1.RoleOtherXStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleOtherXMap extends AutoFillMultipleIndexesMap<IdType, RoleOtherXStruct> {

    private static final String EXECUTE_ACCESS = "executeAccess";

    public RoleOtherXMap(SourceLoader<IdType, RoleOtherXStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleOtherXStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, EXECUTE_ACCESS);
    }

    public List<RoleOtherXStruct> getByExecuteAccess(boolean executeAccess) {
        return this.getByCondition((RoleOtherXStruct) new RoleOtherXStruct()
                        .setExecuteAccess(executeAccess),
                EXECUTE_ACCESS);
    }
}
