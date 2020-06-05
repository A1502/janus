package com.wuxian.janus.core.calculate;

import lombok.Getter;
import lombok.Setter;
import com.wuxian.janus.struct.layer1.RoleStruct;

import java.util.ArrayList;
import java.util.List;

public class ExecuteAccessRolePackage {

    @Getter
    @Setter
    private Boolean executeAccessOfTenantCustomRoles = false;

    @Getter
    private List<RoleStruct> roles = new ArrayList<>();
}