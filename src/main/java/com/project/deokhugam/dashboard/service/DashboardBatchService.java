package com.project.deokhugam.dashboard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.deokhugam.dashboard.entity.Dashboard;
import com.project.deokhugam.dashboard.repository.DashboardRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardBatchService {

	private final DashboardRepository dashboardRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void updateAllDashboardPeriods() {
		LocalDateTime now = LocalDateTime.now();

		updateRankingForPeriod("DAILY", now.minusDays(1), now);
		updateRankingForPeriod("WEEKLY", now.minusWeeks(1), now);
		updateRankingForPeriod("MONTHLY", now.minusMonths(1), now);
		updateRankingForPeriod("ALL_TIME", null, now);
	}

	private void updateRankingForPeriod(String period, LocalDateTime start, LocalDateTime end) {
		dashboardRepository.deleteByTypeAndPeriod("BOOK", period);
		dashboardRepository.deleteByTypeAndPeriod("REVIEW", period);
		dashboardRepository.deleteByTypeAndPeriod("USER", period);

		updateBookRanking(start, end, period);
		updateReviewRanking(start, end, period);
		updatePowerUserRanking(start, end, period);
	}

	private void updateBookRanking(LocalDateTime start, LocalDateTime end, String period) {
		List<Object[]> results = createNativeQueryWithOptionalDates(
			"SELECT r.book_id, COUNT(r.review_id), AVG(r.review_rating) " +
				"FROM review r " +
				(getWhereClause(start)) +
				" GROUP BY r.book_id",
			start, end
		);

		results.sort((a, b) -> {
			double scoreA = ((Number)a[1]).doubleValue() * 0.4 + ((Number)a[2]).doubleValue() * 0.6;
			double scoreB = ((Number)b[1]).doubleValue() * 0.4 + ((Number)b[2]).doubleValue() * 0.6;
			return Double.compare(scoreB, scoreA);
		});

		double prevScore = -1;
		int rank = 0, sameRankCount = 1;

		for (Object[] row : results) {
			UUID bookId = (UUID)row[0];
			long reviewCount = ((Number)row[1]).longValue();
			double rating = ((Number)row[2]).doubleValue();
			double score = reviewCount * 0.4 + rating * 0.6;

			if (score != prevScore) {
				rank += sameRankCount;
				sameRankCount = 1;
			} else {
				sameRankCount++;
			}
			prevScore = score;

			dashboardRepository.save(Dashboard.builder()
				.type("BOOK").period(period)
				.targetId(bookId).bookId(bookId)
				.score(score).rank((long)rank)
				.reviewCount(reviewCount).reviewRating(rating)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build());
		}
	}

	private void updateReviewRanking(LocalDateTime start, LocalDateTime end, String period) {
		List<Object[]> results = createNativeQueryWithOptionalDates(
			"SELECT r.review_id, r.book_id, r.user_id, r.like_count, r.comment_count, r.review_rating " +
				"FROM review r " +
				getWhereClause(start),
			start, end
		);

		results.sort((a, b) -> {
			double scoreA = ((Number)a[3]).doubleValue() * 0.3 + ((Number)a[4]).doubleValue() * 0.7;
			double scoreB = ((Number)b[3]).doubleValue() * 0.3 + ((Number)b[4]).doubleValue() * 0.7;
			return Double.compare(scoreB, scoreA);
		});

		double prevScore = -1;
		int rank = 0, sameRankCount = 1;

		for (Object[] row : results) {
			UUID reviewId = (UUID)row[0];
			UUID bookId = (UUID)row[1];
			UUID userId = (UUID)row[2];
			long likeCount = ((Number)row[3]).longValue();
			long commentCount = ((Number)row[4]).longValue();
			double rating = ((Number)row[5]).doubleValue();
			double score = likeCount * 0.3 + commentCount * 0.7;

			if (score != prevScore) {
				rank += sameRankCount;
				sameRankCount = 1;
			} else {
				sameRankCount++;
			}
			prevScore = score;

			dashboardRepository.save(Dashboard.builder()
				.type("REVIEW").period(period)
				.targetId(reviewId).reviewId(reviewId).bookId(bookId).userId(userId)
				.score(score).rank((long)rank)
				.likeCount(likeCount).commentCount(commentCount).reviewRating(rating)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build());
		}
	}

	private void updatePowerUserRanking(LocalDateTime start, LocalDateTime end, String period) {
		List<Object[]> results = createNativeQueryWithOptionalDates(
			"SELECT r.user_id, SUM(d.score), SUM(d.like_count), SUM(d.comment_count) " +
				"FROM dashboard d JOIN review r ON d.review_id = r.review_id " +
				"WHERE d.type = 'REVIEW' " +
				(start != null ? "AND d.created_at BETWEEN :start AND :end " : "") +
				"GROUP BY r.user_id",
			start, end
		);

		results.sort((a, b) -> {
			double scoreA = ((Number)a[1]).doubleValue() * 0.5 + ((Number)a[2]).doubleValue() * 0.2
				+ ((Number)a[3]).doubleValue() * 0.3;
			double scoreB = ((Number)b[1]).doubleValue() * 0.5 + ((Number)b[2]).doubleValue() * 0.2
				+ ((Number)b[3]).doubleValue() * 0.3;
			return Double.compare(scoreB, scoreA);
		});

		double prevScore = -1;
		int rank = 0, sameRankCount = 1;

		for (Object[] row : results) {
			UUID userId = (UUID)row[0];
			double reviewScoreSum = ((Number)row[1]).doubleValue();
			long likeCount = ((Number)row[2]).longValue();
			long commentCount = ((Number)row[3]).longValue();
			double score = reviewScoreSum * 0.5 + likeCount * 0.2 + commentCount * 0.3;

			if (score != prevScore) {
				rank += sameRankCount;
				sameRankCount = 1;
			} else {
				sameRankCount++;
			}
			prevScore = score;

			dashboardRepository.save(Dashboard.builder()
				.type("USER").period(period)
				.targetId(userId).userId(userId)
				.score(score).rank((long)rank)
				.reviewScoreSum(reviewScoreSum).likeCount(likeCount).commentCount(commentCount)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build());
		}
	}

	private List<Object[]> createNativeQueryWithOptionalDates(String baseQuery, LocalDateTime start,
		LocalDateTime end) {
		var query = entityManager.createNativeQuery(baseQuery);
		if (start != null && end != null) {
			query.setParameter("start", start);
			query.setParameter("end", end);
		}
		return query.getResultList();
	}

	private String getWhereClause(LocalDateTime start) {
		return start != null ? "WHERE r.created_at BETWEEN :start AND :end" : "";
	}
}
