package com.jichan.repository;

import com.jichan.entity.UserSpecialty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSpecialtyRepository extends JpaRepository<UserSpecialty, Long> {
    List<UserSpecialty> findByUserId(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("delete from UserSpecialty us where us.userId = :userId")
    void deleteAllByUserId(Long userId);

    @Query("SELECT us FROM UserSpecialty us WHERE us.specialtyDetailId = :specialtyId")
    List<UserSpecialty> findUsersBySpecialtyId(@Param("specialtyId") Long specialtyId, Pageable pageable);
}
