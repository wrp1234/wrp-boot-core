package com.wrp.boot.core.exception;

import com.wrp.boot.core.request.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理
 * @author wrp
 * @since 2024年12月17日 14:04
 **/
@RestControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler ({MissingServletRequestParameterException.class})
    public Result<Void> handleMissingServletRequestParameterException() {
        return Result.error("有请求参数未传哦");
    }

    @ExceptionHandler ({MethodArgumentNotValidException.class})
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.error(e.getBindingResult().getAllErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";")));
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler ({HttpRequestMethodNotSupportedException.class})
    public Result<Void> handleHttpRequestMethodNotSupportedException() {
        return Result.error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler ({HttpMessageNotReadableException.class})
    public Result<Void> handleHttpMessageNotReadableException() {
        return Result.error("参数出错啦");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result<Void>> defaultErrorHandler(Exception e) {
        //自定义的异常
        if (e instanceof BusinessException ex) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Result.error(ex.getMessage()));
        } else {
            log.error("服务器内部错误:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Result.serverError("服务出错啦。"));
        }
    }
}
