package com.project.deokhugam.dashboard.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class CursorPageResponse<T> {

	private List<T> content;
	private String nextCursor;
	private LocalDateTime nextAfter;
	private int size;
	private long totalElements;
	private boolean hasNext;
}
