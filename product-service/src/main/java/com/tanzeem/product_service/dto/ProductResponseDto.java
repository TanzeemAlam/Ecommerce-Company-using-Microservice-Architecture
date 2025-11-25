package com.tanzeem.product_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
	
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	private String description;
	
	@NotBlank(message = "Product sku is mandatory")
	private String sku;
	
	@NotBlank(message = "Product category is mandatory")
	private String category;
	
	@NotBlank(message = "Price is mandatory")
	@PositiveOrZero(message = "Price should be positive or zero")
	private String price;
	
	@NotBlank(message = "Size is mandatory")
	@Positive(message = "Size should be positive")
	private String size;
	
	@NotBlank(message = "Design is mandatory")
	private String design;
}
