package com.fc.toy_project3.domain.comment.repository;

import com.fc.toy_project3.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
