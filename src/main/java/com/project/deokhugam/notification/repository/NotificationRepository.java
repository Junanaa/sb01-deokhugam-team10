package com.project.deokhugam.notification.repository;

import com.project.deokhugam.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("SELECT n FROM Notification n " +
            "WHERE n.userId = :userId " +
            "AND n.createdAt < :after " +
            "ORDER BY n.createdAt DESC")
    List<Notification> findNotificationsCursorDesc(
            @Param("userId") UUID userId,
            @Param("after") LocalDateTime after,
            Pageable pageable
    );

    @Query("SELECT n FROM Notification n " +
            "WHERE n.userId = :userId " +
            "AND n.createdAt < :after " +
            "ORDER BY n.createdAt ASC")
    List<Notification> findNotificationsCursorAsc(
            @Param("userId") UUID userId,
            @Param("after") LocalDateTime after,
            Pageable pageable
    );

    List<Notification> findByUserId(UUID userId);

    List<Notification> findAllByConfirmedIsTrueAndUpdatedAtBefore(LocalDateTime cutoff);
}
