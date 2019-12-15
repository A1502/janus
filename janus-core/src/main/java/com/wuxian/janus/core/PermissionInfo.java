package com.wuxian.janus.core;

import lombok.Data;
import com.wuxian.janus.entity.PermissionTemplateEntity;

import java.util.List;

@Data
public class PermissionInfo {
    private PermissionTemplateEntity template;
    private List<PermissionDetail> permissionDetails;
}
