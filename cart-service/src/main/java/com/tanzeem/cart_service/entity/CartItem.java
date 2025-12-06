package com.tanzeem.cart_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;
	
	@NotNull(message = "Cart Id cannot be null")
	private Long cartId;
	
	@NotNull(message = "Product Id cannot be null")
	private Long productId;
	
	@Positive(message = "Quantity should be positive")
	private Long quantity;
	
	@Positive(message = "Price should be positive")
	private Double pricePerUnit;
	
	private LocalDateTime addedAt;
	private LocalDateTime updatedAt;
}
