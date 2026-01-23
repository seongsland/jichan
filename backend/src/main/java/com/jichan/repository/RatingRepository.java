package com.jichan.repository;

import com.jichan.dto.ContactDto;
import com.jichan.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserIdAndExpertId(Long userId, Long expertId);
    void deleteByUserIdAndExpertId(Long userId, Long expertId);
    List<Rating> findByUserIdAndExpertIdIn(Long userId, List<Long> expertIds);

    @Query("SELECT new com.jichan.dto.ContactDto$RatingStatsDto(COALESCE(AVG(r.score), 0.0), COUNT(r)) FROM Rating r WHERE r.expertId = :expertId")
    ContactDto.RatingStatsDto findStatsByExpertId(@Param("expertId") Long expertId);

    @Modifying
    @Query("delete from Rating r where r.expertId = :userId or r.userId = :userId")
    void deleteAllByAllUserId(@Param("userId")Long userId);
}
