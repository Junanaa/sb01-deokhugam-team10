package com.example.deokhugam_team10.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bookId;

  private String title;
  private String author;

  private String description;

  private String publisher;
  private LocalDateTime publishedDate;
  private String isbn;
  private String thumbnailUrl;
  private String thumbnailImage;
  private Long reviewCount;
  private Long bookRating;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long bookRank;
  private Long bookScore;
}

