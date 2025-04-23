package com.project.deokhugam.global.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.deokhugam.global.exception.CustomException;

import jakarta.annotation.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomApiResponse<T>(
	@JsonIgnore
	HttpStatus httpStatus,
	boolean success,
	@Nullable T data,
	@Nullable ErrorResponse error
) {
	public static <T> CustomApiResponse<T> ok(@Nullable final T data) {
		return new CustomApiResponse<>(HttpStatus.OK, true, data, null);
	}

	public static <T> CustomApiResponse<T> created(@Nullable final T data) {
		return new CustomApiResponse<>(HttpStatus.CREATED, true, data, null);
	}

	public static <T> CustomApiResponse<T> fail(final CustomException e) {
		return new CustomApiResponse<>(e.getErrorCode().getStatus(), false, null,
			ErrorResponse.of(e.getErrorCode()));
	}
}
