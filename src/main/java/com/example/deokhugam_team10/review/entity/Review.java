package com.example.deokhugam_team10.review.entity;

import com.example.deokhugam_team10.book.entity.Book;
import com.example.deokhugam_team10.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id")
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private String reviewContent;

  private Long likeCount;
  private Long commentCount;
  private Long reviewRating;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long reviewRank;
  private Long reviewScore;
  private Boolean liked;
}

