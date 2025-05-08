package com.project.deokhugam.comment.repository;

import com.project.deokhugam.comment.entity.Comment;
import com.project.deokhugam.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByReview_ReviewIdAndDeletedFalseOrderByCreatedAtDesc(UUID reviewId);

    @Query("""
    SELECT c FROM Comment c
    WHERE c.review.reviewId = :reviewId
      AND c.deleted = false
      AND (:cursorCreatedAt IS NULL OR (
            (c.createdAt < :cursorCreatedAt) OR 
            (c.createdAt = :cursorCreatedAt AND c.id < :cursorId)
      ))
    ORDER BY c.createdAt DESC, c.id DESC
""")
    List<Comment> findCommentsByReviewIdWithCursor(
            @Param("reviewId") String reviewId,
            @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
            @Param("cursorId") UUID cursorId,
            Pageable pageable
    );
}
