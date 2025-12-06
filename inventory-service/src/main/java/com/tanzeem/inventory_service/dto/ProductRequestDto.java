package com.tanzeem.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
	
	@NotNull(message = "Id cannot be blank")
	private Long productId;
	
	@NotBlank(message = "SKU is mandatory")
	private String sku;
}
