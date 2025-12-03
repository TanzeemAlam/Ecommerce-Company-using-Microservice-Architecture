package com.tanzeem.inventory_service.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {	
	
	@Positive(message = "Quantity should be positive")
	private Long quantity;
}
