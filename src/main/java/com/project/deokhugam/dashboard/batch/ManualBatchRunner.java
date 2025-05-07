package com.project.deokhugam.dashboard.batch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.project.deokhugam.dashboard.service.DashboardBatchService;

import lombok.RequiredArgsConstructor;

// 00시에 자동 업데이트 설정으로 테스트 하려고 만든 Batch 클래스
@Component
@RequiredArgsConstructor
public class ManualBatchRunner implements CommandLineRunner {

	private final DashboardBatchService dashboardBatchService;

	@Override
	@Transactional
	public void run(String... args) {
		System.out.println("🔄 Manually triggering ALL dashboard period batch updates...");

		dashboardBatchService.updateAllDashboardPeriods(); // ✅ 전체 기간 실행

		System.out.println("✅ All period dashboard batch update completed.");
	}
}
