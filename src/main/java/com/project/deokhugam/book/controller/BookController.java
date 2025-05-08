package com.project.deokhugam.book.controller;

import com.project.deokhugam.book.dto.BookInfoResponse;
import com.project.deokhugam.book.dto.BookRequestDto;
import com.project.deokhugam.book.dto.BookResponse;
import com.project.deokhugam.book.dto.BookUpdateRequest;
import com.project.deokhugam.book.dto.CursorPageResponse;
import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.book.service.BookService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")
public class BookController {

  private final BookService bookService;

  // 생성자 주입
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  // 도서 등록 API
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> registerBook(
      @RequestPart("bookData") BookRequestDto request,
      @RequestPart("thumbnailImage") MultipartFile file){
    bookService.registerBook(request, file);
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

  @PatchMapping(value = "/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Book> updateBook(
      @PathVariable UUID bookId,
      @RequestPart("bookData") @Valid BookUpdateRequest request,
      @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage) {

    Book updatedBook = bookService.update(bookId, request, thumbnailImage);
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

  @GetMapping
  public ResponseEntity<CursorPageResponse<BookResponse>> getBooks(
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "title") String orderBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction direction,
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after,
      @RequestParam(defaultValue = "50") int limit
  ) {
    CursorPageResponse<BookResponse> response = bookService.getBooks(keyword, orderBy, direction, cursor, after, limit);
    return ResponseEntity.ok(response);
  }
}