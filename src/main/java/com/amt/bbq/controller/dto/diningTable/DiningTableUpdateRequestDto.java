package com.amt.bbq.controller.dto.diningTable;

public record DiningTableUpdateRequestDto(
		String tableName,
	    Integer capacity,
	    Boolean isActive) {

}
