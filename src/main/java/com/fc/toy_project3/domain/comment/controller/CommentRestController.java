package com.fc.toy_project3.domain.comment.controller;

import com.fc.toy_project3.domain.comment.dto.request.CommentCreateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentDeleteResponseDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentResponseDTO;
import com.fc.toy_project3.domain.comment.service.CommentService;
import com.fc.toy_project3.global.common.ResponseDTO;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseDTO<CommentResponseDTO>> postComment(@AuthenticationPrincipal
        CustomUserDetails customUserDetails,
        @Valid @RequestBody CommentCreateRequestDTO commentCreateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseDTO.res(HttpStatus.CREATED, commentService.postComment(
                    customUserDetails.getMemberId(), commentCreateRequestDTO),
                "성공적으로 댓글을 등록했습니다."));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<CommentResponseDTO>> patchComment(@AuthenticationPrincipal
        CustomUserDetails customUserDetails,
        @PathVariable long commentId,
        @Valid @RequestBody CommentUpdateRequestDTO commentUpdateRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK,
                commentService.patchComment(customUserDetails.getMemberId(), commentId,
                    commentUpdateRequestDTO),
                "댓글을 성공적으로 수정했습니다."));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<CommentDeleteResponseDTO>> softDeleteComment(
        @AuthenticationPrincipal
            CustomUserDetails customUserDetails,
        @PathVariable long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK,
                commentService.softDeleteComment(customUserDetails.getMemberId(), commentId),
                "댓글을 성공적으로 삭제했습니다."));
    }
}
