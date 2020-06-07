package com.wuxian.janus.cache.model.source;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessControl extends Access {

    private boolean  viewControl;
    private boolean  executeControl;
    private boolean  editControl;
    private boolean  deleteControl;
    private boolean  enableControl;

    public AccessControl() {
    }

    public AccessControl(boolean  viewAccess,
                         boolean  executeAccess,
                         boolean  editAccess,
                         boolean  deleteAccess,
                         boolean  enableAccess,
                         boolean  viewControl,
                         boolean  executeControl,
                         boolean  editControl,
                         boolean  deleteControl,
                         boolean  enableControl) {
        super(viewAccess, executeAccess, editAccess, deleteAccess, enableAccess);
        this.viewControl = viewControl;
        this.executeControl = executeControl;
        this.editControl = editControl;
        this.deleteControl = deleteControl;
        this.enableControl = enableControl;
    }
}
