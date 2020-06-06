package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import com.wuxian.janus.struct.primary.IdType;

import java.util.List;

public class PermissionTemplateMap extends SimpleIndexesMap<IdType, PermissionTemplateStruct> {

    private static final String CODE = "code";

    public PermissionTemplateMap(SourceLoader<IdType, PermissionTemplateStruct> sourceLoader) {
        super(sourceLoader);
        this.createIndex(PermissionTemplateStruct.class, CODE);
    }

    public List<PermissionTemplateStruct> getByCode(String code) {
        return this.getByCondition((PermissionTemplateStruct) new PermissionTemplateStruct()
                        .setCode(code),
                CODE);
    }
}
