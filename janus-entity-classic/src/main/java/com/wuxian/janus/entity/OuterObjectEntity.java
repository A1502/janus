package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.OuterObjectPrototype;

/**
 * 外部对象Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/11
 */

@TableName("outer_object")
public class OuterObjectEntity extends OuterObjectPrototype<Long, Long> {
    //<ID, UID>
}
