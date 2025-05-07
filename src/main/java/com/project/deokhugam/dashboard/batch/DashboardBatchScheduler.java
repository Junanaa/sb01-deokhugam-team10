package com.project.deokhugam.dashboard.batch;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.project.deokhugam.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DashboardBatchScheduler {

	private final DashboardService dashboardService;

	// 매일 자정 (00:00)에 실행
	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void runDailyBatch() {
		LocalDateTime start = LocalDateTime.now().minusDays(1);
		LocalDateTime end = LocalDateTime.now();

		dashboardService.calculateDailyBookRanking(start, end);
		dashboardService.calculateDailyReviewRanking(start, end);
		dashboardService.calculateDailyPowerUserRanking(start, end);
	}
}
