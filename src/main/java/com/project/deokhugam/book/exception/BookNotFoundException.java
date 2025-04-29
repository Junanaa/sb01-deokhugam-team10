package com.project.deokhugam.book.exception;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(UUID bookId) {
    super("Book not found with id: " + bookId);
  }
}
