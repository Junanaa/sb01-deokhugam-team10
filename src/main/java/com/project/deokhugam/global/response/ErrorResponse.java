package com.project.deokhugam.global.response;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.deokhugam.global.exception.ErrorCode;

import lombok.Builder;

@Builder
public record ErrorResponse(
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC") Instant timestamp,
	HttpStatus status, String message, String details
) {
	public static ErrorResponse of(ErrorCode errorCode) {
		return ErrorResponse.builder()
			.timestamp(Instant.now())
			.status(errorCode.getStatus())
			.message(errorCode.getMessage())
			.details(errorCode.getDetail())
			.build();
	}
}
