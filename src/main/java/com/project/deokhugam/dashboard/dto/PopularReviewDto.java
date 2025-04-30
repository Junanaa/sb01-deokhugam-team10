package com.project.deokhugam.dashboard.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularReviewDto {

	private String id;
	private String reviewId;
	private String bookId;
	private String bookTitle;
	private String bookThumbnailUrl;
	private String userId;
	private String userNickname;
	private String reviewContent;
	private Double reviewRating;
	private String period;
	private LocalDateTime createdAt;
	private Long rank;
	private Double score;
	private Long likeCount;
	private Long commentCount;
}
