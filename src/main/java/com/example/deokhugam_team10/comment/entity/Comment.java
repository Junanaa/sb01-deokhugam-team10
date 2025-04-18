package com.example.deokhugam_team10.comment.entity;

import com.example.deokhugam_team10.book.entity.Book;
import com.example.deokhugam_team10.review.entity.Review;
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
@Table(name = "comment")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id")
  private Review review;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id")
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Lob
  private String content;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

