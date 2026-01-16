package com.jichan.repository;

import com.jichan.entity.SpecialtyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyDetailRepository extends JpaRepository<SpecialtyDetail, Long> {
}




