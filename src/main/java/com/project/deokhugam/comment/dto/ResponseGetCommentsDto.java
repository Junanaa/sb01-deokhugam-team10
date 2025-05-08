package com.project.deokhugam.comment.dto;

import lombok.Data;

@Data
public class ResponseGetCommentsDto {
    private ResponseCreateCommentDto responseCreateCommentDto;
    private String nextCursor;
    private String nextAfter;
    private Integer size;
    private Integer totalElements;
    private Boolean hasNext;

}
