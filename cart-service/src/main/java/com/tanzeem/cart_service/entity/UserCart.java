package com.tanzeem.cart_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartId;
	
	@NotNull(message = "User id cannot be null")
	private Long userId;
	
	private String status;										//ACTIVE, ORDERED, ABANDONED
	
	
	private double totalAmount;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
