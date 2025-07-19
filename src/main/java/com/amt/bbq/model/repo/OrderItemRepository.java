package com.amt.bbq.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amt.bbq.model.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
