package com.wuxian.janus.core.index;

import com.wuxian.janus.index.SimpleIndexesMap;
import com.wuxian.janus.index.SourceLoader;
import com.wuxian.janus.struct.layer1.RoleStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleMap extends SimpleIndexesMap<IdType, RoleStruct> {

    private static final String CODE = "code";

    public RoleMap(SourceLoader<IdType, RoleStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(RoleStruct.class, CODE);
    }

    public List<RoleStruct> getByCode(String code) {
        return this.getByCondition((RoleStruct) new RoleStruct()
                        .setCode(code),
                CODE);
    }
}