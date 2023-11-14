package com.fc.toy_project3.domain.comment.exception;

public class CommentMemberNotFoundException extends RuntimeException {

    public CommentMemberNotFoundException() {
        super("댓글을 작성한 회원이 아닙니다.");
    }
}
