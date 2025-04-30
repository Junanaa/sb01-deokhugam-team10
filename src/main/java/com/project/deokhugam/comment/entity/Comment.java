package com.project.deokhugam.comment.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.user.entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment")
public class Comment {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "comment_id", updatable = false, nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Lob
	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private LocalDateTime createdAt;
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Column(nullable = false)
	private boolean deleted;

	public Comment(Review review, User user, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.review = review;
		this.user = user;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}

