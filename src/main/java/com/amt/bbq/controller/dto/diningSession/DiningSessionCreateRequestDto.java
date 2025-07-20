package com.amt.bbq.controller.dto.diningSession;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiningSessionCreateRequestDto {
	
	@NotNull
	@Min(1)
	private Long tableId;
	
	@NotNull
	@FutureOrPresent
	private LocalDateTime startTime;

	@Future
	private LocalDateTime endTime;
}
