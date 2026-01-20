package com.jichan.repository;

import com.jichan.entity.ContactLog;
import com.jichan.entity.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactLogRepository extends JpaRepository<ContactLog, Long> {
    Optional<ContactLog> findByViewerIdAndExpertIdAndContactType(Long viewer, Long expertId, ContactType contactType);
    List<ContactLog> findByViewerId(Long viewerId);
    List<ContactLog> findByViewerIdAndExpertIdIn(Long viewerId, List<Long> expertIds);
    
    @Query("SELECT c.expertId, COUNT(c) FROM ContactLog c WHERE c.expertId IN :expertIds GROUP BY c.expertId")
    List<Object[]> countByExpertIdIn(@Param("expertIds") List<Long> expertIds);
}
