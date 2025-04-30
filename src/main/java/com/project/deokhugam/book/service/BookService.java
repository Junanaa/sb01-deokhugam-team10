package com.project.deokhugam.book.service;

import com.project.deokhugam.book.dto.BookInfoResponse;
import com.project.deokhugam.book.dto.BookRequestDto;
import com.project.deokhugam.book.dto.BookUpdateRequest;
import com.project.deokhugam.book.entity.Book;
import java.util.UUID;

public interface BookService {
  void create(BookRequestDto request);

  Book findBookById(UUID bookId);

  void delete(UUID bookId);

  Book update(UUID bookId, BookUpdateRequest request);

  BookInfoResponse searchByIsbn(String isbn);

  void deleteHard(UUID bookId);
}
