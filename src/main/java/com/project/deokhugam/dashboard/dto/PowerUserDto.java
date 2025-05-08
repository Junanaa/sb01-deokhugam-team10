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
public class PowerUserDto {

	private String userId;
	private String nickname;
	private String period;
	private LocalDateTime createdAt;
	private Long rank;
	private Double score;
	private Double reviewScoreSum;
	private Long likeCount;
	private Long commentCount;
}
