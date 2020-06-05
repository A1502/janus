package com.wuxian.janus.cache.source;

import com.wuxian.janus.struct.primary.IdType;

import java.util.Collection;

public class LongIdGenerator implements IdGenerator {

    private Long next = 0L;

    private Long tryParse(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void addUsed(Collection<IdType> used) {
        for (IdType one : used) {
            if (one != null) {
                Long data = tryParse(one.getValue().toString());
                if (data != null && data >= next) {
                    next = data + 1;
                }
            }
        }
    }

    @Override
    public IdType generate() {
        IdType newOne = new IdType(null);
        newOne.setStringValue(next.toString());
        next = next + 1;
        return newOne;
    }
}
