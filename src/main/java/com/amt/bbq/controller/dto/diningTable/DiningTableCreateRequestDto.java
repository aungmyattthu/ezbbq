package com.amt.bbq.controller.dto.diningTable;

import com.amt.bbq.model.entity.DiningTable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiningTableCreateRequestDto{	
	
	@NotBlank
	private String tableName;
	@Min(1)
	int capacity;
	boolean isOccupied = false;
	boolean isActive = true;
	
	public static DiningTableCreateRequestDto dto(DiningTable table) {
		return new DiningTableCreateRequestDto(
					table.getTableName(), 
					table.getCapacity(), 
					table.isOccupied(),
					table.isActive()
				);
	}
}
