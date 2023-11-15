package com.fc.toy_project3.domain.like.repository;

import com.fc.toy_project3.domain.like.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeCustomRepository {
    Optional<Like> findByMemberIdAndTripId(Long memberId, Long tripId);
}