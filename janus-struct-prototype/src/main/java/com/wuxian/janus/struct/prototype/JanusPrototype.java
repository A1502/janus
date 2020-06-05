package com.wuxian.janus.struct.prototype;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Setter
@Getter
public class JanusPrototype<ID, UID> {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "编号", example = "1")
    private ID id;

    @ApiModelProperty(value = "创建人", example = "1")
    private UID createdBy;

    @ApiModelProperty(value = "创建时间", example = "2019-10-10")
    private Date createdDate;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    @ApiModelProperty(value = "最后修改人", example = "1")
    private UID lastModifiedBy;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    @ApiModelProperty(value = "最后修改时间", example = "2019-10-10")
    private Date lastModifiedDate;

    @TableField("creation_proposer")
    @ApiModelProperty(value = "创建发起人", example = "1")
    private UID creationProposer;

    @TableField("modification_proposer")
    @ApiModelProperty(value = "修改发起人", example = "1")
    private UID modificationProposer;
}