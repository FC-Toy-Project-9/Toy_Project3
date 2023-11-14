package com.fc.toy_project3.domain.comment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fc.toy_project3.domain.comment.dto.request.CommentCreateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentDeleteResponseDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentResponseDTO;
import com.fc.toy_project3.domain.comment.service.CommentService;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.global.common.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentRestController commentRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("postComment()는 여행 댓글을 저장할 수 있다.")
    public void saveComment() throws Exception {
        //given
        Long mebmerId = 1L;
        CommentCreateRequestDTO commentCreateRequestDTO = CommentCreateRequestDTO.builder()
            .tripId(1L)
            .content("여행 계획 정말 멋있다.").build();
        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder().tripId(1L).memberId(1L)
            .content("여행 계획 정말 멋있다.").build();
        given(commentService.postComment(any(Long.TYPE), any(CommentCreateRequestDTO.class))).willReturn(
            commentResponseDTO);

        //when
        ResponseEntity<ResponseDTO<CommentResponseDTO>> responseEntity = commentRestController.postComment(1L,
            commentCreateRequestDTO);

        //then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(commentService, times(1)).postComment((any(Long.TYPE)),any(CommentCreateRequestDTO.class));

    }

    @Test
    @DisplayName("patchComment()는 여행 댓글을 수정할 수 있다.")
    public void patchComment() throws Exception {
        //given
        Long commentId = 1L;
        Long memberId = 1L;
        CommentUpdateRequestDTO commentUpdateRequestDTO = CommentUpdateRequestDTO.builder()
            .content("여행 잘 다녀와.").build();
        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder().tripId(1L).memberId(1L)
            .content("여행 잘 다녀와.").build();
        given(
            commentService.patchComment(any(Long.TYPE),any(Long.TYPE),
                any(CommentUpdateRequestDTO.class))).willReturn(
            commentResponseDTO);

        //when
        ResponseEntity<ResponseDTO<CommentResponseDTO>> responseEntity = commentRestController.patchComment(
            memberId,commentId, commentUpdateRequestDTO);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(commentService, times(1)).patchComment((any(Long.TYPE)),(any(Long.TYPE)),
            any(CommentUpdateRequestDTO.class));
    }

    @Test
    @DisplayName("softDeleteComment()는 여행 댓글을 수정할 수 있다.")
    public void softDeleteComment() throws Exception {
        //given
        Long commentId = 1L;
        Long memberId = 1L;
        CommentDeleteResponseDTO commentDeleteResponseDTO = CommentDeleteResponseDTO.builder()
            .commentId(1L).build();
        given(commentService.softDeleteComment(any(Long.TYPE),any(Long.TYPE))).willReturn(
            commentDeleteResponseDTO);

        //when
        ResponseEntity<ResponseDTO<CommentDeleteResponseDTO>> responseEntity = commentRestController.softDeleteComment(memberId,
            commentId);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(commentService, times(1)).softDeleteComment((any(Long.TYPE)),(any(Long.TYPE)));

    }


}
