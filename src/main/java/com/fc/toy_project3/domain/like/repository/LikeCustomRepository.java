package com.fc.toy_project3.domain.like.repository;

import com.fc.toy_project3.domain.like.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeCustomRepository {

    Page<Like> findAllByMemberIdAndPageable(long memberId, Pageable pageable);
}
