package com.project.deokhugam.notification.service.impl;

import com.project.deokhugam.notification.client.DashboardRestClient;
import com.project.deokhugam.notification.client.ReviewRestClient;
import com.project.deokhugam.dashboard.dto.PopularReviewDto;
import com.project.deokhugam.global.exception.CustomException;
import com.project.deokhugam.global.exception.ErrorCode;
import com.project.deokhugam.global.response.CustomApiResponse;
import com.project.deokhugam.notification.dto.request.NotificationUpdateRequest;
import com.project.deokhugam.notification.dto.response.CursorPageResponseNotificationDto;
import com.project.deokhugam.notification.dto.response.NotificationDto;
import com.project.deokhugam.notification.dto.response.ReviewDetailResponse;
import com.project.deokhugam.notification.entity.Notification;
import com.project.deokhugam.notification.repository.NotificationRepository;
import com.project.deokhugam.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final DashboardRestClient dashboardRestClient;
    private final ReviewRestClient reviewRestClient;

    @Override
    @Transactional
    public NotificationDto updateConfirmed(UUID notificationId, UUID userId, NotificationUpdateRequest request) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!notification.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        notification.setConfirmed(request.getConfirmed());
        notification.setUpdatedAt(LocalDateTime.now());

        return toDto(notification, userId);
    }

    @Override
    @Transactional
    public void readAllNotifications(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);

        for (Notification notification : notifications) {
            if (!notification.getConfirmed()) {
                notification.setConfirmed(true);
                notification.setUpdatedAt(LocalDateTime.now());
                notificationRepository.save(notification);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CustomApiResponse<CursorPageResponseNotificationDto> getNotifications(UUID userId, LocalDateTime after, int limit, String direction) {
        if (after == null) {
            after = LocalDateTime.now();
        }

        PageRequest pageRequest = PageRequest.of(0, limit + 1);

        List<Notification> notifications = "ASC".equalsIgnoreCase(direction)
                ? notificationRepository.findNotificationsCursorAsc(userId, after, pageRequest)
                : notificationRepository.findNotificationsCursorDesc(userId, after, pageRequest);

        List<Notification> limitedResult = notifications.stream().limit(limit).collect(Collectors.toList());
        List<NotificationDto> dtos = limitedResult.stream()
                .map(notification -> toDto(notification, userId))
                .collect(Collectors.toList());

        boolean hasNext = notifications.size() > limit;
        LocalDateTime nextAfter = limitedResult.isEmpty() ? null : limitedResult.get(limitedResult.size() - 1).getCreatedAt();
        String nextCursor = limitedResult.isEmpty() ? null : limitedResult.get(limitedResult.size() - 1).getId().toString();

        CursorPageResponseNotificationDto pageResponse = new CursorPageResponseNotificationDto(
                dtos,
                nextCursor,
                nextAfter,
                dtos.size(),
                notifications.size(),
                hasNext
        );

        return CustomApiResponse.ok(pageResponse);
    }

    @Override
    @Transactional
    public NotificationDto createCommentNotification(UUID receiverUserId, UUID reviewId, UUID bookId, String content) {
        Notification notification = Notification.builder()
                .userId(receiverUserId)
                .reviewId(reviewId)
                .bookId(bookId)
                .content(content)
                .confirmed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toDto(notificationRepository.save(notification), receiverUserId);
    }

    @Override
    @Transactional
    public NotificationDto createLikeNotification(UUID receiverUserId, UUID reviewId, UUID bookId, String content) {
        Notification notification = Notification.builder()
                .userId(receiverUserId)
                .reviewId(reviewId)
                .bookId(bookId)
                .content(content)
                .confirmed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toDto(notificationRepository.save(notification), receiverUserId);
    }

    @Override
    @Transactional
    public void createRankingNotificationsFromDashboard(String period) {
        List<PopularReviewDto> top10Reviews = dashboardRestClient.getTop10PopularReviews(period);

        for (PopularReviewDto review : top10Reviews) {
            Notification notification = Notification.builder()
                    .userId(UUID.fromString(review.getUserId()))
                    .reviewId(UUID.fromString(review.getReviewId()))
                    .bookId(UUID.fromString(review.getBookId()))
                    .content(String.format("당신의 리뷰가 %s 인기 리뷰 %d위에 선정되었습니다!", review.getPeriod(), review.getRank()))
                    .confirmed(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }
    }

    private NotificationDto toDto(Notification notification, UUID requesterId) {
        String reviewTitle = fetchReviewTitle(notification.getReviewId(), requesterId);

        return NotificationDto.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .reviewId(notification.getReviewId())
                .reviewTitle(reviewTitle)
                .content(notification.getContent())
                .confirmed(notification.getConfirmed())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }

    private String fetchReviewTitle(UUID reviewId, UUID requesterId) {
        try {
            ReviewDetailResponse reviewDetail = reviewRestClient.getReviewDetail(reviewId, requesterId);
            return reviewDetail.getBookTitle();
        } catch (Exception e) {
            return "";
        }
    }
}
