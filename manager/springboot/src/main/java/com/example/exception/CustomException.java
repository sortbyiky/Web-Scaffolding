package com.example.exception;

import com.example.common.enums.ResultCodeEnum;

public class CustomException extends RuntimeException {
    private String code; // 异常的错误码
    private String msg;  // 异常的错误信息

    // 使用枚举类 ResultCodeEnum 构造自定义异常
    public CustomException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.code; // 设置错误码
        this.msg = resultCodeEnum.msg;   // 设置错误信息
    }

    // 使用自定义的错误码和信息构造自定义异常
    public CustomException(String code, String msg) {
        this.code = code; // 设置错误码
        this.msg = msg;   // 设置错误信息
    }

    // 获取错误码
    public String getCode() {
        return code;
    }

    // 设置错误码
    public void setCode(String code) {
        this.code = code;
    }

    // 获取错误信息
    public String getMsg() {
        return msg;
    }

    // 设置错误信息
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
