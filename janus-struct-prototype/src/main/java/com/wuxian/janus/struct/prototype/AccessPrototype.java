package com.wuxian.janus.struct.prototype;

import com.wuxian.janus.struct.annotation.PropertyRemark;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class AccessPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    @PropertyRemark(value = "查看访问权:能否查看", example = "1")
    private boolean viewAccess;

    /**
    不同对象对'执行'的解释不同
    */
    @PropertyRemark(value = "执行访问权:能否执行", example = "true")
    private boolean executeAccess;

    @PropertyRemark(value = "编辑访问权:能否编辑", example = "true")
    private boolean editAccess;

    @PropertyRemark(value = "删除访问权:能否删除", example = "true")
    private boolean deleteAccess;

    @PropertyRemark(value = "启用访问权:能否启用", example = "true")
    private boolean enableAccess;
}
