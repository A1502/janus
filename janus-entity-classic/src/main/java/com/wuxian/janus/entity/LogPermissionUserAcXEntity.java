package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.layer5.LogPermissionUserAcXPrototype;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class LogPermissionUserAcXEntity extends LogPermissionUserAcXPrototype<Long, Long> {
    //<ID, UID>
}
