package com.wuxian.janus.struct.layer6;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer6.PermissionDetailPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */

@TableName("permission_detail")
public class PermissionDetailStruct extends PermissionDetailPrototype<Long, String> {
    //<ID, UID>
}
