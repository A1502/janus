package com.wuxian.janus.struct.prototype;

import com.wuxian.janus.struct.annotation.PropertyRemark;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class ControlPrototype<ID, UID> extends AccessPrototype<ID, UID> {

    @PropertyRemark(value = "查看控制权:能否管理他人查看", example = "true")
    private boolean viewControl;

    /**
    不同对象对'执行'的解释不同
     */
    @PropertyRemark(value = "执行控制权:能否管理他人执行", example = "true")
    private boolean executeControl;

    @PropertyRemark(value = "编辑控制权:能否管理他人编辑", example = "true")
    private boolean editControl;

    @PropertyRemark(value = "删除控制权:能否管理他人删除", example = "true")
    private boolean deleteControl;

    @PropertyRemark(value = "启用控制权:能否管理他人启用", example = "true")
    private boolean enableControl;
}
