package com.tanzeem.cart_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
	@NotNull(message = "Product Id cannot be null")
	private Long productId;
	
	@Positive(message = "Quantity should be positive")
	private Long quantity;
	
	@PositiveOrZero(message = "Price should be positive")
	private Double pricePerUnit;
}
