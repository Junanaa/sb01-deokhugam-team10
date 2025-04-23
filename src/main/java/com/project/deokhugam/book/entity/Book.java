package com.project.deokhugam.book.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "book_id", updatable = false, nullable = false)
	private UUID bookId;

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
