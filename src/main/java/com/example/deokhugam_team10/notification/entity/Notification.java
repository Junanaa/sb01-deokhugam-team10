package com.example.deokhugam_team10.notification.entity;

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

