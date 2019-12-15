package com.wuxian.janus.entity.prototype.base;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class ControlPrototype<ID, UID> extends AccessPrototype<ID, UID> {

    @TableField("view_control")
    @ApiModelProperty(value = "查看控制权:能否管理他人查看", example = "true")
    private boolean viewControl;

    /**
    不同对象对'执行'的解释不同
     */
    @TableField("execute_control")
    @ApiModelProperty(value = "执行控制权:能否管理他人执行", example = "true")
    private boolean executeControl;

    @TableField("edit_control")
    @ApiModelProperty(value = "编辑控制权:能否管理他人编辑", example = "true")
    private boolean editControl;

    @TableField("delete_control")
    @ApiModelProperty(value = "删除控制权:能否管理他人删除", example = "true")
    private boolean deleteControl;

    @TableField("enable_control")
    @ApiModelProperty(value = "启用控制权:能否管理他人启用", example = "true")
    private boolean enableControl;
}
