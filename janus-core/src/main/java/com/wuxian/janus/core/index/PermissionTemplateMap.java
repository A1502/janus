package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.PermissionTemplateEntity;
import com.wuxian.janus.entity.primary.IdType;

import java.util.List;

public class PermissionTemplateMap extends AutoFillMultipleIndexesMap<IdType, PermissionTemplateEntity> {

    private static final String CODE = "code";

    public PermissionTemplateMap(SourceLoader<IdType, PermissionTemplateEntity> sourceLoader) {
        super(sourceLoader);

        this.createIndex(PermissionTemplateEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, CODE);
    }

    public List<PermissionTemplateEntity> getByCode(String code) {
        return this.getByCondition((PermissionTemplateEntity) new PermissionTemplateEntity()
                        .setCode(code),
                CODE);
    }
}
