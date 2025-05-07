package com.project.deokhugam.notification.client;

import com.project.deokhugam.dashboard.dto.CursorPageResponse;
import com.project.deokhugam.dashboard.dto.PopularReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DashboardRestClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${dashboard.server.url}")
    private String dashboardServerUrl;

    /**
     * 대시보드 서버에서 각 기간별 인기 리뷰 TOP 10을 조회
     * @param period 기간 (DAILY, WEEKLY, MONTHLY, ALL_TIME)
     * @return 인기 리뷰 목록 (최대 10개)
     */
    public List<PopularReviewDto> getTop10PopularReviews(String period) {
        // 절대 URL 생성
        String url = dashboardServerUrl + "/api/reviews/popular"
                + "?period=" + period
                + "&direction=ASC"
                + "&limit=10";

        log.info("[DashboardRestClient] 요청 URL: {}", url);

        CursorPageResponse<PopularReviewDto> response = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse -> {
                            log.warn("Dashboard 서버 응답 오류: {}", clientResponse.statusCode());
                            return Mono.error(new RuntimeException("Dashboard 서버 오류"));
                        }
                )
                .bodyToMono(new ParameterizedTypeReference<CursorPageResponse<PopularReviewDto>>() {})
                .block();

        if (response == null || response.getContent() == null) {
            log.warn("Dashboard 서버에서 인기 리뷰를 받아오지 못했습니다.");
            return List.of();
        }

        return response.getContent();
    }
}
