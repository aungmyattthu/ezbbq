package com.amt.bbq.service;

import static com.amt.bbq.utils.EntityOperations.safeCall;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.amt.bbq.controller.dto.diningTable.DiningTableCreateRequestDto;
import com.amt.bbq.controller.dto.diningTable.DiningTableResponseDto;
import com.amt.bbq.controller.dto.diningTable.DiningTableSearch;
import com.amt.bbq.controller.dto.diningTable.DiningTableUpdateRequestDto;
import com.amt.bbq.model.entity.DiningTable;
import com.amt.bbq.model.entity.consts.MessageConstants;
import com.amt.bbq.model.repo.DiningSessionRepository;
import com.amt.bbq.model.repo.DiningTableRepository;
import com.amt.bbq.utils.exceptions.BusinessRuleException;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

// table CRUD could be made only by admin
@Service
@RequiredArgsConstructor
public class DiningTableService {

	private final DiningTableRepository tableRepo;
	
	private final DiningSessionRepository sessionRepo;

	public List<DiningTableResponseDto> getAllTables() {

		return tableRepo.findAll().stream().map(DiningTableResponseDto::mapToDto).toList();
	}

	public List<DiningTableResponseDto> getTablesByActive(Boolean active) {

		return tableRepo.findByIsActive(active).stream().map(DiningTableResponseDto::mapToDto).toList();

	}

	public DiningTableResponseDto getTableById(Long id) {

		return DiningTableResponseDto.mapToDto(findTableById(id));
	}

	public List<DiningTableResponseDto> searchTables(DiningTableSearch form, Pageable pageable) {
		Specification<DiningTable> spec = buildSearchSpecification(form);
		Page<DiningTable> tables = tableRepo.findAll(spec, pageable);

		if(form.isOccupied() != null) {
			return tables.stream()
				  .filter(table -> table.isOccupied() == form.isOccupied())
				  .map(DiningTableResponseDto::mapToDto)
				  .toList();		            
		}
		
		return tables.stream()
				.map(DiningTableResponseDto::mapToDto)
				.toList();
	}

	private Specification<DiningTable> buildSearchSpecification(DiningTableSearch form) {

		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();
			if (form.tableName() != null) {
				predicates.add(cb.like(cb.lower(root.get("tableName")), "%" + form.tableName().toLowerCase() + "%"));
			}

			if (form.minCapacity() != null) {
				predicates.add(cb.ge(root.get("capacity"), form.minCapacity()));
			}
			if (form.isActive() != null) {
				predicates.add(cb.equal(root.get("isActive"), form.isActive()));
			}
//			if (form.isOccupied() != null) {
//				predicates.add(cb.equal(root.get("isOccupied"), form.isOccupied()));
//			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	@Transactional
	public DiningTableResponseDto create(DiningTableCreateRequestDto request) {
		if (tableRepo.existsByTableName(request.getTableName())) {
			throw new BusinessRuleException("Table name '" + request.getTableName() + "' already exists");
		}
		var table = new DiningTable();
		table.setTableName(request.getTableName());
		table.setCapacity(request.getCapacity());
		table.setActive(request.isActive());

		var savedtable = tableRepo.save(table);

		return DiningTableResponseDto.mapToDto(savedtable);
	}
	
	@Transactional
	public DiningTableResponseDto updateTable(Long id, DiningTableUpdateRequestDto request) {
	    var table = findTableById(id);
	    
	    if (!table.getTableName().equals(request.tableName()) && 
	    		tableRepo.existsByTableName(request.tableName())) {
	        throw new BusinessRuleException("Table name '" + request.tableName() + "' already exists");
	    }

	    if (request.tableName() != null) {
	    	table.setTableName(request.tableName());
	    }
	    
	    if (request.capacity() != null) {
	    	table.setCapacity(request.capacity());
	    }
	    
	    if (request.isActive() != null) {
	        table.setActive(request.isActive());
	    }

	    var updatedTable = tableRepo.save(table);
	    return DiningTableResponseDto.mapToDto(updatedTable);
	}
	

	public void delete(Long id) {
		var table = findTableById(id);
		tableRepo.delete(table);

	}
	
	@Transactional
	public DiningTableResponseDto endSession(Long id) {
		var table = findTableById(id);
		
		// check active session
		if(!table.isOccupied()) {
			// if not active session, throw
			throw new BusinessRuleException(MessageConstants.TBL_ERR_MSG001);
		}	
		
		// if active session, end session
		var session = table.getActiveSession();
		session.setActive(false);
		sessionRepo.save(session);
		return DiningTableResponseDto.mapToDto(table);
	}
	
	public DiningTable findTableById(Long id) {
		return safeCall(tableRepo.findById(id), "Dining Table", "table id", id);
	}

}
