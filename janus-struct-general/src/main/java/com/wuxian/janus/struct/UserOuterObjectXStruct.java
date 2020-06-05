package com.wuxian.janus.struct;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.layer6.UserOuterObjectXPrototype;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("user_outer_object_x")
public class UserOuterObjectXStruct extends UserOuterObjectXPrototype<Long, String> {
    //<ID, UID>
}
