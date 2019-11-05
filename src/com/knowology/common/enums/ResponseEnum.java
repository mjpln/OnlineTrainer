package com.knowology.common.enums;

/**
 * @Author: sundj
 * @Date: 2019-10-30
 */
public enum ResponseEnum {
    SUCCESS(true,"生成成功"),
    ERROR_FAIL(false,"生成失败");

    private boolean code;
    private String msg;

    ResponseEnum(boolean code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean getCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
