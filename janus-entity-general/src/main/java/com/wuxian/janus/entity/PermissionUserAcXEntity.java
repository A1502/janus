package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.PermissionUserAcXPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */
@TableName("permission_user_ac_x")
public class PermissionUserAcXEntity extends PermissionUserAcXPrototype<Long, String, String> {
    //<ID, UID, TID>
}
