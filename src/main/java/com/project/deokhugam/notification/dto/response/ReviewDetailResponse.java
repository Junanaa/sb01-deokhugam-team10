package com.project.deokhugam.notification.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class ReviewDetailResponse {
    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private String bookThumbnailUrl;
    private UUID userId;
    private String userNickname;
    private String content;
    private Long rating;
    private Long likeCount;
    private Long commentCount;
    private Boolean likedByMe;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
