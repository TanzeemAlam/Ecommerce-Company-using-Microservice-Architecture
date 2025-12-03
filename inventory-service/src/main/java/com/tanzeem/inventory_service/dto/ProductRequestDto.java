package com.tanzeem.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
	
	@NotBlank(message = "Id cannot be blank")
	private Long productId;
	
	@NotBlank(message = "SKU is mandatory")
	private String sku;
}
