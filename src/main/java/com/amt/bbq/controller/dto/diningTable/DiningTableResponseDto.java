package com.amt.bbq.controller.dto.diningTable;

import com.amt.bbq.controller.dto.diningSession.DiningSessionResponseDto;
import com.amt.bbq.model.entity.DiningTable;

public record DiningTableResponseDto(
		Long id, 
		String tableName, 
		int capacity, 
		boolean isOccupied,
		boolean isActive,
		DiningSessionResponseDto activeSession) {

	public static DiningTableResponseDto mapToDto(DiningTable entity) {

		var activeSession = entity.isOccupied() ?  DiningSessionResponseDto.mapToDto(entity.getActiveSession())
				: null;
		return new DiningTableResponseDto(
				entity.getId(), 
				entity.getTableName(), 
				entity.getCapacity(), 
				entity.isOccupied(), 
				entity.isActive(),
				activeSession
				);
	}

}
