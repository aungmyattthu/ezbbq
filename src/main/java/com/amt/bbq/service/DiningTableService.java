package com.amt.bbq.service;

import static com.amt.bbq.utils.EntityOperations.safeCall;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.amt.bbq.controller.dto.DiningTableCreateRequestDto;
import com.amt.bbq.controller.dto.DiningTableResponseDto;
import com.amt.bbq.controller.dto.DiningTableSearch;
import com.amt.bbq.controller.dto.UpdateDiningTableRequestDto;
import com.amt.bbq.model.entity.DiningTable;
import com.amt.bbq.model.repo.DiningTableRepository;
import com.amt.bbq.utils.exceptions.BusinessRuleException;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiningTableService {

	private final DiningTableRepository repo;

	public List<DiningTableResponseDto> getAllTables() {

		return repo.findAll().stream().map(DiningTableResponseDto::mapToDto).toList();
	}

	public List<DiningTableResponseDto> getTablesByActive(Boolean active) {

		return repo.findByIsActive(active).stream().map(DiningTableResponseDto::mapToDto).toList();

	}

	public DiningTableResponseDto getTableById(Long id) {

		return safeCall(repo.findById(id).map(DiningTableResponseDto::mapToDto), "Dining Table", "table id", id);
	}

	public List<DiningTableResponseDto> searchTables(DiningTableSearch form, Pageable pageable) {
		Specification<DiningTable> spec = buildSearchSpecification(form);
		Page<DiningTable> tables = repo.findAll(spec, pageable);

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
			if (form.isOccupied() != null) {
				predicates.add(cb.equal(root.get("isOccupied"), form.isOccupied()));
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

		var savedtable = repo.save(table);

		return DiningTableResponseDto.mapToDto(savedtable);
	}
	
	@Transactional
	public DiningTableResponseDto updateTable(Long id, UpdateDiningTableRequestDto request) {
	    DiningTable table = safeCall(repo.findById(id), "Dining Table", "table id", id);
	    
	    if (!table.getTableName().equals(request.tableName()) && 
	    		repo.existsByTableName(request.tableName())) {
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

	    DiningTable updatedTable = repo.save(table);
	    return DiningTableResponseDto.mapToDto(updatedTable);
	}
	

	public void delete(Long id) {
		DiningTable table = safeCall(repo.findById(id), "Dining Table", "table id", id);
		repo.delete(table);

	}

}
