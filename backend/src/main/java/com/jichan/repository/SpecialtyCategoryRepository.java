package com.jichan.repository;

import com.jichan.entity.SpecialtyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyCategoryRepository extends JpaRepository<SpecialtyCategory, Long> {
}




