package com.wuxian.janus.core.index;

import com.wuxian.janus.core.basis.AutoFillMultipleIndexesMap;
import com.wuxian.janus.core.basis.SourceLoader;
import com.wuxian.janus.entity.RoleEntity;
import com.wuxian.janus.entity.primary.IdType;

import java.util.List;

/**
 * @author wuxian
 */

public class RoleMap extends AutoFillMultipleIndexesMap<IdType, RoleEntity> {

    private static final String CODE = "code";

    public RoleMap(SourceLoader<IdType, RoleEntity> sourceLoader) {
        super(sourceLoader);

        this.createIndex(RoleEntity.class, (Object[] fields) ->
                new String[]{
                        safeToString(fields[0])
                }, CODE);
    }

    public List<RoleEntity> getByCode(String code) {
        return this.getByCondition((RoleEntity) new RoleEntity()
                        .setCode(code),
                CODE);
    }
}