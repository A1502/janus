package com.wuxian.janus.struct;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer2.PermissionTemplatePrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@TableName("permission_template")
public class PermissionTemplateStruct extends PermissionTemplatePrototype<Long, Long, Long> {
    //<ID, UID, AID>
}
