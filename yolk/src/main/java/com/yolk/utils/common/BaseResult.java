package com.yolk.utils.common;

import com.yolk.model.ToString;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class BaseResult extends ToString {

    /**  */
    private static final long serialVersionUID = -7120466585897654805L;

    /** 是否成功 */
    private boolean           success          = false;

    /** 错误信息 */
    private String            errorMsg         = "";

    public BaseResult() {
    }

    public BaseResult(boolean success, String errorCode, String errorMsg) {
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
