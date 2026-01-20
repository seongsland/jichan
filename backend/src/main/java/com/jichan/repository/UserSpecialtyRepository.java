package com.jichan.repository;

import com.jichan.entity.UserSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSpecialtyRepository extends JpaRepository<UserSpecialty, Long> {
    List<UserSpecialty> findByUserId(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("delete from UserSpecialty us where us.userId = :userId")
    void deleteAllByUserId(Long userId);
}
