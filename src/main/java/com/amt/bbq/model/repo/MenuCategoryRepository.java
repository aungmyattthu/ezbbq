package com.amt.bbq.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amt.bbq.model.entity.MenuCategory;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long>{

}
