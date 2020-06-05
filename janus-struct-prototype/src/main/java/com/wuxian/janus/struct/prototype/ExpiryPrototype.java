package com.wuxian.janus.struct.prototype;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Setter
@Getter
public class ExpiryPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    @TableField("view_access_expiry_date")
    @ApiModelProperty(value = "查看访问权过期时间", example = "2019-10-10 23:59")
    private Date viewAccessExpiryDate;

    @TableField("execute_access_expiry_date")
    @ApiModelProperty(value = "加入访问权过期时间", example = "2019-10-10 23:59")
    private Date executeAccessExpiryDate;

    @TableField("edit_access_expiry_date")
    @ApiModelProperty(value = "编辑访问权过期时间", example = "2019-10-10 23:59")
    private Date editAccessExpiryDate;

    @TableField("delete_access_expiry_date")
    @ApiModelProperty(value = "删除访问权过期时间", example = "2019-10-10 23:59")
    private Date deleteAccessExpiryDate;

    @TableField("enable_access_expiry_date")
    @ApiModelProperty(value = "启用访问权过期时间", example = "2019-10-10 23:59")
    private Date enableAccessExpiryDate;

    @TableField("view_control_expiry_date")
    @ApiModelProperty(value = "查看控制权过期时间", example = "2019-10-10 23:59")
    private Date viewControlExpiryDate;

    @TableField("execute_control_expiry_date")
    @ApiModelProperty(value = "加入控制权过期时间", example = "2019-10-10 23:59")
    private Date executeControlExpiryDate;

    @TableField("edit_control_expiry_date")
    @ApiModelProperty(value = "编辑控制权过期时间", example = "2019-10-10 23:59")
    private Date editControlExpiryDate;

    @TableField("delete_control_expiry_date")
    @ApiModelProperty(value = "删除控制权过期时间", example = "2019-10-10 23:59")
    private Date deleteControlExpiryDate;

    @TableField("enable_control")
    @ApiModelProperty(value = "启用控制权过期时间", example = "2019-10-10 23:59")
    private Date enableControlExpiryDate;
}
