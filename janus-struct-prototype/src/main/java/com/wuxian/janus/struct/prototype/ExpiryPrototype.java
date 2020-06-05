package com.wuxian.janus.struct.prototype;

import com.wuxian.janus.struct.annotation.PropertyRemark;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Setter
@Getter
public class ExpiryPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    @PropertyRemark(value = "查看访问权过期时间", example = "2019-10-10 23:59")
    private Date viewAccessExpiryDate;

    @PropertyRemark(value = "加入访问权过期时间", example = "2019-10-10 23:59")
    private Date executeAccessExpiryDate;

    @PropertyRemark(value = "编辑访问权过期时间", example = "2019-10-10 23:59")
    private Date editAccessExpiryDate;

    @PropertyRemark(value = "删除访问权过期时间", example = "2019-10-10 23:59")
    private Date deleteAccessExpiryDate;

    @PropertyRemark(value = "启用访问权过期时间", example = "2019-10-10 23:59")
    private Date enableAccessExpiryDate;

    @PropertyRemark(value = "查看控制权过期时间", example = "2019-10-10 23:59")
    private Date viewControlExpiryDate;

    @PropertyRemark(value = "加入控制权过期时间", example = "2019-10-10 23:59")
    private Date executeControlExpiryDate;

    @PropertyRemark(value = "编辑控制权过期时间", example = "2019-10-10 23:59")
    private Date editControlExpiryDate;

    @PropertyRemark(value = "删除控制权过期时间", example = "2019-10-10 23:59")
    private Date deleteControlExpiryDate;

    @PropertyRemark(value = "启用控制权过期时间", example = "2019-10-10 23:59")
    private Date enableControlExpiryDate;
}
