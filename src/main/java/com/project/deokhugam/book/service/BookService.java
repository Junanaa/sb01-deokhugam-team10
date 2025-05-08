package com.project.deokhugam.book.service;

import com.project.deokhugam.book.dto.BookInfoResponse;
import com.project.deokhugam.book.dto.BookRequestDto;
import com.project.deokhugam.book.dto.BookResponse;
import com.project.deokhugam.book.dto.BookUpdateRequest;
import com.project.deokhugam.book.dto.CursorPageResponse;
import com.project.deokhugam.book.entity.Book;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
  void create(BookRequestDto request);

  Book findBookById(UUID bookId);

  void delete(UUID bookId);

  Book update(UUID bookId, BookUpdateRequest request, MultipartFile thumbnailImage);

  BookInfoResponse searchByIsbn(String isbn);

  void deleteHard(UUID bookId);
  CursorPageResponse<BookResponse> getBooks(
      String keyword,
      String orderBy,
      Sort.Direction direction,
      String cursor,
      LocalDateTime after,
      int limit
  );

  void registerBook(BookRequestDto request, MultipartFile thumbnail);
}
