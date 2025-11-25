package com.tanzeem.product_detail_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponseDto {
	
	@NotNull(message = "Price is mandatory")
	@PositiveOrZero(message = "Price should be positive or zero")
	private double price;
	
	@NotBlank(message = "Size is mandatory")
	private String size;
	
	@NotBlank(message = "Design is mandatory")
	private String design;
	
	@NotNull(message = "Product Id is mandatory")
	private Long productId;
}
