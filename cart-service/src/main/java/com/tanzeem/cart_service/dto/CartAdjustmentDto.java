package com.tanzeem.cart_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartAdjustmentDto {

	@NotNull(message = "Id cannot be null")
	private Long productId;
	
	@NotNull(message = "Quantity cannot be null")
	@Positive(message = "Quantity needs to be positive")
	private Long quantity;
}
