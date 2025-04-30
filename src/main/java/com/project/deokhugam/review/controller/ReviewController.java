package com.project.deokhugam.review.controller;

import com.project.deokhugam.review.dto.*;
import com.project.deokhugam.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewCreateRequest request) {
        ReviewDto review = reviewService.createReview(request);
        return ResponseEntity
                .created(URI.create("/reviews/" + review.id()))
                .body(review);
    }

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<ReviewLikeDto> likeReview(
            @PathVariable UUID reviewId,
            @RequestHeader("Deokhugam-Request-User-ID") UUID requestUserId
    ) {
        ReviewLikeDto response = reviewService.likeReview(reviewId, requestUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> findReview(
            @PathVariable UUID reviewId,
            @RequestHeader("Deokhugam-Request-User-ID") UUID requestUserId
    ) {
        ReviewDto review = reviewService.findReview(reviewId, requestUserId);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable UUID reviewId,
            @RequestHeader("Deokhugam-Request-User-ID") UUID requestUserId,
            @RequestBody @Valid ReviewUpdateRequest request
    ) {
        ReviewDto review = reviewService.updateReview(reviewId, requestUserId, request);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID reviewId,
            @RequestHeader("Deokhugam-Request-User-ID") UUID requestUserId
    ) {
        reviewService.deleteReview(reviewId, requestUserId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reviewId}/hard")
    public ResponseEntity<Void> hardDeleteReview(
            @PathVariable UUID reviewId,
            @RequestHeader("Deokhugam-Request-User-ID") UUID requestUserId
    ) {
        reviewService.hardDeleteReview(reviewId, requestUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CursorPageResponseReviewDto> searchReviews(
            ReviewSearchRequest request,
            @RequestHeader("Deokhugam-Request-User-ID") UUID requestHeaderUserId
    ) {
        CursorPageResponseReviewDto response = reviewService.searchReviews(request, requestHeaderUserId);
        return ResponseEntity.ok(response);
    }

}
