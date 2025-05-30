package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();

    @ExceptionHandler(Exception.class)
    @ResponseBody // Return JSON string
    public Result error(Exception e) {
        log.error("Error message：", e);
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody // Return JSON string
    public Result error(CustomException e) {
//        log.error("Error message：", e);
        return Result.error(e.getCode(), e.getMsg());
    }
}
