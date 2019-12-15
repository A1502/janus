package com.wuxian.janus.core.basis;

import java.util.Map;

/**
 * @author Solomon
 */

public interface SourceLoader<K, V> {
    Map<K, V> load();
}
