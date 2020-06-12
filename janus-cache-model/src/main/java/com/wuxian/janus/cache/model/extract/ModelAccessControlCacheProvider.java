package com.wuxian.janus.cache.model.extract;

import com.wuxian.janus.cache.model.extract.id.IdGeneratorFactory;
import com.wuxian.janus.cache.model.extract.id.LongIdGeneratorFactory;
import com.wuxian.janus.cache.model.source.ApplicationGroup;
import com.wuxian.janus.core.cache.provider.DirectAccessControlCacheProvider;
import com.wuxian.janus.core.cache.provider.DirectAccessControlSource;
import com.wuxian.janus.core.cache.provider.ProxyAccessControlCacheProvider;

public class ModelAccessControlCacheProvider extends ProxyAccessControlCacheProvider {

    public ModelAccessControlCacheProvider(ApplicationGroup applicationGroup, IdGeneratorFactory idGeneratorFactory) {

        SourceExtractor sourceExtractor = new SourceExtractor(idGeneratorFactory);

        DirectAccessControlSource source = sourceExtractor.extract(applicationGroup);

        DirectAccessControlCacheProvider cacheProvider = DirectAccessControlCacheProvider.createFrom(source);

        setProvider(cacheProvider);
    }

    public ModelAccessControlCacheProvider(ApplicationGroup applicationGroup) {
        this(applicationGroup, new LongIdGeneratorFactory());
    }
}
