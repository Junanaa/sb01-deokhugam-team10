package com.project.deokhugam.dashboard.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.deokhugam.dashboard.dto.CursorPageResponse;
import com.project.deokhugam.dashboard.dto.DashboardPeriod;
import com.project.deokhugam.dashboard.dto.PopularBookDto;
import com.project.deokhugam.dashboard.dto.PopularReviewDto;
import com.project.deokhugam.dashboard.dto.PowerUserDto;
import com.project.deokhugam.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RankingController {

	private final DashboardService dashboardService;

	// 리뷰 랭킹
	@GetMapping("/reviews/popular")
	public CursorPageResponse<PopularReviewDto> getPopularReviews(
		@RequestParam DashboardPeriod period,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after,
		@RequestParam(defaultValue = "ASC") String direction,
		@PageableDefault(size = 10, sort = "rank") Pageable pageable
	) {
		return dashboardService.getPopularReviews(period.name(), cursor, after, direction, pageable);
	}

	// 책 랭킹
	@GetMapping("/books/popular")
	public CursorPageResponse<PopularBookDto> getPopularBooks(
		@RequestParam DashboardPeriod period,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after,
		@RequestParam(defaultValue = "ASC") String direction,
		@PageableDefault(size = 10, sort = "rank") Pageable pageable
	) {
		return dashboardService.getPopularBooks(period.name(), cursor, after, direction, pageable);
	}

	// 유저 랭킹
	@GetMapping("/users/power")
	public CursorPageResponse<PowerUserDto> getPowerUsers(
		@RequestParam DashboardPeriod period,
		@RequestParam(required = false) Double cursor,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after,
		@RequestParam(defaultValue = "ASC") String direction,
		@PageableDefault(size = 10, sort = "rank") Pageable pageable
	) {
		return dashboardService.getPowerUsers(period.name(), cursor, after, direction, pageable);
	}
}
