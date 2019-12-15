package com.wuxian.janus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.UserOuterObjectXPrototype;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("user_outer_object_x")
public class UserOuterObjectXEntity extends UserOuterObjectXPrototype<Long, String> {
    //<ID, UID>
}
