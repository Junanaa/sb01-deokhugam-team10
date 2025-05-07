package com.project.deokhugam.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

import java.util.UUID;

public record ReviewCreateRequest(

        @NotNull UUID bookId,
        @NotNull UUID userId,
        @NotBlank String content,
        @NotNull @Min(1) @Max(5) Integer rating
) {
}
