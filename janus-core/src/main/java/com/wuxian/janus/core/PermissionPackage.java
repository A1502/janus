package com.wuxian.janus.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Solomon
 */

@Data
public class PermissionPackage {
    private Boolean hasAllPermission = false;
    private Boolean hasAllCustomPermission = false;
    private List<PermissionInfo> permissions = new ArrayList<>();
}
