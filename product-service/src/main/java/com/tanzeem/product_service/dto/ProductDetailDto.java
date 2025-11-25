package com.tanzeem.product_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto {
	
	@NotBlank(message = "Id cannot be blank")
	private Long productId;
	
	@NotBlank(message = "Price is mandatory")
	@PositiveOrZero(message = "Price should be positive or zero")
	private String price;
	
	@NotBlank(message = "Size is mandatory")
	@Positive(message = "Size should be positive")
	private String size;
	
	@NotBlank(message = "Design is mandatory")
	private String design;
}
