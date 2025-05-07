package com.project.deokhugam.dashboard.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import com.project.deokhugam.dashboard.dto.CursorPageResponse;
import com.project.deokhugam.dashboard.dto.PopularBookDto;
import com.project.deokhugam.dashboard.dto.PopularReviewDto;
import com.project.deokhugam.dashboard.dto.PowerUserDto;

public interface DashboardService {
	CursorPageResponse<PopularReviewDto> getPopularReviews(String period, Long cursor, LocalDateTime after,
		String direction, Pageable pageable);

	CursorPageResponse<PopularBookDto> getPopularBooks(String period, Long cursor, LocalDateTime after,
		String direction, Pageable pageable);

	CursorPageResponse<PowerUserDto> getPowerUsers(
		String period, Double cursor, LocalDateTime after,
		String direction, Pageable pageable);

	void calculateDailyBookRanking(LocalDateTime start, LocalDateTime end);

	void calculateDailyReviewRanking(LocalDateTime start, LocalDateTime end);

	void calculateDailyPowerUserRanking(LocalDateTime start, LocalDateTime end);
}
