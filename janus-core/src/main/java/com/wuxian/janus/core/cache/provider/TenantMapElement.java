package com.wuxian.janus.core.cache.provider;

import com.wuxian.janus.struct.primary.ApplicationIdType;
import com.wuxian.janus.struct.primary.TenantIdType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
public class TenantMapElement<K, V> {

    @Getter
    private ApplicationIdType applicationId;

    @Getter
    private TenantIdType tenantId;

    @Getter
    private Map<K, V> element;
}
