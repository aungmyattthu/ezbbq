package com.amt.bbq.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amt.bbq.controller.dto.DiningTableCreateRequestDto;
import com.amt.bbq.controller.dto.DiningTableResponseDto;
import com.amt.bbq.controller.dto.DiningTableSearch;
import com.amt.bbq.controller.dto.UpdateDiningTableRequestDto;
import com.amt.bbq.service.DiningTableService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/tables")
public class DiningTableController {
	private final DiningTableService service;
	// api/tables/
	@GetMapping
	public ResponseEntity<List<DiningTableResponseDto>> getAllTables(
			@RequestParam(required = false) Boolean active
			){
		List<DiningTableResponseDto> tables = new ArrayList<DiningTableResponseDto>();
		
		if(null == active) {
			tables = service.getAllTables();
		}
		else {
			tables = service.getTablesByActive(active);
		}
		
		return ResponseEntity.ok(tables);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DiningTableResponseDto> getTableById(@PathVariable Long id){
		return ResponseEntity.ok(service.getTableById(id));
	}
	
	// search
	@GetMapping("/search")
	public ResponseEntity<List<DiningTableResponseDto>> search(
								@Validated DiningTableSearch form,
								Pageable pagable){
		return ResponseEntity.ok(service.searchTables(form, pagable));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DiningTableResponseDto createTable(
			@Valid @RequestBody DiningTableCreateRequestDto request) {
		return service.create(request);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<DiningTableResponseDto> updateTable(
	    @PathVariable Long id,
	    @Valid @RequestBody UpdateDiningTableRequestDto request
	) {
	    DiningTableResponseDto updatedTable = service.updateTable(id, request);
	    return ResponseEntity.ok(updatedTable);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTable(@PathVariable Long id) {
		service.delete(id);
	}

}
