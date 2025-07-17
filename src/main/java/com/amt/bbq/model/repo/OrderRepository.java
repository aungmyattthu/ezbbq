package com.amt.bbq.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amt.bbq.model.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
