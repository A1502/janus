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

    @PropertyRemark(value = "查看访问权是否过期", example = "true")
    private Boolean viewAccessExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "加入访问权过期时间", example = "2019-10-10 23:59")
    private Date executeAccessExpiryDate;

    @PropertyRemark(value = "加入访问权是否过期", example = "true")
    private Boolean executeAccessExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "编辑访问权过期时间", example = "2019-10-10 23:59")
    private Date editAccessExpiryDate;

    @PropertyRemark(value = "编辑访问权是否过期", example = "true")
    private Boolean editAccessExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "删除访问权过期时间", example = "2019-10-10 23:59")
    private Date deleteAccessExpiryDate;

    @PropertyRemark(value = "删除访问权是否过期", example = "true")
    private Boolean deleteAccessExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "启用访问权过期时间", example = "2019-10-10 23:59")
    private Date enableAccessExpiryDate;

    @PropertyRemark(value = "启用访问权是否过期", example = "true")
    private Boolean enableAccessExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "查看控制权过期时间", example = "2019-10-10 23:59")
    private Date viewControlExpiryDate;

    @PropertyRemark(value = "查看控制权是否过期", example = "true")
    private Boolean viewControlExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "加入控制权过期时间", example = "2019-10-10 23:59")
    private Date executeControlExpiryDate;

    @PropertyRemark(value = "加入控制权是否过期", example = "true")
    private Boolean executeControlExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "编辑控制权过期时间", example = "2019-10-10 23:59")
    private Date editControlExpiryDate;

    @PropertyRemark(value = "编辑控制权是否过期", example = "true")
    private Boolean editControlExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "删除控制权过期时间", example = "2019-10-10 23:59")
    private Date deleteControlExpiryDate;

    @PropertyRemark(value = "删除控制权是否过期", example = "true")
    private Boolean deleteControlExpired;

    //-------------------------------------------------------------------------------

    @PropertyRemark(value = "启用控制权过期时间", example = "2019-10-10 23:59")
    private Date enableControlExpiryDate;

    @PropertyRemark(value = "启用控制权是否过期", example = "true")
    private Boolean enableControlExpired;
}
