package com.jichan.repository;

import com.jichan.entity.ContactLog;
import com.jichan.entity.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactLogRepository extends JpaRepository<ContactLog, Long>, ContactLogRepositoryCustom {
    Optional<ContactLog> findByViewerIdAndExpertIdAndContactType(Long viewer, Long expertId, ContactType contactType);

    List<ContactLog> findByViewerIdAndExpertIdIn(Long viewerId, List<Long> expertIds);

    void deleteByViewerIdAndExpertId(Long viewerId, Long expertId);

    @Modifying
    @Query("delete from ContactLog cl where cl.viewerId = :userId or cl.expertId = :userId")
    void deleteAllByAllUserId(@Param("userId") Long requesterId);
}
