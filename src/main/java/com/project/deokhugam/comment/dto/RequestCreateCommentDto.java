package com.project.deokhugam.comment.dto;

import lombok.Data;

@Data
public class RequestCreateCommentDto {
    private String reviewId;
    private String userId;
    private String content;

    private RequestCreateCommentDto() {
    }
}
