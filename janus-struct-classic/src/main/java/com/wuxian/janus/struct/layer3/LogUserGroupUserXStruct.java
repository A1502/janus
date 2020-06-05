package com.wuxian.janus.struct.layer3;

import com.wuxian.janus.struct.prototype.layer3.LogUserGroupUserXPrototype;
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
public class LogUserGroupUserXStruct extends LogUserGroupUserXPrototype<Long, Long> {
    //<ID, UID>
}
