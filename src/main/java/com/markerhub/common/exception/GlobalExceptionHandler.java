package com.markerhub.common.exception;

import com.markerhub.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常捕获
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED) //给前端的状态码
    @ExceptionHandler(value=ShiroException.class)
    public Result handler(ShiroException e){
        log.error("Shiro异常：-----------{}",e);
        return Result.fail(401,e.getMessage(),null);   //shiro异常一般为没有权限操作
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) //给前端的状态码
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e){
        log.error("实体校验异常：-----------{}",e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        return Result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) //给前端的状态码
    @ExceptionHandler(value=IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常：-----------{}",e);
        return Result.fail(e.getMessage());  //登陆时返回用户是否存在
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) //给前端的状态码
    @ExceptionHandler(value=RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("运行时异常：-----------{}",e);
        return Result.fail(e.getMessage());
    }
}
