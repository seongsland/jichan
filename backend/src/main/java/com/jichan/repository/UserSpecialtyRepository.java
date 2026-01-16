package com.jichan.repository;

import com.jichan.entity.UserSpecialty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSpecialtyRepository extends JpaRepository<UserSpecialty, Long> {
    List<UserSpecialty> findByUserId(Long userId);

    @Query("SELECT us FROM UserSpecialty us WHERE us.specialtyDetail.id = :specialtyId")
    List<UserSpecialty> findUsersBySpecialtyId(@Param("specialtyId") Long specialtyId, Pageable pageable);
}
