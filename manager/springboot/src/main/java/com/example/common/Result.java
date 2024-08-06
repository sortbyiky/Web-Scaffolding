package com.example.common;

import com.example.common.enums.ResultCodeEnum;

public class Result {
    private String code; // 返回码，用于表示操作结果
    private String msg;  // 返回信息，描述操作结果
    private Object data; // 返回数据，可以是任何类型的对象

    // 私有构造方法，初始化 Result 对象并设置数据
    private Result(Object data) {
        this.data = data;
    }

    // 无参构造方法，允许创建空的 Result 对象
    public Result() {
    }

    // 静态方法，返回表示成功的 Result 对象（无数据）
    public static Result success() {
        Result tResult = new Result(); // 创建 Result 对象
        tResult.setCode(ResultCodeEnum.SUCCESS.code); // 设置成功返回码
        tResult.setMsg(ResultCodeEnum.SUCCESS.msg);   // 设置成功信息
        return tResult;
    }

    // 静态方法，返回表示成功的 Result 对象（包含数据）
    public static Result success(Object data) {
        Result tResult = new Result(data); // 创建 Result 对象并传入数据
        tResult.setCode(ResultCodeEnum.SUCCESS.code); // 设置成功返回码
        tResult.setMsg(ResultCodeEnum.SUCCESS.msg);   // 设置成功信息
        return tResult;
    }

    // 静态方法，返回表示系统错误的 Result 对象
    public static Result error() {
        Result tResult = new Result(); // 创建 Result 对象
        tResult.setCode(ResultCodeEnum.SYSTEM_ERROR.code); // 设置系统错误码
        tResult.setMsg(ResultCodeEnum.SYSTEM_ERROR.msg);   // 设置系统错误信息
        return tResult;
    }

    // 静态方法，返回自定义错误信息的 Result 对象
    public static Result error(String code, String msg) {
        Result tResult = new Result(); // 创建 Result 对象
        tResult.setCode(code); // 设置自定义错误码
        tResult.setMsg(msg);   // 设置自定义错误信息
        return tResult;
    }

    // 静态方法，返回指定错误枚举类型的 Result 对象
    public static Result error(ResultCodeEnum resultCodeEnum) {
        Result tResult = new Result(); // 创建 Result 对象
        tResult.setCode(resultCodeEnum.code); // 设置错误码
        tResult.setMsg(resultCodeEnum.msg);   // 设置错误信息
        return tResult;
    }

    // 获取返回码
    public String getCode() {
        return code;
    }

    // 设置返回码
    public void setCode(String code) {
        this.code = code;
    }

    // 获取返回信息
    public String getMsg() {
        return msg;
    }

    // 设置返回信息
    public void setMsg(String msg) {
        this.msg = msg;
    }

    // 获取返回数据
    public Object getData() {
        return data;
    }

    // 设置返回数据
    public void setData(Object data) {
        this.data = data;
    }
}
