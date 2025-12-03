package com.tanzeem.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {
	
	@NotBlank(message = "Id cannot be blank")
	private Long productId;
	
	@NotBlank(message = "SKU is mandatory")
	private String sku;
}
