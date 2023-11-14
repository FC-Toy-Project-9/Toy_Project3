package com.fc.toy_project3.domain.like.repository;

import com.fc.toy_project3.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByMemberIdAndTripId(Long memberId, Long tripId);
}