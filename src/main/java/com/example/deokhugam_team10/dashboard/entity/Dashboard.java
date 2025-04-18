package com.example.deokhugam_team10.dashboard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dashboard")
public class Dashboard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String type;
  private String period;
  private String targetId;
  private String bookId;
  private String userId;
  private Double score;
  private Long rank;
  private Long likeCount;
  private Long commentCount;
  private Long reviewCount;
  private Double reviewRating;
  private Double reviewScoreSum;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
