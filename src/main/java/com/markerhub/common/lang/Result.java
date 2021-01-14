package com.markerhub.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private int code;
    //设计异步返回结果 用于判断前端是否操作成功 200表示正常 其他为异常
    private String msg;
    private Object data;

    public static Result success(Object data){
        return success(200,"操作成功",data);
    }
    public static Result success(int code,String msg,Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }


    public static Result fail(String msg) {
        return fail(404,msg,null);
    }
    public static Result fail(String msg,Object data){
        return fail(404,msg,data);
    }
    public static Result fail(int code,String msg,Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
