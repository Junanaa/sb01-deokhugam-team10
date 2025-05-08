package com.project.deokhugam.review.dto;

import java.util.UUID;

public record ReviewSearchRequest(
        UUID userId,
        UUID bookId,
        String keyword,
        String orderBy,
        String direction,
        String cursor,
        String after,
        Integer limit,
        UUID requestUserId
) {
}
