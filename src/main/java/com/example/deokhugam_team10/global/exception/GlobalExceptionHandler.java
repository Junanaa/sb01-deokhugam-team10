package com.example.deokhugam_team10.global.exception;

import com.example.deokhugam_team10.global.response.CustomApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 존재하지 않는 요청에 대한 예외
    @ExceptionHandler(value = {NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class})
    public CustomApiResponse<?> handleNoPageFoundException(Exception e) {
        log.error("GlobalExceptionHandler catch NoHandlerFoundException : {}", e.getMessage());
        return CustomApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(value = {CustomException.class})
    public CustomApiResponse<?> handleCustomException(CustomException e) {
        log.error("handleCustomException() in GlobalExceptionHandler throw CustomException : {}",
                e.getMessage());
        return CustomApiResponse.fail(e);
    }

    @ExceptionHandler(value = {Exception.class})
    public CustomApiResponse<?> handleException(Exception e) {
        log.error("handleCustomException() in GlobalExceptionHandler throw Exception : {}",
                e.getMessage());
        return CustomApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
