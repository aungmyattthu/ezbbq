package com.amt.bbq.model.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.amt.bbq.model.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByIsActive(boolean isActive);
    
    @Query("SELECT s FROM Session s WHERE s.isActive = true AND s.endTime IS NULL AND s.startTime <= :cutoff")
    List<Session> findExpiredSessions(LocalDateTime cutoff);
    
    Optional<Session> findByTableIdAndIsActive(Long tableId, boolean isActive);
}