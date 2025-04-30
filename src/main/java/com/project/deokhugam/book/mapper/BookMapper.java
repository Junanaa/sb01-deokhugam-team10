package com.project.deokhugam.book.mapper;

import com.project.deokhugam.book.dto.BookRequestDto;
import com.project.deokhugam.book.entity.Book;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
  public Book toEntity(BookRequestDto request) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setDescription(request.getDescription());
    book.setPublisher(request.getPublisher());
    book.setPublishedDate(request.getPublishedDate());
    book.setIsbn(request.getIsbn());
    book.setThumbnailUrl(request.getThumbnailUrl());
    book.setReviewCount(0L);
    book.setBookRating(0L);
    book.setCreatedAt(LocalDateTime.now());
    book.setUpdatedAt(LocalDateTime.now());
    return book;
  }
}
