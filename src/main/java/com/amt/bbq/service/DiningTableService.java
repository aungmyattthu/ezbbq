package com.amt.bbq.service;

import static com.amt.bbq.utils.EntityOperations.safeCall;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.amt.bbq.controller.dto.DiningTableCreateRequestDto;
import com.amt.bbq.controller.dto.DiningTableResponseDto;
import com.amt.bbq.controller.dto.DiningTableSearch;
import com.amt.bbq.model.entity.DiningTable;
import com.amt.bbq.model.repo.DiningTableRepository;
import com.amt.bbq.utils.exceptions.BusinessRuleException;
import com.amt.bbq.utils.exceptions.ResourceNotFoundException;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiningTableService {

	private final DiningTableRepository repo;
	
	public List<DiningTableResponseDto> getAllTables() {
		
		return repo.findAll().stream().map(DiningTableResponseDto::from).toList();
	}

	public List<DiningTableResponseDto> getTablesByActive(Boolean active) {
		
		return repo.findByIsActive(active).stream().map(DiningTableResponseDto::from).toList();
				
	}

	public DiningTableResponseDto getTableById(Long id) {
		
		return safeCall(
				repo.findById(id).map(DiningTableResponseDto::from),
				"Dining Table", 
				"table id", 
				id);				
	}

//	public List<DiningTableResponseDto> search(DiningTableSearch form, Pageable pagable) {
//		Specification<DiningTable> spec = buildSearchSpecification(form);
//		///Page<DiningTable> tables = repo.findAll(spec, pagable);
//		return tables.stream().map(DiningTableResponseDto::from).toList();
//	}

	private Specification<DiningTable> buildSearchSpecification(DiningTableSearch form) {
		
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(form.tableName() != null) {
				predicates.add(cb.like(
						cb.lower(root.get("tableName")), 
						"%" +form.tableName().toLowerCase()+"%"));
			}
			
			if(form.capacity() != null) {
				predicates.add(cb.equal(
						root.get("capacity"), 
						form.capacity()));
			}
			if(form.isActive() != null) {
				predicates.add(cb.equal(
						root.get("isActive"), 
						form.isActive()));
			}
			if(form.isOccupied() != null) {
				predicates.add(cb.equal(
						root.get("isOccupied"), 
						form.isOccupied()));
			}
			
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	@Transactional
	public DiningTableResponseDto create(DiningTableCreateRequestDto request) {
		if (repo.existsByTableName(request.getTableName())) {
			throw new BusinessRuleException("Table name '" + request.getTableName() + "' already exists");
		}
		var table = new DiningTable();
		table.setTableName(request.getTableName());
		table.setCapacity(request.getCapacity());
		table.setActive(request.isActive());
		table.setOccupied(false); // default false at creation
		 
		var savedtable = repo.save(table);
		
		return DiningTableResponseDto.from(savedtable);
	}

	public void delete(Long id, boolean force) {
		 DiningTable table = repo.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));

//	        if (!force) {
//	            // Check for active sessions
//	            boolean hasActiveSessions = repo.existsByTableAndIsActive(table, true);
//	            if (hasActiveSessions) {
//	                throw new BusinessRuleException(
//	                    "Cannot delete table with active sessions. Use force=true to override");
//	            }
//	        }

		repo.delete(table);
		
	}

}
