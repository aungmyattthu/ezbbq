package com.amt.bbq.controller.dto;

public record DiningTableSearch(
			String tableName,
			Integer capacity,
			Boolean isOccupied,
			Boolean isActive
		) {	 
}
