package com.project.deokhugam.book.repository;

import com.project.deokhugam.book.dto.BookResponse;
import com.project.deokhugam.book.dto.CursorPageResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;

public interface BookRepositoryCustom {
  CursorPageResponse<BookResponse> searchBooks(
      String keyword,
      String orderBy,
      Sort.Direction direction,
      String cursor,
      LocalDateTime after,
      int limit
  );
}
