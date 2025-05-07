package com.project.deokhugam.notification.service;

import com.project.deokhugam.global.response.CustomApiResponse;
import com.project.deokhugam.notification.dto.request.NotificationUpdateRequest;
import com.project.deokhugam.notification.dto.response.CursorPageResponseNotificationDto;
import com.project.deokhugam.notification.dto.response.NotificationDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * NotificationService
 * 알림 기능의 서비스 레이어 인터페이스
 */
public interface NotificationService {

    NotificationDto updateConfirmed(UUID notificationId, UUID userId, NotificationUpdateRequest request);

    void readAllNotifications(UUID userId);

    CustomApiResponse<CursorPageResponseNotificationDto> getNotifications(UUID userId, LocalDateTime after, int limit, String direction);

    NotificationDto createCommentNotification(UUID receiverUserId, UUID reviewId, UUID bookId, String content);

    NotificationDto createLikeNotification(UUID receiverUserId, UUID reviewId, UUID bookId, String content);

    void createRankingNotificationsFromDashboard(String period); //

}
