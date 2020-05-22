package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.wuxian.janus.entity.prototype.LogPermissionUserAcXPrototype;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("log_permission_user_ac_x")
public class LogPermissionUserAcXEntity extends LogPermissionUserAcXPrototype<Long, String> {
    //<ID, UID>
}
