package com.project.deokhugam.book.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookInfoResponse {
  private String title;
  private String thumbnailImage;
  private String author;
  private String publisher;
  private LocalDate publishedDate;
  private String isbn;
  private String description;
}
