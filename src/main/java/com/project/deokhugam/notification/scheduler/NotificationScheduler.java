package com.project.deokhugam.notification.scheduler;

import com.project.deokhugam.notification.entity.Notification;
import com.project.deokhugam.notification.repository.NotificationRepository;
import com.project.deokhugam.notification.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 알림 스케줄러
 * - 읽음 처리된 7일 지난 알림 삭제
 * - 인기 리뷰 Top10 알림 생성
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RequestMapping("/api/test/notifications")
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    /**
     * 1. 읽음 처리된 알림 중 7일 지난 항목 삭제
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 정각
    @Transactional
    public void deleteOldReadNotifications() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        List<Notification> oldReadNotifications = notificationRepository.findAllByConfirmedIsTrueAndUpdatedAtBefore(threshold);

        if (!oldReadNotifications.isEmpty()) {
            notificationRepository.deleteAllInBatch(oldReadNotifications);
            log.info(" {}개의 읽음 처리된 7일 지난 알림 삭제 완료", oldReadNotifications.size());
        } else {
            log.info(" 삭제할 읽음 알림 없음");
        }
    }

    /**
     *  2. 인기 리뷰 Top10 알림 생성
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 정각
    @Transactional
    public void notifyPopularReviewsTop10() {
        String[] periods = {"DAILY", "WEEKLY", "MONTHLY", "ALL_TIME"};

        for (String period : periods) {
            notificationService.createRankingNotificationsFromDashboard(period);
            log.info(" {} 기간 인기 리뷰 Top10 알림 생성 완료", period);
        }
    }


}
