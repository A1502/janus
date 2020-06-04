package com.wuxian.janus.entity.prototype.first;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuxian.janus.entity.prototype.JanusPrototype;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@TableName("scope_role_user_x")
public class ScopeRoleUserXPrototype<ID, UID> extends JanusPrototype<ID, UID> {

    private static final long serialVersionUID = 1L;

    public void fill(ID id, String scope, ID roleId, UID userId
            , UID createdBy, Date createdDate, UID lastModifiedBy, Date lastModifiedDate) {
        setId(id);
        setScope(scope);
        setRoleId(roleId);
        setUserId(userId);

        setCreationProposer(createdBy);
        setModificationProposer(lastModifiedBy);
        setCreatedBy(createdBy);
        setCreatedDate(createdDate);
        setLastModifiedBy(lastModifiedBy);
        setLastModifiedDate(lastModifiedDate);
    }

    /**
     * 用户可以在不同的scope下加入角色和用户组。达到在不同scope下的权限不同效果。
     */
    @TableField("scope")
    @ApiModelProperty(value = "范围", example = "null")
    private String scope;

    @TableField("role_id")
    @ApiModelProperty(value = "角色id", example = "1")
    private ID roleId;

    @TableField("user_id")
    @ApiModelProperty(value = "用户id", example = "1")
    private UID userId;

    //无需version

}
