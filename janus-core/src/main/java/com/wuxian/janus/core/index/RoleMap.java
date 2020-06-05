package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.RoleStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleMap extends AutoFillMultipleIndexesMap<IdType, RoleStruct> {

    private static final String CODE = "code";

    public RoleMap(SourceLoader<IdType, RoleStruct> sourceLoader) {
        super(sourceLoader);

        this.createIndex(RoleStruct.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, CODE);
    }

    public List<RoleStruct> getByCode(String code) {
        return this.getByCondition((RoleStruct) new RoleStruct()
                        .setCode(code),
                CODE);
    }
}