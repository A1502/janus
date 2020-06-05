package com.wuxian.janus.struct.layer1;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer1.RoleUserXPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */
@TableName("role_permission_x")
public class RoleUserXStruct extends RoleUserXPrototype<Long, String> {
    //<ID, UID>
}