package com.jichan.repository;

import com.jichan.entity.UserSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSpecialtyRepository extends JpaRepository<UserSpecialty, Long> {
    List<UserSpecialty> findByUserId(Long userId);
    List<UserSpecialty> findByUserIdIn(List<Long> userIds);

    @Modifying
    @Query("delete from UserSpecialty us where us.userId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
