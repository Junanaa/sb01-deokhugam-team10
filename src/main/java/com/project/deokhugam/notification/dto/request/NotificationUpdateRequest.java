package com.project.deokhugam.notification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class NotificationUpdateRequest {

    @NotNull(message = "confirmed 필드는 필수입니다.")
    private Boolean confirmed;

    public NotificationUpdateRequest(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}

