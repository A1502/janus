package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.layer1.OuterObjectTypePrototype;

/**
 * 外部对象类型实体
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */

@TableName("outer_object_type")
public class OuterObjectTypeEntity extends OuterObjectTypePrototype<Long, Long> {
    //<ID, UID>
}
