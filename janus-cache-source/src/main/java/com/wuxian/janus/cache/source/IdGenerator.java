package com.wuxian.janus.cache.source;

import com.wuxian.janus.struct.primary.IdType;

import java.util.Collection;

public interface IdGenerator {

    void addUsed(Collection<IdType> used);

    IdType generate();

}
