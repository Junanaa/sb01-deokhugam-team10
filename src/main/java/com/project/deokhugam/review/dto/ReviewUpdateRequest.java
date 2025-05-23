package com.project.deokhugam.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ReviewUpdateRequest(

        @NotBlank(message = "리뷰 내용은 비어 있을 수 없습니다.")
        String content,

        @NotNull(message = "평점은 필수 입력입니다.")
        @Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다.")
        @Max(value = 5, message = "평점은 최대 5점 이하여야 합니다.")
        Integer rating

) {}
