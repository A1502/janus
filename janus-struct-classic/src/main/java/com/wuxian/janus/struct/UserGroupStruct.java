package com.wuxian.janus.struct;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer1.UserGroupPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@TableName("role_permission_x")
public class UserGroupStruct extends UserGroupPrototype<Long, Long, Long, Long> {
    //<ID, UID, AID, TID>
}