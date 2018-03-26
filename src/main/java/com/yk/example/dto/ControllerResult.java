package com.yk.example.dto;

/**
 * Created by yk on 2018/3/26.
 */
public class ControllerResult<RESULTOBJECT> {

    /** 返回编码（0、正确返回， -1、内部错误，其它返回码再议） */
    private Integer ret_code;
    /** 编码返回描述（泛型返回对象） */
    private RESULTOBJECT ret_values;

    private String message;

    public Integer getRet_code() {
        return ret_code;
    }
    public ControllerResult<RESULTOBJECT> setRet_code(Integer ret_code) {
        this.ret_code = ret_code;
        return this;
    }
    public RESULTOBJECT getRet_values() {
        return ret_values;
    }
    public ControllerResult<RESULTOBJECT> setRet_values(RESULTOBJECT ret_values) {
        this.ret_values = ret_values;
        return this;
    }
    public String getMessage() {
        return message;
    }
    public ControllerResult<RESULTOBJECT> setMessage(String message) {
        this.message = message;
        return  this;
    }
}
