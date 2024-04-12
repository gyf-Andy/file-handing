package com.gyf.result;

import java.io.Serializable;

public class ResultObject implements Serializable {

    private Integer code;
    private String msg;
    private Object data;

    public ResultObject() {
    }

    public ResultObject(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultObject(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误记录
     *
     * ResultObject 中如果缺少get set方法
     * 导致无法获取属性的值进而导致转换成json报错
     * 加上get set方法可以完美解决
     *
     */
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
