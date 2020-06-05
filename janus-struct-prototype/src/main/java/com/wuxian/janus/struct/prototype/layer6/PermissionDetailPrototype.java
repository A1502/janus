package com.wuxian.janus.struct.prototype.layer6;

import com.wuxian.janus.struct.prototype.JanusPrototype;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/09
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class PermissionDetailPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限模板id", example = "1")
    private ID permissionTemplateId;

    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sortNo;

    @ApiModelProperty(value = "图片路径，可以只存相对路径", example = "/xxx/yyy/zzz?id=888")
    private String imagePath;

    @ApiModelProperty(value = "描述", example = "这是描述文本...")
    private String description;

    //无需version
}
