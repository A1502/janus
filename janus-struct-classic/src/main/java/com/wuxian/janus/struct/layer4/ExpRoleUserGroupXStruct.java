package com.wuxian.janus.struct.layer4;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer4.ExpRoleUserGroupXPrototype;
import lombok.NoArgsConstructor;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */

@NoArgsConstructor
@TableName("exp_role_user_group_x")
public class ExpRoleUserGroupXStruct extends ExpRoleUserGroupXPrototype<Long, Long> {
    //<ID, UID>
}
