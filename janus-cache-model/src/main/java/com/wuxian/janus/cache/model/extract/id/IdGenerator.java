package com.wuxian.janus.cache.model.extract.id;

import com.wuxian.janus.struct.primary.IdType;

import java.util.Collection;

public interface IdGenerator {

    void addUsed(Collection<IdType> used);

    IdType generate();

}
