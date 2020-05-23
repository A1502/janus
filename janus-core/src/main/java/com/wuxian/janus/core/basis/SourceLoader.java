package com.wuxian.janus.core.basis;

import java.util.Map;

/**
 * @author wuxian
 */

public interface SourceLoader<K, V> {
    Map<K, V> load();
}
