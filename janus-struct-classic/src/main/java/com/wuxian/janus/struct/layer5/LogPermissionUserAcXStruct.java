package com.wuxian.janus.struct.layer5;

import com.wuxian.janus.struct.prototype.layer5.LogPermissionUserAcXPrototype;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class LogPermissionUserAcXStruct extends LogPermissionUserAcXPrototype<Long, Long> {
    //<ID, UID>
}
