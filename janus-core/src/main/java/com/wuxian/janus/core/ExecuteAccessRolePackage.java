package com.wuxian.janus.core;

import lombok.Getter;
import lombok.Setter;
import com.wuxian.janus.entity.RoleEntity;

import java.util.ArrayList;
import java.util.List;

public class ExecuteAccessRolePackage {

    @Getter
    @Setter
    private Boolean executeAccessOfTenantCustomRoles = false;

    @Getter
    private List<RoleEntity> roles = new ArrayList<>();
}