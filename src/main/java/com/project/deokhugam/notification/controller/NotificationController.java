package com.project.deokhugam.notification.controller;

import com.project.deokhugam.global.response.CustomApiResponse;
import com.project.deokhugam.notification.dto.request.NotificationUpdateRequest;
import com.project.deokhugam.notification.dto.response.CursorPageResponseNotificationDto;
import com.project.deokhugam.notification.dto.response.NotificationDto;
import com.project.deokhugam.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "알림 관리", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "알림 목록 조회",
            description = "사용자의 알림 목록을 커서 기반으로 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "알림 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = CursorPageResponseNotificationDto.class))
    )
    @ApiResponse(responseCode = "400", description = "잘못된 요청 (정렬 방향 오류, 파라미터 누락 등)")
    @ApiResponse(responseCode = "404", description = "사용자 정보 없음")
    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    @GetMapping
    public ResponseEntity<CustomApiResponse<CursorPageResponseNotificationDto>> getNotifications(
            @Parameter(description = "사용자 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestParam UUID userId,

            @Parameter(description = "정렬 방향", example = "DESC", schema = @Schema(allowableValues = {"ASC", "DESC"}, defaultValue = "DESC"))
            @RequestParam(defaultValue = "DESC")
            @Pattern(regexp = "ASC|DESC", message = "정렬 방향은 ASC 또는 DESC만 허용.") String direction,

            @Parameter(description = "커서 페이지네이션 커서", example = "cursor")
            @RequestParam(required = false) String cursor,

            @Parameter(description = "보조 커서 (createdAt)", example = "2025-04-06T15:04:05.000Z")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "limit은 1 이상의 정수.")
            @Max(value = 100, message = "limit은 최대 100까지 허용.") int limit
    ) {
        CustomApiResponse<CursorPageResponseNotificationDto> response = notificationService.getNotifications(userId, after, limit, direction);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @Operation(
            summary = "알림 읽음 상태 업데이트",
            description = "특정 알림의 읽음 상태를 업데이트합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "알림 상태 업데이트 성공",
            content = @Content(schema = @Schema(implementation = NotificationDto.class))
    )
    @ApiResponse(responseCode = "400", description = "잘못된 요청 (입력값 검증 실패, 요청자 ID 누락)")
    @ApiResponse(responseCode = "403", description = "알림 수정 권한 없음")
    @ApiResponse(responseCode = "404", description = "알림 정보 없음")
    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    @PatchMapping("/{notificationId}")
    public ResponseEntity<CustomApiResponse<NotificationDto>> updateNotification(
            @Parameter(description = "알림 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID notificationId,

            @Parameter(description = "요청자 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestHeader("Deokhugam-Request-User-ID") UUID userId,

            @RequestBody @Valid NotificationUpdateRequest request
    ) {
        NotificationDto updated = notificationService.updateConfirmed(notificationId, userId, request);
        return ResponseEntity.ok(CustomApiResponse.ok(updated));
    }

    @Operation(summary = "모든 알림 읽음 처리", description = "사용자의 모든 알림을 읽음 처리합니다.")
    @ApiResponse(responseCode = "204", description = "알림 읽음 처리 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 (사용자 ID 누락)")
    @ApiResponse(responseCode = "404", description = "사용자 정보 없음")
    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    @PatchMapping("/read-all")
    public ResponseEntity<Void> readAllNotifications(
            @Parameter(description = "요청자 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestHeader("Deokhugam-Request-User-ID") UUID userId
    ) {
        notificationService.readAllNotifications(userId);
        return ResponseEntity.noContent().build();
    }
}
