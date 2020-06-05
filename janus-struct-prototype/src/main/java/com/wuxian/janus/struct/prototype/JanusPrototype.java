package com.wuxian.janus.struct.prototype;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Setter
@Getter
public class JanusPrototype<ID, UID> {

    @ApiModelProperty(value = "编号", example = "1")
    private ID id;

    @ApiModelProperty(value = "创建人", example = "1")
    private UID createdBy;

    @ApiModelProperty(value = "创建时间", example = "2019-10-10")
    private Date createdDate;

    @ApiModelProperty(value = "最后修改人", example = "1")
    private UID lastModifiedBy;

    @ApiModelProperty(value = "最后修改时间", example = "2019-10-10")
    private Date lastModifiedDate;

    @ApiModelProperty(value = "创建发起人", example = "1")
    private UID creationProposer;

    @ApiModelProperty(value = "修改发起人", example = "1")
    private UID modificationProposer;
}