package com.project.deokhugam.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

	@Id
	@Column(name = "notification_id", columnDefinition = "BINARY(16)")
	// UUID로 변경
	private UUID id;

	@Column(name = "review_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID reviewId;

	@Column(name = "book_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID bookId;

	@Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID userId;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "confirmed", nullable = false)
	private Boolean confirmed = false;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
}
