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
public class PopularBookDto {

	private String id;
	private String bookId;
	private String title;
	private String author;
	private String thumbnailUrl;
	private String period;
	private Long rank;
	private Double score;
	private Long reviewCount;
	private Double rating;
	private LocalDateTime createdAt;
}
