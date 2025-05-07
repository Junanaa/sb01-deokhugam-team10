package com.project.deokhugam.book.dto;

import com.project.deokhugam.book.entity.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

  private UUID id;
  private String title;
  private String author;
  private String description;
  private String publisher;
  private LocalDate publishedDate;
  private String isbn;
  private String thumbnailUrl;
  private Long reviewCount;
  private Double rating;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static BookResponse from(Book book) {
    BookResponse response = new BookResponse();
    response.setId(book.getBookId());
    response.setTitle(book.getTitle());
    response.setAuthor(book.getAuthor());
    response.setDescription(book.getDescription());
    response.setPublisher(book.getPublisher());
    response.setPublishedDate(book.getPublishedDate());
    response.setIsbn(book.getIsbn());
    response.setThumbnailUrl(book.getThumbnailUrl());
    response.setReviewCount(book.getReviewCount());
    response.setRating(book.getBookRating() != null ? book.getBookRating() / 10.0 : null);
    response.setCreatedAt(book.getCreatedAt());
    response.setUpdatedAt(book.getUpdatedAt());
    return response;
  }

  // Getters & Setters
  // (생략 시 Lombok의 @Getter @Setter 사용 가능)
}

