package com.project.deokhugam.notification.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  커서 기반 알림 목록 응답 DTO
 * - content: 실제 알림 목록
 * - nextAfter: 다음 페이지 기준 시간
 * - hasNext: 다음 페이지 존재 여부
 */
public record CursorPageResponseNotificationDto(
        List<NotificationDto> content,
        String nextCursor,
        LocalDateTime nextAfter,
        int size,
        long totalElements,
        boolean hasNext
) {}
