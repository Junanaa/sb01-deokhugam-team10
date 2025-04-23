package com.project.deokhugam.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") //user가 예약명이라 테이블명 users로 설정
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  private String email;
  private String nickname;
  private LocalDateTime createdAt;
  private Long rank;
  private Long score;
}
