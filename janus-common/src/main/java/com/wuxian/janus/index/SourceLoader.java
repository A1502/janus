package com.wuxian.janus.index;

import java.util.Map;

/**
 * @author wuxian
 */

public interface SourceLoader<K, V> {
    Map<K, V> load();
}
