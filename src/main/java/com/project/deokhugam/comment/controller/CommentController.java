package com.project.deokhugam.comment.controller;

import com.project.deokhugam.comment.dto.RequestCreateCommentDto;
import com.project.deokhugam.comment.dto.ResponseCreateCommentDto;
import com.project.deokhugam.comment.dto.ResponseGetCommentsDto;
import com.project.deokhugam.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping
    public ResponseCreateCommentDto createComment(@RequestBody RequestCreateCommentDto createCommentDto) {

        return commentService.save(createCommentDto);
    }

    // 리뷰 댓글 목록 조회
    @GetMapping
    public ResponseGetCommentsDto getComments(
            @RequestParam String reviewId,
            /**
             *  정렬 방향
             *  - Avaiable values : ASC, DESC <- validation을 이용해야 할 듯.
             *  - Default value : DESC.
             */
            @RequestParam(required = false, defaultValue = "DESC") String direction,
            @RequestParam(required = false) String cursor,
            /**
             * 보조 커서
             * - data-time을 받음. 특정 날짜 이후 데이터를 조회.
             */
            @RequestParam(required = false) String after,
            @RequestParam(required = false, defaultValue = "50") Integer limit
    ) {
        commentService.findComments(reviewId, direction, cursor, after, limit);

        return null;
    }

    // 댓글 상세 정보 조회
    @GetMapping("/{commentId}")
    public ResponseCreateCommentDto getCommentById(@RequestParam(value = "commentId") UUID commentId) {

        ResponseCreateCommentDto result = commentService.findById(commentId);

        return result;
    }

    // 댓글 논리 삭제
    // 논리 삭제가 뭐인교?
    @DeleteMapping("/{commentId}")
    public HttpStatus deleteCommentLogically(
            @RequestParam String commentId,
            @RequestParam String deokhugamRequestUserId
    ) {
        return commentService.deleteCommentLogically(commentId, deokhugamRequestUserId);
    }

    // 댓글 수정
    @RequestMapping(value = "/{commendId}", method = RequestMethod.PATCH)
    public ResponseEntity<ResponseCreateCommentDto> updateComment(
            @RequestParam(value = "commentId") String commentId,
            @RequestParam(value = "deokhugamRequestUserId") String deokhugamRequestUserId,
            @RequestBody String content) {
        return commentService.updateCommentContents(commentId, deokhugamRequestUserId, content);
    }

    // 댓글 물리 삭제
    @DeleteMapping("/{commentId}/hard")
    public HttpStatus deleteCommentHardly(
            @RequestParam String commentId,
            @RequestParam String deokhugamRequestUserId
    ) {
        return commentService.deleteCommentHardly(commentId, deokhugamRequestUserId);
    }
}
