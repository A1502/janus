package com.wuxian.janus.core;

import lombok.Data;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;

import java.util.List;

@Data
public class PermissionInfo {
    private PermissionTemplateStruct template;
    private List<PermissionDetail> permissionDetails;
}
