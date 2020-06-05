package com.wuxian.janus.core.calculate;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxian
 */

@Data
public class PermissionPackage {
    private Boolean hasAllPermission = false;
    private Boolean hasAllCustomPermission = false;
    private List<PermissionInfo> permissions = new ArrayList<>();
}
