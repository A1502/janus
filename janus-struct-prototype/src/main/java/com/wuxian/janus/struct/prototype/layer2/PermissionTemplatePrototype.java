package com.wuxian.janus.struct.prototype.layer2;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.struct.prototype.JanusPrototype;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Prototype类
 *
 * @author wuxian
 * @email
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("permission_template")
public class PermissionTemplatePrototype<ID, UID, AID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id, AID applicationId, String code, Boolean multiple, String name
            , String description, String permissionLevel, String permissionType, ID outerObjectTypeId
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate, Integer version) {
        setApplicationId(applicationId);
        setCode(code);
        setMultiple(multiple);
        setName(name);
        setDescription(description);
        setPermissionLevel(permissionLevel);
        setPermissionType(permissionType);
        setOuterObjectTypeId(outerObjectTypeId);
        setVersion(version);
        setId(id);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
    }

    @TableField("application_id")
    @ApiModelProperty(value = "应用id", example = "1")
    private AID applicationId;

    @TableField("code")
    @ApiModelProperty(value = "编码", example = "Student.Edit")
    private String code;

    /**
    操作级权限 = false，数据级权限 = true，数据级权限可以通过permission表的outerObjectId来指定一条数据
     */
    @TableField("multiple")
    @ApiModelProperty(value = "操作级权限 = false，数据级权限 = true", example = "false")
    private Boolean multiple;

    @TableField("name")
    @ApiModelProperty(value = "名称", example = "编辑学生")
    private String name;

    @TableField("description")
    @ApiModelProperty(value = "描述", example = "这是权限模板的描述...")
    private String description;

    @TableField("permission_level")
    @ApiModelProperty(value = "权限敏感等级枚举,", example = "read|edit|delete|add|read_write|advanced_read|advanced_edit|advanced_delete|advanced_add|advanced_read_write")
    private String permissionLevel;

    @TableField("permission_type")
    @ApiModelProperty(value = "自定义的权限分类", example = "navigation")
    private String permissionType;

    @TableField("outer_object_type_id")
    @ApiModelProperty(value = "绑定的外部对象类型id", example = "1")
    private ID outerObjectTypeId;

    @TableField("version")
    @ApiModelProperty(value = "乐观锁", example = "998")
    private Integer version;
}
