package com.project.deokhugam.comment.repository;

import com.project.deokhugam.comment.entity.Comment;
import com.project.deokhugam.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Long review(Review review);
}
