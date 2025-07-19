package com.amt.bbq.controller.dto;

public record DiningTableSearch(
			String tableName,
			Integer minCapacity,
			Boolean isOccupied,
			Boolean isActive
		) {	 
}
