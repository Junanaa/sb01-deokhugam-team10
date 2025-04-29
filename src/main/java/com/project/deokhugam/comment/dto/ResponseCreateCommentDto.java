package com.project.deokhugam.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseCreateCommentDto {
    private String id;
    private String reviewId;
    private String userId;
    private String userNickname;
    private String content;
    private String createdAt;
    private String updatedAt;
}
