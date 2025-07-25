package com.amt.bbq.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.amt.bbq.model.entity.DiningTable;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, Long>, JpaSpecificationExecutor<DiningTable> 
					{
	// Optional<DiningTable> findByTableName(String qrCode);
	List<DiningTable> findByIsActive(boolean isActive);
	boolean existsByTableName(String tableName);	
}
