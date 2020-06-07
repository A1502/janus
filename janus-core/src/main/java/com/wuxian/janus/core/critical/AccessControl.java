package com.wuxian.janus.core.critical;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessControl extends Access {

    protected boolean viewControl;
    protected boolean executeControl;
    protected boolean editControl;
    protected boolean deleteControl;
    protected boolean enableControl;

    public AccessControl() {
    }

    public AccessControl(boolean viewAccess,
                         boolean executeAccess,
                         boolean editAccess,
                         boolean deleteAccess,
                         boolean enableAccess,
                         boolean viewControl,
                         boolean executeControl,
                         boolean editControl,
                         boolean deleteControl,
                         boolean enableControl) {
        super(viewAccess, executeAccess, editAccess, deleteAccess, enableAccess);
        this.viewControl = viewControl;
        this.executeControl = executeControl;
        this.editControl = editControl;
        this.deleteControl = deleteControl;
        this.enableControl = enableControl;
    }

    @Override
    public String toString() {
        String empty = "";
        return super.toString()
                + (this.viewControl ? ("," + "viewControl") : empty)
                + (this.executeControl ? ("," + "executeControl") : empty)
                + (this.editControl ? ("," + "editControl") : empty)
                + (this.deleteControl ? ("," + "deleteControl") : empty)
                + (this.enableControl ? ("," + "enableControl") : empty);
    }
}
