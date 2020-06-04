package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.third.LogUserGroupUserXPrototype;
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
@TableName("log_user_group_user_x")
public class LogUserGroupUserXEntity extends LogUserGroupUserXPrototype<Long, Long> {
    //<ID, UID>
}
