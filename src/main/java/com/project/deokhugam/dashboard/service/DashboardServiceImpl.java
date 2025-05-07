package com.project.deokhugam.dashboard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.deokhugam.dashboard.dto.CursorPageResponse;
import com.project.deokhugam.dashboard.dto.PopularBookDto;
import com.project.deokhugam.dashboard.dto.PopularReviewDto;
import com.project.deokhugam.dashboard.dto.PowerUserDto;
import com.project.deokhugam.dashboard.entity.Dashboard;
import com.project.deokhugam.dashboard.repository.DashboardRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

	private final DashboardRepository dashboardRepository;

	@PersistenceContext
	private EntityManager em;

	@Override
	public CursorPageResponse<PopularReviewDto> getPopularReviews(
		String period, Long cursor, LocalDateTime after,
		String direction, Pageable pageable) {
		return getCursorPageResponse("REVIEW", period, cursor, after, direction, pageable, this::toPopularReviewDto);
	}

	@Override
	public CursorPageResponse<PopularBookDto> getPopularBooks(
		String period, Long cursor, LocalDateTime after,
		String direction, Pageable pageable) {
		return getCursorPageResponse("BOOK", period, cursor, after, direction, pageable, this::toPopularBookDto);
	}

	@Override
	public CursorPageResponse<PowerUserDto> getPowerUsers(
		String period, Double cursor, LocalDateTime after,
		String direction, Pageable pageable) {
		return getCursorPageResponse("USER", period, cursor, after, direction, pageable, this::toPowerUserDto);
	}

	private <T, C extends Comparable<C>> CursorPageResponse<T> getCursorPageResponse(
		String type, String period, C cursor, LocalDateTime after,
		String direction, Pageable pageable, Function<Dashboard, T> mapper) {

		Pageable sorted = PageRequest.of(0, pageable.getPageSize(), getSort(type, direction));
		List<Dashboard> result;

		if (cursor != null && after == null) {
			return CursorPageResponse.<T>builder()
				.content(List.of())
				.nextCursor(null)
				.nextAfter(null)
				.size(pageable.getPageSize())
				.totalElements(0)
				.hasNext(false)
				.build();
		}

		if (cursor == null || after == null) {
			result = dashboardRepository.findRankingFirstPage(type, period, sorted);
		} else {
			if ("USER".equals(type)) {
				result = dashboardRepository.findRankingWithCursor(type, period, direction, (Double)cursor, after,
					sorted);
			} else {
				result = dashboardRepository.findRankingWithCursor(type, period, direction, (Long)cursor, after,
					sorted);
			}
		}

		List<T> content = result.stream().map(mapper).toList();
		boolean hasNext = result.size() == pageable.getPageSize();
		String nextCursor = hasNext
			? ("USER".equals(type)
			? String.valueOf(result.get(result.size() - 1).getScore())
			: String.valueOf(result.get(result.size() - 1).getRank()))
			: null;

		return CursorPageResponse.<T>builder()
			.content(content)
			.nextCursor(nextCursor)
			.nextAfter(hasNext ? result.get(result.size() - 1).getCreatedAt() : null)
			.size(pageable.getPageSize())
			.totalElements(dashboardRepository.countByTypeAndPeriod(type, period))
			.hasNext(hasNext)
			.build();
	}

	private Sort getSort(String type, String direction) {
		if ("USER".equals(type)) {
			return direction.equalsIgnoreCase("ASC")
				? Sort.by(Sort.Order.asc("score"), Sort.Order.asc("createdAt"))
				: Sort.by(Sort.Order.desc("score"), Sort.Order.desc("createdAt"));
		}
		return direction.equalsIgnoreCase("ASC")
			? Sort.by(Sort.Order.asc("rank"), Sort.Order.asc("createdAt"))
			: Sort.by(Sort.Order.desc("rank"), Sort.Order.desc("createdAt"));
	}

	@Override
	@Transactional
	public void calculateDailyBookRanking(LocalDateTime start, LocalDateTime end) {
		dashboardRepository.deleteByTypeAndPeriod("BOOK", "DAILY");

		List<Object[]> results = em.createNativeQuery(
				"SELECT r.book_id, COUNT(r.review_id), AVG(r.review_rating), MIN(r.created_at) " +
					"FROM review r WHERE r.created_at BETWEEN :start AND :end GROUP BY r.book_id")
			.setParameter("start", start)
			.setParameter("end", end)
			.getResultList();

		results.sort((a, b) -> {
			double scoreA = ((Number)a[1]).doubleValue() * 0.4 + ((Number)a[2]).doubleValue() * 0.6;
			double scoreB = ((Number)b[1]).doubleValue() * 0.4 + ((Number)b[2]).doubleValue() * 0.6;
			int scoreCompare = Double.compare(scoreB, scoreA);
			if (scoreCompare != 0)
				return scoreCompare;
			return ((LocalDateTime)a[3]).compareTo((LocalDateTime)b[3]);
		});

		int rank = 1;
		for (Object[] row : results) {
			UUID bookId = (UUID)row[0];
			long reviewCount = ((Number)row[1]).longValue();
			double avgRating = ((Number)row[2]).doubleValue();
			double score = reviewCount * 0.4 + avgRating * 0.6;

			dashboardRepository.save(Dashboard.builder()
				.type("BOOK").period("DAILY")
				.targetId(bookId).bookId(bookId)
				.score(score).rank((long)rank++)
				.reviewCount(reviewCount).reviewRating(avgRating)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build());
		}
	}

	@Override
	@Transactional
	public void calculateDailyReviewRanking(LocalDateTime start, LocalDateTime end) {
		dashboardRepository.deleteByTypeAndPeriod("REVIEW", "DAILY");

		List<Object[]> results = em.createNativeQuery(
				"SELECT r.review_id, r.book_id, r.user_id, r.like_count, r.comment_count, r.review_rating, r.created_at " +
					"FROM review r WHERE r.created_at BETWEEN :start AND :end")
			.setParameter("start", start)
			.setParameter("end", end)
			.getResultList();

		results.sort((a, b) -> {
			double scoreA = ((Number)a[3]).doubleValue() * 0.3 + ((Number)a[4]).doubleValue() * 0.7;
			double scoreB = ((Number)b[3]).doubleValue() * 0.3 + ((Number)b[4]).doubleValue() * 0.7;
			int scoreCompare = Double.compare(scoreB, scoreA);
			if (scoreCompare != 0)
				return scoreCompare;
			return ((LocalDateTime)a[6]).compareTo((LocalDateTime)b[6]);
		});

		int rank = 1;
		for (Object[] row : results) {
			UUID reviewId = (UUID)row[0];
			UUID bookId = (UUID)row[1];
			UUID userId = (UUID)row[2];
			long likeCount = row[3] != null ? ((Number)row[3]).longValue() : 0L;
			long commentCount = row[4] != null ? ((Number)row[4]).longValue() : 0L;
			double rating = row[5] != null ? ((Number)row[5]).doubleValue() : 0.0;
			double score = likeCount * 0.3 + commentCount * 0.7;

			dashboardRepository.save(Dashboard.builder()
				.type("REVIEW").period("DAILY")
				.targetId(reviewId).reviewId(reviewId).bookId(bookId).userId(userId)
				.score(score).rank((long)rank++)
				.likeCount(likeCount).commentCount(commentCount).reviewRating(rating)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build());
		}
	}

	@Override
	@Transactional
	public void calculateDailyPowerUserRanking(LocalDateTime start, LocalDateTime end) {
		dashboardRepository.deleteByTypeAndPeriod("USER", "DAILY");

		List<Object[]> results = em.createNativeQuery(
				"SELECT r.user_id, SUM(d.score), SUM(d.like_count), SUM(d.comment_count), MIN(d.created_at) " +
					"FROM dashboard d JOIN review r ON d.review_id = r.review_id " +
					"WHERE d.type = 'REVIEW' AND d.created_at BETWEEN :start AND :end " +
					"GROUP BY r.user_id")
			.setParameter("start", start)
			.setParameter("end", end)
			.getResultList();

		results.sort((a, b) -> {
			double scoreA = ((Number)a[1]).doubleValue() * 0.5 + ((Number)a[2]).doubleValue() * 0.2
				+ ((Number)a[3]).doubleValue() * 0.3;
			double scoreB = ((Number)b[1]).doubleValue() * 0.5 + ((Number)b[2]).doubleValue() * 0.2
				+ ((Number)b[3]).doubleValue() * 0.3;
			int scoreCompare = Double.compare(scoreB, scoreA);
			if (scoreCompare != 0)
				return scoreCompare;
			return ((LocalDateTime)a[4]).compareTo((LocalDateTime)b[4]);
		});

		int rank = 1;
		for (Object[] row : results) {
			UUID userId = (UUID)row[0];
			double reviewScoreSum = ((Number)row[1]).doubleValue();
			long likeCount = ((Number)row[2]).longValue();
			long commentCount = ((Number)row[3]).longValue();
			double score = reviewScoreSum * 0.5 + likeCount * 0.2 + commentCount * 0.3;

			dashboardRepository.save(Dashboard.builder()
				.type("USER").period("DAILY")
				.targetId(userId).userId(userId)
				.score(score).rank((long)rank++)
				.reviewScoreSum(reviewScoreSum).likeCount(likeCount).commentCount(commentCount)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build());
		}
	}

	private PopularReviewDto toPopularReviewDto(Dashboard d) {
		return PopularReviewDto.builder()
			.id(toStringSafe(d.getId()))
			.reviewId(toStringSafe(d.getReviewId()))
			.bookId(toStringSafe(d.getBookId()))
			.userId(toStringSafe(d.getUserId()))
			.period(d.getPeriod())
			.createdAt(d.getCreatedAt())
			.rank(d.getRank())
			.score(d.getScore())
			.reviewRating(d.getReviewRating())
			.likeCount(d.getLikeCount())
			.commentCount(d.getCommentCount())
			.bookTitle(d.getBook() != null ? d.getBook().getTitle() : null)
			.bookThumbnailUrl(d.getBook() != null ? d.getBook().getThumbnailUrl() : null)
			.userNickname(d.getUser() != null ? d.getUser().getNickname() : null)
			.reviewContent(d.getReview() != null ? d.getReview().getReviewContent() : null)
			.build();
	}

	private PopularBookDto toPopularBookDto(Dashboard d) {
		return PopularBookDto.builder()
			.id(toStringSafe(d.getId()))
			.bookId(toStringSafe(d.getBookId()))
			.title(d.getBook() != null ? d.getBook().getTitle() : null)
			.author(d.getBook() != null ? d.getBook().getAuthor() : null)
			.thumbnailUrl(d.getBook() != null ? d.getBook().getThumbnailUrl() : null)
			.period(d.getPeriod())
			.createdAt(d.getCreatedAt())
			.rank(d.getRank())
			.score(d.getScore())
			.reviewCount(d.getReviewCount())
			.rating(d.getReviewRating())
			.build();
	}

	private PowerUserDto toPowerUserDto(Dashboard d) {
		return PowerUserDto.builder()
			.userId(toStringSafe(d.getUserId()))
			.nickname(d.getUser() != null ? d.getUser().getNickname() : null)
			.period(d.getPeriod())
			.createdAt(d.getCreatedAt())
			.rank(d.getRank())
			.score(d.getScore())
			.reviewScoreSum(d.getReviewScoreSum())
			.likeCount(d.getLikeCount())
			.commentCount(d.getCommentCount())
			.build();
	}

	private String toStringSafe(UUID id) {
		return id != null ? id.toString() : null;
	}
}
