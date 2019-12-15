package com.wuxian.janus.entity.prototype.base;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class AccessPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    @TableField("view_access")
    @ApiModelProperty(value = "查看访问权:能否查看", example = "1")
    private boolean viewAccess;

    /**
    不同对象对'执行'的解释不同
    */
    @TableField("execute_access")
    @ApiModelProperty(value = "执行访问权:能否执行", example = "true")
    private boolean executeAccess;

    @TableField("edit_access")
    @ApiModelProperty(value = "编辑访问权:能否编辑", example = "true")
    private boolean editAccess;

    @TableField("delete_access")
    @ApiModelProperty(value = "删除访问权:能否删除", example = "true")
    private boolean deleteAccess;

    @TableField("enable_access")
    @ApiModelProperty(value = "启用访问权:能否启用", example = "true")
    private boolean enableAccess;
}
