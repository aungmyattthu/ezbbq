package com.amt.bbq.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.amt.bbq.controller.dto.diningSession.DiningSessionCreateRequestDto;
import com.amt.bbq.controller.dto.diningSession.DiningSessionResponseDto;
import com.amt.bbq.controller.dto.diningSession.DiningSessionSearch;
import com.amt.bbq.controller.dto.diningSession.DiningSessionUpdateRequestDto;
import com.amt.bbq.service.DiningSessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sessions")
public class DiningSessionController {
	private final DiningSessionService service;
	
    @GetMapping("/{id}")
    public ResponseEntity<DiningSessionResponseDto> getSession(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSession(id));
    }
    
    @GetMapping("/search")
	public ResponseEntity<List<DiningSessionResponseDto>> search(
								@Validated DiningSessionSearch form,
								Pageable pagable){
		return ResponseEntity.ok(service.searchTables(form, pagable));
	}
    
    @PostMapping
    public ResponseEntity<DiningSessionResponseDto> createSession(@RequestBody @Valid DiningSessionCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createSession(request));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<DiningSessionResponseDto> updateSession(@PathVariable Long id,
    		 @Valid @RequestBody DiningSessionUpdateRequestDto request) {
        return ResponseEntity.ok(service.updateSession(id, request));
    }
    
    @PatchMapping("/{id}/end")
    public ResponseEntity<DiningSessionResponseDto> endSession(@PathVariable Long id) {
        return ResponseEntity.ok(service.endSession(id));
    }

    // in a case of mistake
    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id){
    	service.delete(id);
    }
   
}
