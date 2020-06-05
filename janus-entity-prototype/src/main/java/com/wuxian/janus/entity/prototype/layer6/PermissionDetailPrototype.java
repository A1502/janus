package com.wuxian.janus.entity.prototype.layer6;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.JanusPrototype;
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
@TableName("permission_detail")
public class PermissionDetailPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    @TableField("permission_template_id")
    @ApiModelProperty(value = "权限模板id", example = "1")
    private ID permissionTemplateId;

    @TableField("sort_no")
    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sortNo;

    @TableField("image_path")
    @ApiModelProperty(value = "图片路径，可以只存相对路径", example = "/xxx/yyy/zzz?id=888")
    private String imagePath;

    @TableField("description")
    @ApiModelProperty(value = "描述", example = "这是描述文本...")
    private String description;

    //无需version
}
