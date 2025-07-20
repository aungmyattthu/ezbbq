package com.amt.bbq.service;

import static com.amt.bbq.utils.EntityOperations.safeCall;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.amt.bbq.controller.dto.diningSession.DiningSessionCreateRequestDto;
import com.amt.bbq.controller.dto.diningSession.DiningSessionResponseDto;
import com.amt.bbq.controller.dto.diningSession.DiningSessionSearch;
import com.amt.bbq.controller.dto.diningSession.DiningSessionUpdateRequestDto;
import com.amt.bbq.model.entity.DiningSession;
import com.amt.bbq.model.entity.DiningTable;
import com.amt.bbq.model.entity.consts.DiningConstants;
import com.amt.bbq.model.entity.consts.MessageConstants;
import com.amt.bbq.model.repo.DiningSessionRepository;
import com.amt.bbq.model.repo.DiningTableRepository;
import com.amt.bbq.utils.exceptions.BusinessRuleException;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

// Session CRUD can be made by all staff user
@Service
@RequiredArgsConstructor
public class DiningSessionService {

	private final DiningSessionRepository sessionRepo;
	
	private final DiningTableRepository tableRepo;
	
	public DiningSessionResponseDto getSession(Long id) {
		
		return DiningSessionResponseDto.mapToDto(findSessionById(id));		
	}

	public List<DiningSessionResponseDto> searchTables(DiningSessionSearch form, Pageable pagable) {
		Specification<DiningSession> spec = buildSearchSpecification(form);
		Page<DiningSession> tables = sessionRepo.findAll(spec, pagable);

		return tables.stream()
				.map(DiningSessionResponseDto::mapToDto)
				.toList();
	}

	private Specification<DiningSession> buildSearchSpecification(DiningSessionSearch form) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();

		   
	        Join<DiningSession, DiningTable> tableJoin = root.join("table", JoinType.INNER);

	        // Filter by table ID
	        if(form.tableId() != null) {
	            predicates.add(cb.equal(root.get("table").get("id"), form.tableId()));
	        }

	        // Filter by table name (search in joined DiningTable)
	        if (form.tableName() != null) {
	            predicates.add(cb.like(
	                cb.lower(tableJoin.get("tableName")), 
	                "%" + form.tableName().toLowerCase() + "%"
	            ));
	        }

	        // Filter by start time (after or equal)
	        if (form.startTime() != null) {
	            predicates.add(cb.greaterThanOrEqualTo(
	                root.get("startTime"), 
	                form.startTime()
	            ));
	        }

	        // Filter by end time (before or equal)
	        if (form.endTime() != null) {
	            predicates.add(cb.lessThanOrEqualTo(
	                root.get("endTime"), 
	                form.endTime()
	            ));
	        }

	        // Filter by active status
	        if (form.isActive() != null) {
	            predicates.add(cb.equal(
	                root.get("isActive"), 
	                form.isActive()
	            ));
	        }


			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	@Transactional
	public DiningSessionResponseDto createSession(DiningSessionCreateRequestDto request) {
		
		var table = findTableById(request.getTableId());
		
		// check table has active session
		if(table.getSessions()
				.stream()
				.anyMatch(session -> session.isActive())) {
			throw new BusinessRuleException("Cannot create session. Table have active one");
		}
		// session save
		var session = new DiningSession();
		session.setTable(table);
		session.setActive(true);
		session.setStartTime(request.getStartTime());
		if(null == request.getEndTime()) {
			session.setEndTime(request.getStartTime().plusHours(DiningConstants.SESSION_HOUR));
		}
		else {
			session.setEndTime(request.getEndTime());
		}		
		
		return DiningSessionResponseDto.mapToDto(sessionRepo.save(session));
	}

	@Transactional
	public DiningSessionResponseDto updateSession(Long id, DiningSessionUpdateRequestDto request) {
		var session = findSessionById(id);
		
		// changes should be made at active session. 
		if(!session.isActive()) {
			throw new BusinessRuleException("Session over");
		}
		if(request.tableId() != null) {
			session.setTable(findTableById(request.tableId()));
		}
		if(request.startTime() != null) {
			session.setStartTime(request.startTime());
		}
		if(request.endTime() != null) {
			session.setEndTime(request.endTime());
		}		
		
		return DiningSessionResponseDto.mapToDto(sessionRepo.save(session));
	}

	@Transactional
	public DiningSessionResponseDto endSession(Long id) {
		var session = findSessionById(id);
		
		if(!session.isActive()) {
			throw new BusinessRuleException(MessageConstants.SES_ERR_MSG001);
		}
		session.setActive(false);
		return DiningSessionResponseDto.mapToDto(sessionRepo.save(session));
	}

	public void delete(Long id) {		
		var session = safeCall(sessionRepo.findById(id), "Session", "Session Id", id);
		sessionRepo.delete(session);
	}
	
	public DiningTable findTableById(Long id) {
		return safeCall(tableRepo.findById(id), "Dining Table", "table id", id);
	}
	
	public DiningSession findSessionById(Long id) {
		return safeCall(sessionRepo.findById(id), "Session", "Session Id", id);
	}
	
}
