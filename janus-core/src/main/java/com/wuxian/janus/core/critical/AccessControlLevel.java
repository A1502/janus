package com.wuxian.janus.core.critical;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessControlLevel {

    private Boolean viewAccess;
    private Boolean executeAccess;
    private Boolean editAccess;
    private Boolean deleteAccess;
    private Boolean enableAccess;
    private Boolean viewControl;
    private Boolean executeControl;
    private Boolean editControl;
    private Boolean deleteControl;
    private Boolean enableControl;
}
