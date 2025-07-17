package com.amt.bbq.model.entity;

import com.amt.bbq.model.entity.consts.StaffRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StaffUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false, length = 255)
    private String passwordHash;
    
    @Column(nullable = false)
    private boolean isActive = true;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffRole role;
}
