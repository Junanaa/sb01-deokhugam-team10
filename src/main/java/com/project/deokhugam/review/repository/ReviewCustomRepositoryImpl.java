package com.project.deokhugam.review.repository;

import com.project.deokhugam.review.entity.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Review> searchReviews(UUID userId, UUID bookId, String keyword,
                                      String orderBy, String direction, String cursor,
                                      String after, int limit) {

        // 1. JPQL 기본
        StringBuilder jpql = new StringBuilder("SELECT r FROM Review r WHERE r.deleted = false");

        // 2. 조건 추가
        if (userId != null) {
            jpql.append(" AND r.user.userId = :userId");
        }
        if (bookId != null) {
            jpql.append(" AND r.book.bookId = :bookId");
        }
        if (StringUtils.hasText(keyword)) {
            jpql.append(" AND (r.user.nickname LIKE :keyword OR r.reviewContent LIKE :keyword OR r.book.title LIKE :keyword)");
        }

        // 3. 커서 조건 추가 (cursor + after 둘 다 고려)
        if (StringUtils.hasText(cursor) && StringUtils.hasText(after)) {
            if ("rating".equals(orderBy)) {
                jpql.append(" AND (r.rating < :cursor OR (r.rating = :cursor AND r.createdAt < :after))");
            } else { // createdAt
                jpql.append(" AND (r.createdAt < :after)");
            }
        }

        // 4. 정렬
        jpql.append(" ORDER BY r.")
                .append(orderBy)
                .append(" ")
                .append(direction);

        if (!"createdAt".equals(orderBy)) {
            jpql.append(", r.createdAt DESC");
        }

        // 5. 쿼리 생성
        TypedQuery<Review> query = em.createQuery(jpql.toString(), Review.class);

        // 6. 파라미터 세팅
        if (userId != null) {
            query.setParameter("userId", userId);
        }
        if (bookId != null) {
            query.setParameter("bookId", bookId);
        }
        if (StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if (StringUtils.hasText(cursor) && StringUtils.hasText(after)) {
            if ("rating".equals(orderBy)) {
                query.setParameter("cursor", Long.valueOf(cursor));
                query.setParameter("after", LocalDateTime.parse(after));
            } else {
                query.setParameter("after", LocalDateTime.parse(after));
            }
        }

        query.setMaxResults(limit);

        return query.getResultList();
    }
}
