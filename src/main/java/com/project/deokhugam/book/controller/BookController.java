package com.project.deokhugam.book.controller;

import com.project.deokhugam.book.dto.BookInfoResponse;
import com.project.deokhugam.book.dto.BookRequestDto;
import com.project.deokhugam.book.dto.BookUpdateRequest;
import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.book.service.BookService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

  private final BookService bookService;

  // 생성자 주입
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  // 도서 등록 API
  @PostMapping
  public ResponseEntity<Void> createBook(@RequestBody @Valid BookRequestDto request) {
    bookService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{bookId}")
  public ResponseEntity<Book> getBook(@PathVariable UUID bookId){
    Book book = bookService.findBookById(bookId);
    return ResponseEntity.ok(book);
  }

  @DeleteMapping("/{bookId}")
  public ResponseEntity<Void> deleteBook(@PathVariable UUID bookId){
    bookService.delete(bookId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{bookId}")
  public ResponseEntity<Book> updateBook(@PathVariable UUID bookId,
      @RequestBody @Valid BookUpdateRequest request) {
    Book updatedBook = bookService.update(bookId, request);
    return ResponseEntity.ok(updatedBook);
  }

  @GetMapping("/info")
  public ResponseEntity<BookInfoResponse> getBookInfo(@RequestParam String isbn){
    BookInfoResponse result = bookService.searchByIsbn(isbn);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{bookId}/hard")
  public ResponseEntity<Void> deleteBookHard(@PathVariable UUID bookId){
    bookService.deleteHard(bookId);
    return ResponseEntity.noContent().build();
  }
}