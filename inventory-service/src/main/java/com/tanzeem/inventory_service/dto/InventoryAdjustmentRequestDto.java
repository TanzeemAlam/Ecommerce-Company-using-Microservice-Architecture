package com.tanzeem.inventory_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryAdjustmentRequestDto {
	
	@NotNull(message = "Product Id cannot be null")
	private Long productId;

	@Positive(message = "Quantity should be positive")
	private Long quantity;
}
