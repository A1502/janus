package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.PermissionTemplatePrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@TableName("permission_template")
public class PermissionTemplateEntity extends PermissionTemplatePrototype<Long, String, String> {
    //<ID, UID, AID>
}
