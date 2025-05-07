package com.project.deokhugam.book.dto;

import com.project.deokhugam.book.entity.Book;
import java.time.Instant;
import java.util.List;

public record CursorPageResponseBookDto(
    List<Book> content,
    String nextCursor,
    Instant nextAfter,
    int size,
    int totalElements,
    Boolean hasNext) {}
