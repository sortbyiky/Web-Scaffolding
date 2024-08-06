package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages="com.example.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get(); // 初始化日志对象，用于记录日志信息

    // 统一异常处理方法，处理所有未捕获的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody // 将返回值转换为 JSON 格式
    public Result error(HttpServletRequest request, Exception e){
        log.error("异常信息：", e); // 记录异常信息到日志
        return Result.error(); // 返回通用错误结果
    }

    // 自定义异常处理方法，专门处理 CustomException
    @ExceptionHandler(CustomException.class)
    @ResponseBody // 将返回值转换为 JSON 格式
    public Result customError(HttpServletRequest request, CustomException e){
        return Result.error(e.getCode(), e.getMsg()); // 返回包含错误码和信息的错误结果
    }
}

