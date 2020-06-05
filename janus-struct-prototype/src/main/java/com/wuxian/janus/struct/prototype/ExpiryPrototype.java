package com.wuxian.janus.struct.prototype;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Setter
@Getter
public class ExpiryPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    @ApiModelProperty(value = "查看访问权过期时间", example = "2019-10-10 23:59")
    private Date viewAccessExpiryDate;

    @ApiModelProperty(value = "加入访问权过期时间", example = "2019-10-10 23:59")
    private Date executeAccessExpiryDate;

    @ApiModelProperty(value = "编辑访问权过期时间", example = "2019-10-10 23:59")
    private Date editAccessExpiryDate;

    @ApiModelProperty(value = "删除访问权过期时间", example = "2019-10-10 23:59")
    private Date deleteAccessExpiryDate;

    @ApiModelProperty(value = "启用访问权过期时间", example = "2019-10-10 23:59")
    private Date enableAccessExpiryDate;

    @ApiModelProperty(value = "查看控制权过期时间", example = "2019-10-10 23:59")
    private Date viewControlExpiryDate;

    @ApiModelProperty(value = "加入控制权过期时间", example = "2019-10-10 23:59")
    private Date executeControlExpiryDate;

    @ApiModelProperty(value = "编辑控制权过期时间", example = "2019-10-10 23:59")
    private Date editControlExpiryDate;

    @ApiModelProperty(value = "删除控制权过期时间", example = "2019-10-10 23:59")
    private Date deleteControlExpiryDate;

    @ApiModelProperty(value = "启用控制权过期时间", example = "2019-10-10 23:59")
    private Date enableControlExpiryDate;
}
