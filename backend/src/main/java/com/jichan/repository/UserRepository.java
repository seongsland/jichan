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

    @Query("SELECT u FROM User u LEFT JOIN Rating r ON u.id = r.expertId WHERE u.isVisible = true GROUP BY u.id ORDER BY AVG(r.score) DESC")
    Slice<User> findByIsVisibleTrueOrderByRatingDesc(Pageable pageable);

    @Query("SELECT u FROM User u JOIN UserSpecialty us ON u.id = us.userId WHERE u.isVisible = true GROUP BY u.id ORDER BY MIN(us.hourlyRate) ASC")
    Slice<User> findByIsVisibleTrueOrderByPriceAsc(Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN Rating r ON u.id = r.expertId WHERE u.isVisible = true AND u.id IN (SELECT us.userId FROM UserSpecialty us WHERE us.specialtyDetailId = :specialtyId) GROUP BY u.id ORDER BY AVG(r.score) DESC")
    Slice<User> findBySpecialtyIdAndIsVisibleTrueOrderByRatingDesc(@Param("specialtyId") Long specialtyId, Pageable pageable);

    @Query("SELECT u FROM User u JOIN UserSpecialty us ON u.id = us.userId WHERE u.isVisible = true AND us.specialtyDetailId = :specialtyId ORDER BY us.hourlyRate ASC")
    Slice<User> findBySpecialtyIdAndIsVisibleTrueOrderByPriceAsc(@Param("specialtyId") Long specialtyId, Pageable pageable);
}
