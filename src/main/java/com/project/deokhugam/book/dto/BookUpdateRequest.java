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
}
