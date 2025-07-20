package com.amt.bbq.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.amt.bbq.model.entity.DiningSession;

@Repository
public interface DiningSessionRepository extends JpaRepository<DiningSession, Long>, JpaSpecificationExecutor<DiningSession> {
    List<DiningSession> findByIsActive(boolean isActive);    
    Optional<DiningSession> findByTableIdAndIsActive(Long tableId, boolean isActive);
}