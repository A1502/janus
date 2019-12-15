package com.wuxian.janus.entity;import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.wuxian.janus.entity.prototype.LogUserGroupUserXPrototype;

/**
 * Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/09
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("log_user_group_user_x")
public class LogUserGroupUserXEntity extends LogUserGroupUserXPrototype<Long, String> {
    //<ID, UID>
}
