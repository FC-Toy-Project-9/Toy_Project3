package com.fc.toy_project3.domain.comment.exception;

public class CommentDeletedException extends RuntimeException {

    public CommentDeletedException() {
        super("삭제된 댓글입니다.");
    }
}
