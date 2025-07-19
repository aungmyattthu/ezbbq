package com.amt.bbq.controller.dto;

import com.amt.bbq.model.entity.DiningTable;

public record DiningTableResponseDto(
		Long id, 
		String tableName, 
		int capacity, 
		boolean isOccupied,
		boolean isActive) {

	public static DiningTableResponseDto mapToDto(DiningTable entity) {

		return new DiningTableResponseDto(
				entity.getId(), 
				entity.getTableName(), 
				entity.getCapacity(), 
				entity.isOccupied(), 
				entity.isActive());
	}

}
