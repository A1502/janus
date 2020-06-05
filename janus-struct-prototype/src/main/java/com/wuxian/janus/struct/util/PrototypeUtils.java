package com.wuxian.janus.struct.util;

import com.wuxian.janus.struct.prototype.AccessPrototype;
import com.wuxian.janus.struct.prototype.ControlPrototype;
import com.wuxian.janus.struct.prototype.JanusPrototype;

import java.util.Date;

public class PrototypeUtils {

    public static <ID, UID> void fill(JanusPrototype<ID, UID> struct, UID createdBy, Date createdDate
            , UID lastModifiedBy, Date lastModifiedDate) {
        struct.setCreationProposer(createdBy);
        struct.setModificationProposer(lastModifiedBy);
        struct.setCreatedBy(createdBy);
        struct.setCreatedDate(createdDate);
        struct.setLastModifiedBy(lastModifiedBy);
        struct.setLastModifiedDate(lastModifiedDate);
    }

    public static <ID, UID> void fillAccess(AccessPrototype<ID, UID> struct, Boolean viewAccess
            , Boolean executeAccess, Boolean editAccess, Boolean deleteAccess, Boolean enableAccess) {
        struct.setViewAccess(viewAccess);
        struct.setExecuteAccess(executeAccess);
        struct.setEditAccess(editAccess);
        struct.setDeleteAccess(deleteAccess);
        struct.setEnableAccess(enableAccess);
    }

    public static <ID, UID> void fillControl(ControlPrototype<ID, UID> struct, Boolean viewControl
            , Boolean executeControl, Boolean editControl, Boolean deleteControl, Boolean enableControl) {
        struct.setViewControl(viewControl);
        struct.setExecuteControl(executeControl);
        struct.setEditControl(editControl);
        struct.setDeleteControl(deleteControl);
        struct.setEnableControl(enableControl);
    }
}
