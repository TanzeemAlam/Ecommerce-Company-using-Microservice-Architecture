package com.tanzeem.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDto {
	
	@NotBlank
	private Long productId;
	
	@NotBlank
	private String sku;
	
	@PositiveOrZero
	private Long quantity;
	
	@PositiveOrZero
	private Long reserved;

	@PositiveOrZero
	private Long available;
}
