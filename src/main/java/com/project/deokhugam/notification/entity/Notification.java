package com.project.deokhugam.notification.entity;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long notificationId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "review_id")
  private Review review;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id")
  private Book book;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @Lob
  private String content;

  private Boolean confirmed;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

