package com.project.deokhugam.review.mapper;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.review.dto.ReviewCreateRequest;
import com.project.deokhugam.review.dto.ReviewDto;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewCreateRequest dto, Book book, User user) {
        return Review.builder()
                .book(book)
                .user(user)
                .reviewContent(dto.content())
                .rating(Long.valueOf(dto.rating()))
                .likeCount(0L)
                .commentCount(0L)
                .reviewRank(0L)
                .reviewScore(0L)
                .liked(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getReviewId())
                .bookId(review.getBook().getBookId())
                .bookTitle(review.getBook().getTitle())
                .bookThumbnailUrl(review.getBook().getThumbnailUrl())
                .userId(review.getUser().getUserId())
                .userNickname(review.getUser().getNickname())
                .content(review.getReviewContent())
                .rating(review.getRating().intValue())
                .likeCount(review.getLikeCount().intValue())
                .commentCount(review.getCommentCount().intValue())
                .likedByMe(review.getLiked())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public ReviewDto toDto(Review review, UUID requestUserId) {
        return ReviewDto.builder()
                .id(review.getReviewId())
                .bookId(review.getBook().getBookId())
                .bookTitle(review.getBook().getTitle())
                .bookThumbnailUrl(review.getBook().getThumbnailUrl())
                .userId(review.getUser().getUserId())
                .userNickname(review.getUser().getNickname())
                .content(review.getReviewContent())
                .rating(review.getRating().intValue())
                .likeCount(review.getLikeCount().intValue())
                .commentCount(review.getCommentCount().intValue())
                .likedByMe(determineLikedByMe(review, requestUserId))
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    private boolean determineLikedByMe(Review review, UUID requestUserId) {
        // 작성자가 본인이라면 좋아요를 누른 상태로 보지 않는다
        if (review.getUser().getUserId().equals(requestUserId)) {
            return false;
        }
        return Boolean.TRUE.equals(review.getLiked());
    }
}
