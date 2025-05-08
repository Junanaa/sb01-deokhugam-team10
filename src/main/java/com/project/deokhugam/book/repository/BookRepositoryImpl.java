package com.project.deokhugam.book.repository;

import com.project.deokhugam.book.dto.BookResponse;
import com.project.deokhugam.book.dto.CursorPageResponse;
import com.project.deokhugam.book.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  public CursorPageResponse<BookResponse> searchBooks(String keyword, String orderBy, Sort.Direction direction,
      String cursor, LocalDateTime after, int limit) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Book> query = cb.createQuery(Book.class);
    Root<Book> book = query.from(Book.class);

    List<Predicate> predicates = new ArrayList<>();

    if (keyword != null && !keyword.isEmpty()) {
      String like = "%" + keyword.toLowerCase() + "%";
      predicates.add(cb.or(
          cb.like(cb.lower(book.get("title")), like),
          cb.like(cb.lower(book.get("author")), like),
          cb.like(cb.lower(book.get("isbn")), like)
      ));
    }

    if (after != null) {
      predicates.add(cb.greaterThan(book.get("createdAt"), after));
    }

    query.select(book)
        .where(predicates.toArray(new Predicate[0]))
        .orderBy(direction == Sort.Direction.ASC
            ? cb.asc(book.get(orderBy))
            : cb.desc(book.get(orderBy)));

    List<Book> results = em.createQuery(query)
        .setMaxResults(limit + 1)
        .getResultList();

    boolean hasNext = results.size() > limit;
    if (hasNext) results.remove(results.size() - 1);

    String nextCursor = hasNext ? results.get(results.size() - 1).getBookId().toString() : null;
    LocalDateTime nextAfter = hasNext ? results.get(results.size() - 1).getCreatedAt() : null;

    List<BookResponse> content = results.stream()
        .map(BookResponse::from)
        .toList();

    return new CursorPageResponse<>(content, nextCursor, nextAfter, content.size(), content.size(), hasNext);
  }
}

