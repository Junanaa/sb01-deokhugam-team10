package com.project.deokhugam.review.repository;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID>, ReviewCustomRepository {
    boolean existsByUserAndBook(User user, Book book);
}
