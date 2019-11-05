package com.knowology.common.util;

import com.knowology.common.enums.ResponseEnum;

import java.io.Serializable;

/**
 * @Author: sundj
 * @Date: 2019-10-30
 */
public class ResultData implements Serializable {


    private static final long serialVersionUID = 2356926212950280044L;
    /**
     * 返回码  0-成功 1-失败
     */
    private boolean code;
    /**
     * 描述信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object obj;

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

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
    /**
     * 通用成功
     *
     * @return
     */
    public static ResultData ok() {
        ResultData data = new ResultData();
        data.setCode(ResponseEnum.SUCCESS.getCode());
        data.setMsg(ResponseEnum.SUCCESS.getMsg());
        return data;
    }

    /**
     * 通用失败
     *
     * @return
     */
    public static ResultData fail() {
        ResultData data = new ResultData();
        data.setCode(ResponseEnum.ERROR_FAIL.getCode());
        data.setMsg(ResponseEnum.ERROR_FAIL.getMsg());
        return data;
    }
}
