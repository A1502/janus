package com.wuxian.janus.struct.layer3;import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.wuxian.janus.struct.prototype.layer3.LogUserGroupUserXPrototype;

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
public class LogUserGroupUserXStruct extends LogUserGroupUserXPrototype<Long, String> {
    //<ID, UID>
}
