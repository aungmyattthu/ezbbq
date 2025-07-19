package com.amt.bbq.controller.dto;

public record UpdateDiningTableRequestDto(
		String tableName,
	    Integer capacity,
	    Boolean isActive) {

}
