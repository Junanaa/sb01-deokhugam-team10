package com.project.deokhugam.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequest {

  private String title;
  private String author;
  private String description;
  private String publisher;
  private LocalDate publishedDate;
  private String isbn;
  private String thumbnailUrl;
  private Long reviewCount;
  private Long rating;
  private LocalDateTime updatedAt; // 수정 시간은 서버에서 넣어줄 수도 있음
}
