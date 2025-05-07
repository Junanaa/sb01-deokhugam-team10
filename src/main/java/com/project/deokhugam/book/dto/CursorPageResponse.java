package com.project.deokhugam.book.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CursorPageResponse<T> {

  private List<T> content;
  private String nextCursor;
  private LocalDateTime nextAfter;
  private int size;
  private long totalElements;
  private boolean hasNext;

  public CursorPageResponse() {
  }

  public CursorPageResponse(List<T> content, String nextCursor, LocalDateTime nextAfter,
      int size, long totalElements, boolean hasNext) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.nextAfter = nextAfter;
    this.size = size;
    this.totalElements = totalElements;
    this.hasNext = hasNext;
  }
}
