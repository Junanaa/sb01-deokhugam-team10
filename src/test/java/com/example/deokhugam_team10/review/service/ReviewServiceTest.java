package com.example.deokhugam_team10.review.service;

import com.example.deokhugam_team10.review.dto.ReviewCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    void testCreateReview() {
        ReviewCreateRequest request = new ReviewCreateRequest(
                "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "리뷰 테스트입니다.",
                5
        );

        reviewService.createReview(request);
    }

}