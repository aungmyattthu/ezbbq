package com.amt.bbq.controller.dto.diningTable;

public record DiningTableSearch(
			String tableName,
			Integer minCapacity,
			Boolean isOccupied,
			Boolean isActive
		) {	 
}
