package com.project.deokhugam.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.deokhugam.book.controller.BookController;
import com.project.deokhugam.book.dto.BookRequestDto;
import com.project.deokhugam.book.service.BookServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookServiceImpl bookService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldCreateBook() throws Exception {
    BookRequestDto request = new BookRequestDto(
        "테스트 제목",
        "테스트 저자",
        "테스트 설명",
        "테스트 출판사",
        LocalDate.now(),
        "1234567890",
        "http://thumbnail.url",
        0L,
        0L,
        LocalDateTime.now(),
        LocalDateTime.now()
    );

    mockMvc.perform(post("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    System.out.println("post Test 성공");
  }
}
