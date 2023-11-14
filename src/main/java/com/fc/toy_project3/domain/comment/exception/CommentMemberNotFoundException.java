package com.fc.toy_project3.domain.comment.exception;

public class CommentMemberNotFoundException extends RuntimeException {

    public CommentMemberNotFoundException() {
        super("해당 회원의 댓글을 찾을 수 없습니다.");
    }
}
