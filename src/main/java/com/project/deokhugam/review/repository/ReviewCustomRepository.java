package com.project.deokhugam.review.repository;

import com.project.deokhugam.review.entity.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewCustomRepository {
    List<Review> searchReviews(
            UUID userId,
            UUID bookId,
            String keyword,
            String orderBy,
            String direction,
            String cursor,
            String after,
            int limit
    );
}
