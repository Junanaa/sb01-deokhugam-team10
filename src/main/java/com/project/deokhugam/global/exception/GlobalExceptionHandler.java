package com.project.deokhugam.global.exception;

import com.project.deokhugam.book.exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.project.deokhugam.global.response.CustomApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// 존재하지 않는 요청에 대한 예외
	@ExceptionHandler(value = {NoHandlerFoundException.class,
		HttpRequestMethodNotSupportedException.class})
	public CustomApiResponse<?> handleNoPageFoundException(Exception ex) {
		log.error("GlobalExceptionHandler catch NoHandlerFoundException : {}", ex.getMessage());
		return CustomApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(value = {CustomException.class})
	public CustomApiResponse<?> handleCustomException(CustomException ex) {
		log.error("handleCustomException() in GlobalExceptionHandler throw CustomException : {}",
			ex.getMessage());
		return CustomApiResponse.fail(ex);
	}

	@ExceptionHandler(value = {Exception.class})
	public CustomApiResponse<?> handleException(Exception ex) {
		log.error("handleCustomException() in GlobalExceptionHandler throw Exception : {}",
			ex.getMessage());
		return CustomApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public CustomApiResponse<?> handleValidationError(MethodArgumentNotValidException ex) {
		log.error("Validation error occurred in GlobalExceptionHandler: {}", ex.getMessage());

		return CustomApiResponse.fail(new CustomException(ErrorCode.INVALID_INPUT_VALUE));
	}
  
}
