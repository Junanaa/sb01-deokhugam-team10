package com.project.deokhugam.notification.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 알림 조회 시 클라이언트에게 응답되는 DTO
 * DB의 Notification Entity 내용을 기반으로 응답을 구성한다.
 */
@Getter
@Builder
public class NotificationDto {

    private UUID id;
    private UUID userId;
    private UUID reviewId;
    private String reviewTitle;
    private String content;
    private Boolean confirmed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

