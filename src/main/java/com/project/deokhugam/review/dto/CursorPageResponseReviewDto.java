package com.project.deokhugam.review.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CursorPageResponseReviewDto(
        List<ReviewDto> content,
        String nextCursor,
        LocalDateTime nextAfter,
        int size,
        long totalElements,
        boolean hasNext
) {

}
