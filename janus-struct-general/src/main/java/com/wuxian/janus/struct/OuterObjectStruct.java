package com.wuxian.janus.struct;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer1.OuterObjectPrototype;

/**
 * 外部对象Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */

@TableName("outer_object")
public class OuterObjectStruct extends OuterObjectPrototype<Long, String> {
    //<ID, UID>
}
