package com.jichan.repository;

import com.jichan.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Slice<User> findByIsVisibleTrue(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isVisible = true AND u.id IN (SELECT us.userId FROM UserSpecialty us WHERE us.specialtyDetailId = :specialtyId)")
    Slice<User> findBySpecialtyIdAndIsVisibleTrue(@Param("specialtyId") Long specialtyId, Pageable pageable);

    Slice<User> findByIsVisibleTrueOrderByAverageRatingDesc(Pageable pageable);

    Slice<User> findByIsVisibleTrueOrderByMinHourlyRateAsc(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isVisible = true AND u.id IN (SELECT us.userId FROM UserSpecialty us WHERE us.specialtyDetailId = :specialtyId) ORDER BY u.averageRating DESC")
    Slice<User> findBySpecialtyIdAndIsVisibleTrueOrderByAverageRatingDesc(@Param("specialtyId") Long specialtyId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isVisible = true AND u.id IN (SELECT us.userId FROM UserSpecialty us WHERE us.specialtyDetailId = :specialtyId) ORDER BY u.minHourlyRate ASC")
    Slice<User> findBySpecialtyIdAndIsVisibleTrueOrderByMinHourlyRateAsc(@Param("specialtyId") Long specialtyId, Pageable pageable);
}
