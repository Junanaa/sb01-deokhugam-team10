package com.project.deokhugam.dashboard.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dashboard")
public class Dashboard {

	@Id
	@GeneratedValue
	@Column(columnDefinition = "UUID")
	private UUID id;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private String period;

	@Column(nullable = false)
	private String targetId;

	@Column(name = "book_id", insertable = false, updatable = false)
	private UUID bookId;

	@Column(name = "user_id", insertable = false, updatable = false)
	private UUID userId;

	@Column(name = "review_id", insertable = false, updatable = false)
	private UUID reviewId;

	@Column(nullable = false)
	private Double score;

	@Column(nullable = false)
	private Long rank;

	@Column(nullable = false)
	private Long likeCount;

	@Column(nullable = false)
	private Long commentCount;

	@Column(nullable = false)
	private Long reviewCount;

	@Column(nullable = false)
	private Double reviewRating;

	@Column(nullable = false)
	private Double reviewScoreSum;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", referencedColumnName = "book_id", insertable = false, updatable = false)
	private Book book;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id", referencedColumnName = "review_id", insertable = false, updatable = false)
	private Review review;
}
