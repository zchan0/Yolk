package com.ustc.yolk.utils.charset;

/**
 * Created by Administrator on 2016/11/28.
 */
public enum CharsetEnum {
    UTF_8("UTF-8");

    private String code;

    CharsetEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
