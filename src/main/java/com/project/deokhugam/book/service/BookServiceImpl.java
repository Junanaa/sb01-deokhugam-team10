package com.project.deokhugam.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.deokhugam.book.dto.BookInfoResponse;
import com.project.deokhugam.book.dto.BookRequestDto;
import com.project.deokhugam.book.dto.BookResponse;
import com.project.deokhugam.book.dto.BookUpdateRequest;
import com.project.deokhugam.book.dto.CursorPageResponse;
import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.book.exception.BookNotFoundException;
import com.project.deokhugam.book.mapper.BookMapper;
import com.project.deokhugam.book.repository.BookRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class BookServiceImpl implements BookService {

  @Value("${naver.client.id}")
  private String clientId;

  @Value("${naver.client.secret}")
  private String clientSecret;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
    this.bookRepository = bookRepository;
    this.bookMapper = bookMapper;
  }

  @Override
  public void create(BookRequestDto request) {
    Book book = bookMapper.toEntity(request);
    bookRepository.save(book);
  }

  @Override
  public Book findBookById(UUID bookId) {
    return bookRepository.findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(bookId));
  }

  @Override
  public void delete(UUID bookId) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(bookId));

    book.setDeleted(true); // 논리 삭제 처리
    bookRepository.save(book);
  }

  @Override
  public Book update(UUID bookId, BookUpdateRequest request) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(bookId));

    if (request.getTitle() != null) {
      book.setTitle(request.getTitle());
    }
    if (request.getAuthor() != null) {
      book.setAuthor(request.getAuthor());
    }
    if (request.getDescription() != null) {
      book.setDescription(request.getDescription());
    }
    if (request.getPublisher() != null) {
      book.setPublisher(request.getPublisher());
    }
    if (request.getPublishedDate() != null) {
      book.setPublishedDate(request.getPublishedDate());
    }
    if (request.getIsbn() != null) {
      book.setIsbn(request.getIsbn());
    }
    if (request.getThumbnailUrl() != null) {
      book.setThumbnailUrl(request.getThumbnailUrl());
    }
    if (request.getReviewCount() != null) {
      book.setReviewCount(request.getReviewCount());
    }
    if (request.getRating() != null) {
      book.setBookRating(request.getRating());
    }

    book.setUpdatedAt(LocalDateTime.now());

    return bookRepository.save(book);
  }

  @Override
  public BookInfoResponse searchByIsbn(String isbn) {
    String url = "https://openapi.naver.com/v1/search/book.json?query=" + isbn;

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Naver-Client-Id", clientId);
    headers.set("X-Naver-Client-Secret", clientSecret);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

    try {
      JsonNode root = objectMapper.readTree(response.getBody());
      JsonNode item = root.path("items").get(0); // 첫 번째 아이템

      BookInfoResponse result = new BookInfoResponse();
      result.setTitle(item.path("title").asText());
      result.setLink(item.path("link").asText());
      result.setImage(item.path("image").asText());
      result.setAuthor(item.path("author").asText());
      result.setDiscount(item.path("discount").asText());
      result.setPublisher(item.path("publisher").asText());
      result.setPubdate(item.path("pubdate").asText());
      result.setIsbn(item.path("isbn").asText());
      result.setDescription(item.path("description").asText());

      return result;
    } catch (Exception e) {
      throw new RuntimeException("도서 조회 실패", e);
    }
  }

  @Override
  public void deleteHard(UUID bookId) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(bookId));

    bookRepository.delete(book);
  }

  @Override
  public CursorPageResponse<BookResponse> getBooks(
      String keyword,
      String orderBy,
      Sort.Direction direction,
      String cursor,
      LocalDateTime after,
      int limit
  ) {
    return bookRepository.searchBooks(keyword, orderBy, direction, cursor, after, limit);
  }
}
