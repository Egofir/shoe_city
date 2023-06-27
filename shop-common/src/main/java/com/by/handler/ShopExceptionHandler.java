package com.by.handler;

import com.by.entity.ResultEntity;
import com.by.entity.ShopException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice // 表示全局异常
public class ShopExceptionHandler {

    // 处理系统异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultEntity<?> systemException() {
        return ResultEntity.error("系统维护中");
    }

    // 处理业务异常
    @ExceptionHandler(ShopException.class)
    @ResponseBody
    public ResultEntity<?> businessException(ShopException e) {
        return ResultEntity.error(e.getMsg());
    }

}
