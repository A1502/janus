package com.wuxian.janus.entity.prototype;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.base.JanusPrototype;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 外部对象Prototype类
 *
 * @author Solomon
 * @email
 * @date 2019/07/11
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("outer_object")
public class OuterObjectPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id
            , ID outerObjectTypeId, String referenceId, String referenceCode, String referenceName
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate) {

        setId(id);
        setOuterObjectTypeId(outerObjectTypeId);
        setReferenceId(referenceId);
        setReferenceCode(referenceCode);
        setReferenceName(referenceName);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
    }

    @TableField("outer_object_type_id")
    @ApiModelProperty(value = "外部对象类型id", example = "1")
    private ID outerObjectTypeId;

    @TableField("reference_id")
    @ApiModelProperty(value = "外部引用id", example = "CN88800")
    private String referenceId;

    @TableField("reference_code")
    @ApiModelProperty(value = "外部引用code", example = "China_Shanghai")
    private String referenceCode;

    @TableField("reference_name")
    @ApiModelProperty(value = "外部引用名称", example = "中国上海市")
    private String referenceName;

    //无需version
}
