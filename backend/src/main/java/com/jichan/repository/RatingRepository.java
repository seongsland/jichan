package com.jichan.repository;

import com.jichan.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("SELECT COALESCE(AVG(r.score), 0.0) FROM Rating r WHERE r.expertId = :expertId")
    double findStatsByExpertId(@Param("expertId") Long expertId);
}
