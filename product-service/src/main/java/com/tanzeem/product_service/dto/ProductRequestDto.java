package com.tanzeem.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
	
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	private String description;
	
	@NotBlank(message = "Product sku is mandatory")
	private String sku;
	
	@NotBlank(message = "Product category is mandatory")
	private String category;
}
