package com.amt.bbq.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "bbqTable")
public class DiningTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String tableName;
	
	private int capacity;
	
	private boolean isActive;

	@Column(updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DiningSession> sessions = new ArrayList<>();
	
	public boolean isOccupied() {
        return this.sessions.stream()
            .anyMatch(session -> session.isActive());
    }
	
	public DiningSession getActiveSession() {
		return this.sessions.stream()
	            .filter(session -> session.isActive())
	            .findFirst().orElse(new DiningSession());
	}
}
