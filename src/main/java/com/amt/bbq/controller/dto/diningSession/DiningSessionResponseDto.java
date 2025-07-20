package com.amt.bbq.controller.dto.diningSession;

import java.time.LocalDateTime;

import com.amt.bbq.model.entity.DiningSession;
import com.amt.bbq.model.entity.consts.OrderStatus;

public record DiningSessionResponseDto(
			Long id,
	        Long tableId,
	        String tableName,
	        LocalDateTime startTime,
	        LocalDateTime endTime,
	        boolean isActive,
	        int totalOrderCount,
	        int pendingOrderCount,
	        int deliveredOrderCount
		) {
	public static DiningSessionResponseDto mapToDto(DiningSession entity) {
		var builder = new DiningSessionResponseDtoBuilder();
		builder.id(entity.getId());
		builder.tableId(entity.getTable().getId());
		builder.tableName(entity.getTable().getTableName());
		builder.startTime(entity.getStartTime());
		builder.endTime(entity.getEndTime());
		builder.isActive(entity.isActive());
		builder.totalOrderCount(entity.getOrders().size());
		builder.pendingOrderCount(entity.getOrders().stream()
					.filter(order -> order.getStatus() == OrderStatus.PENDING).toList().size());	
		builder.deliveredOrderCount(entity.getOrders().stream()
				.filter(order -> order.getStatus() == OrderStatus.DELIVERED).toList().size());
		
		return builder.build();
	}

	 public static class DiningSessionResponseDtoBuilder {
	        private Long id;
	        private Long tableId;
	        private String tableName;
	        private LocalDateTime startTime;
	        private LocalDateTime endTime;
	        private boolean isActive;
	        private int totalOrderCount;
	        private int pendingOrderCount;
	        private int deliveredOrderCount;

	        public DiningSessionResponseDtoBuilder id(Long id) {
	            this.id = id;
	            return this;
	        }

	        public DiningSessionResponseDtoBuilder tableId(Long tableId) {
	            this.tableId = tableId;
	            return this;
	        }

	        public DiningSessionResponseDtoBuilder tableName(String tableName) {
	            this.tableName = tableName;
	            return this;
	        }

	        public DiningSessionResponseDtoBuilder startTime(LocalDateTime startTime) {
	            this.startTime = startTime;
	            return this;
	        }

	        public DiningSessionResponseDtoBuilder endTime(LocalDateTime endTime) {
	            this.endTime = endTime;
	            return this;
	        }

	        public DiningSessionResponseDtoBuilder isActive(boolean isActive) {
	            this.isActive = isActive;
	            return this;
	        }

	        public DiningSessionResponseDtoBuilder totalOrderCount(int totalOrderCount) {
	            this.totalOrderCount = totalOrderCount;
	            return this;
	        }

	        public DiningSessionResponseDtoBuilder pendingOrderCount(int pendingOrderCount) {
	            this.pendingOrderCount = pendingOrderCount;
	            return this;
	        }

	        public DiningSessionResponseDtoBuilder deliveredOrderCount(int deliveredOrderCount) {
	            this.deliveredOrderCount = deliveredOrderCount;
	            return this;
	        }

	        public DiningSessionResponseDto build() {
	            return new DiningSessionResponseDto(
	                id,
	                tableId,
	                tableName,
	                startTime,
	                endTime,
	                isActive,
	                totalOrderCount,
	                pendingOrderCount,
	                deliveredOrderCount
	            );
	        }
	    }
}
