package com.wuxian.janus.core.critical;

import com.wuxian.janus.core.StrictUtils;
import lombok.Getter;

@Getter
@SuppressWarnings("all")
public enum AccessControlLevelEnum {

    /**
     * 完全
     */
    FULL(true, true, true, true, true,//v,exec,e,del,en
            //ctrl
            true, true, true, true, true),

    /**
     * 除了自己不execute，其他访问控制能力都具备
     */
    HYPO_FULL(true, false, true, true, true,//v,exec,e,del,en
            //ctrl
            true, true, true, true, true),

    /**
     * 除了删除其他都能
     */
    ZERO(true, true, true, false, true,//v,exec,e,del,en
            //ctrl
            true, true, true, false, true),

    /**
     * 不能删不能禁用
     */
    ONE(true, true, true, false, false,//v,exec,e,del,en
            //ctrl
            true, true, true, false, false),

    /**
     * 自己用，也决定别人能否用
     */
    TWO(true, true, false, false, false,//v,exec,e,del,en
            //ctrl
            true, true, false, false, false),

    /**
     * 自己不用，只决定别人能否用
     */
    TWO_POINT_FIVE(true, false, false, false, false,//v,exec,e,del,en
            //ctrl
            true, true, false, false, false),

    /**
     * 仅自己用
     */
    THREE(true, true, false, false, false,//v,exec,e,del,en
            //ctrl
            false, false, false, false, false),

    /**
     * 看看而已
     */
    FOUR(true, false, false, false, false,//v,exec,e,del,en
            //ctrl
            false, false, false, false, false),

    /**
     * 没有权限
     */
    NONE(false, false, false, false, false,//v,exec,e,del,en
            //ctrl
            false, false, false, false, false);

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

    AccessControlLevelEnum(boolean viewAccess,
                           boolean executeAccess,
                           boolean editAccess,
                           boolean deleteAccess,
                           boolean enableAccess,
                           boolean viewControl,
                           boolean executeControl,
                           boolean editControl,
                           boolean deleteControl,
                           boolean enableControl) {
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

    public boolean match(AccessControlLevel level) {
        if (level.getViewAccess() != null && !StrictUtils.equals(level.getViewAccess(), this.getViewAccess())) {
            return false;
        }
        if (level.getExecuteAccess() != null && !StrictUtils.equals(level.getExecuteAccess(), this.getExecuteAccess())) {
            return false;
        }
        if (level.getEditAccess() != null && !StrictUtils.equals(level.getEditAccess(), this.getEditAccess())) {
            return false;
        }
        if (level.getDeleteAccess() != null && !StrictUtils.equals(level.getDeleteAccess(), this.getDeleteAccess())) {
            return false;
        }
        if (level.getEnableAccess() != null && !StrictUtils.equals(level.getEnableAccess(), this.getEnableAccess())) {
            return false;
        }


        if (level.getViewControl() != null && !StrictUtils.equals(level.getViewControl(), this.getViewControl())) {
            return false;
        }
        if (level.getExecuteControl() != null && !StrictUtils.equals(level.getExecuteControl(), this.getExecuteControl())) {
            return false;
        }
        if (level.getEditControl() != null && !StrictUtils.equals(level.getEditControl(), this.getEditControl())) {
            return false;
        }
        if (level.getDeleteControl() != null && !StrictUtils.equals(level.getDeleteControl(), this.getDeleteControl())) {
            return false;
        }
        if (level.getEnableControl() != null && !StrictUtils.equals(level.getEnableControl(), this.getEnableControl())) {
            return false;
        }

        return true;
    }
}
