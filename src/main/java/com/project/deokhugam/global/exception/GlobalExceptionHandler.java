package com.project.deokhugam.global.exception;

import com.project.deokhugam.book.exception.BookNotFoundException;
import lombok.Getter;
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

	@ExceptionHandler({ NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<CustomApiResponse<?>> handleNoPageFoundException(Exception ex) {
		log.error("GlobalExceptionHandler catch NoHandlerFoundException : {}", ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(CustomApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomApiResponse<?>> handleCustomException(CustomException ex) {
		log.error("CustomException : {}", ex.getMessage());
		return ResponseEntity
				.status(ex.getErrorCode().getStatus())
				.body(CustomApiResponse.fail(ex));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomApiResponse<?>> handleException(Exception ex) {
		log.error("Unknown Exception : {}", ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(CustomApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomApiResponse<?>> handleValidationError(MethodArgumentNotValidException ex) {
		log.error("Validation error occurred: {}", ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(CustomApiResponse.fail(new CustomException(ErrorCode.INVALID_INPUT_VALUE)));
	}
}
