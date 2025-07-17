package com.amt.bbq.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.amt.bbq.model.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryIdAndCurrentAvailability(Long categoryId, boolean available);
    
    @Modifying
    @Query("UPDATE MenuItem m SET m.dailyQuantityRemaining = m.maxDailyQuantity, m.currentAvailability = true")
    void resetDailyQuantities();
}