package com.amt.bbq.controller.dto.diningSession;

import java.time.LocalDateTime;

public record DiningSessionUpdateRequestDto(
			Long tableId,
			LocalDateTime startTime,
			LocalDateTime endTime
		) {

}
