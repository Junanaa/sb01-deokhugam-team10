package com.project.deokhugam.notification.client;

import com.project.deokhugam.notification.dto.response.ReviewDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewRestClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${review.server.url}")
    private String reviewServerUrl;


    public ReviewDetailResponse getReviewDetail(UUID reviewId, UUID requesterId) {
        log.info("[ReviewRestClient] 요청 URL: {}/api/reviews/{}", reviewServerUrl, reviewId);

        return webClientBuilder
                .baseUrl(reviewServerUrl)
                .build()
                .get()
                .uri("/api/reviews/{reviewId}", reviewId)
                .header("Deokhugam-Request-User-ID", requesterId.toString())
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse -> {
                            log.warn("Review 서버 응답 오류: {}", clientResponse.statusCode());
                            return Mono.error(new RuntimeException("Review 서버 오류"));
                        }
                )
                .bodyToMono(ReviewDetailResponse.class)
                .block();
    }
}
