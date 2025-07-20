package com.amt.bbq.controller.dto.diningSession;

import java.time.LocalDateTime;

public record DiningSessionSearch(
			Integer tableId,
			String tableName,
			LocalDateTime startTime,
			LocalDateTime endTime,
			Boolean isActive,
			Boolean hasPendingOrder
		) {

}
