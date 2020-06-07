package com.wuxian.janus.core.critical;


import lombok.Data;

@Data
public class AccessControlMode extends Access {

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

    public AccessControlMode(Boolean viewAccess,
                             Boolean executeAccess,
                             Boolean editAccess,
                             Boolean deleteAccess,
                             Boolean enableAccess,
                             Boolean viewControl,
                             Boolean executeControl,
                             Boolean editControl,
                             Boolean deleteControl,
                             Boolean enableControl) {
        this.viewAccess = viewAccess;
        this.executeAccess = executeAccess;
        this.editAccess = editAccess;
        this.deleteAccess = deleteAccess;
        this.enableAccess = enableAccess;
        this.viewControl = viewControl;
        this.executeControl = executeControl;
        this.editControl = editControl;
        this.deleteControl = deleteControl;
        this.enableControl = enableControl;
    }
}
