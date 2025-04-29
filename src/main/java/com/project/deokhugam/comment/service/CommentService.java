package com.project.deokhugam.comment.service;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.comment.dto.RequestCreateCommentDto;
import com.project.deokhugam.comment.dto.ResponseCreateCommentDto;
import com.project.deokhugam.comment.entity.Comment;
import com.project.deokhugam.comment.repository.CommentRepository;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ResponseCreateCommentDto save(RequestCreateCommentDto createCommentDto) {
        Comment savedComment = commentRepository.save(new Comment(
                reviewRepository.findById(createCommentDto.getReviewId()),
                userRepository.findById(createCommentDto.getUserId()),
                createCommentDto.getContent(),
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        return new ResponseCreateCommentDto(
                String.valueOf(savedComment.getId()),
                String.valueOf(savedComment.getReview().getReviewId()),
                String.valueOf(savedComment.getUser().getUserId()),
                String.valueOf(savedComment.getUser().getNickname()),
                String.valueOf(savedComment.getContent()),
                String.valueOf(savedComment.getCreatedAt()),
                String.valueOf(savedComment.getUpdatedAt())
        );
    }

    /**
     * 전체 조회는 마지막에 해야되나.
     * 페이징 부분 공부가 더 필요함.
     *
     * + deleted 상태를 봐서 논리적으로 없는 댓글은 조회에서 제외해야함.
     */
    public void findComments(String reviewId, String direction, String cursor, String after, Integer limit) {
        reviewRepository.findById(reviewId);
    }

    public ResponseCreateCommentDto findById(UUID commentId) {
        Optional<Comment> findComment = commentRepository.findById(commentId);

        return new ResponseCreateCommentDto(
                String.valueOf(findComment.get().getId()),
                String.valueOf(findComment.get().getReview().getReviewId()),
                String.valueOf(findComment.get().getUser().getUserId()),
                String.valueOf(findComment.get().getUser().getNickname()),
                String.valueOf(findComment.get().getContent()),
                String.valueOf(findComment.get().getCreatedAt()),
                String.valueOf(findComment.get().getUpdatedAt())
        );
    }

    public ResponseEntity<ResponseCreateCommentDto> updateCommentContents(String commentId, String deokhugamRequestUserId, String content) {

        // 사용자 아이디는 검증을 위해서 필요한건가?
        Optional<User> findUser = userRepository.findById(deokhugamRequestUserId);
        Optional<Comment> findComment = commentRepository.findById(UUID.fromString(commentId));

        // 요청으로 온 UserID와 댓글의 UserId가 다르면 로그찍고 badRequest반환
        if(!findUser.get().getUserId().equals(findComment.get().getUser().getUserId())) {
            log.debug("findUser Id={}", findUser.get().getUserId());
            log.debug("findComment의 User Id={}", findComment.get().getUser().getUserId());
            return ResponseEntity.badRequest().body(new ResponseCreateCommentDto(
                    String.valueOf(findComment.get().getId()),
                    String.valueOf(findComment.get().getReview().getReviewId()),
                    String.valueOf(findComment.get().getUser().getUserId()),
                    String.valueOf(findComment.get().getUser().getNickname()),
                    String.valueOf(findComment.get().getContent()),
                    String.valueOf(findComment.get().getCreatedAt()),
                    String.valueOf(findComment.get().getUpdatedAt())
            ));
        }

        // 이러면 변경 감지를 해서 알아서 update를 날려준다는 거제?
        findComment.get().setContent(content);

        return ResponseEntity.ok(new ResponseCreateCommentDto(
                String.valueOf(findComment.get().getId()),
                String.valueOf(findComment.get().getReview().getReviewId()),
                String.valueOf(findComment.get().getUser().getUserId()),
                String.valueOf(findComment.get().getUser().getNickname()),
                String.valueOf(findComment.get().getContent()),
                String.valueOf(findComment.get().getCreatedAt()),
                String.valueOf(findComment.get().getUpdatedAt())
        ));
    }

    public HttpStatus deleteCommentLogically(String commentId, String deokhugamRequestUserId) {

        Optional<User> findUser = userRepository.findById(deokhugamRequestUserId);
        Optional<Comment> findComment = commentRepository.findById(UUID.fromString(commentId));

        // 요청 사용자 아이디와 댓글의 사용자 아이디 정보가 일치하지 않으면
        // 권한이 없다고 판단하고 403코드 반환
        if(!findUser.get().getUserId().equals(findComment.get().getUser().getUserId())) {
            return HttpStatus.FORBIDDEN;
        }

        // Comment정보가 없으면 404반환
        if(!findComment.isPresent()) {
            return HttpStatus.NOT_FOUND;
        }

        // 논리적 삭제.
        // deleted 상태를 true로 변경함으로써 논리적 삭제를 구현함.
        findComment.get().setDeleted(true);
        return HttpStatus.OK;

    }

    public HttpStatus deleteCommentHardly(String commentId, String deokhugamRequestUserId) {
        Optional<User> findUser = userRepository.findById(deokhugamRequestUserId);
        Optional<Comment> findComment = commentRepository.findById(UUID.fromString(commentId));

        // 요청 사용자 아이디와 댓글의 사용자 아이디 정보가 일치하지 않으면
        // 권한이 없다고 판단하고 403코드 반환
        if(!findUser.get().getUserId().equals(findComment.get().getUser().getUserId())) {
            return HttpStatus.FORBIDDEN;
        }

        // Comment정보가 없으면 404반환
        if(!findComment.isPresent()) {
            return HttpStatus.NOT_FOUND;
        }

        // 논리적 삭제.
        // 실제 DB에서 Comment정보를 삭제.
        commentRepository.delete(findComment.get());
        return HttpStatus.OK;
    }
}
