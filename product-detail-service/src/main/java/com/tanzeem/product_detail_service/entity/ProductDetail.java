package com.tanzeem.product_detail_service.entity;

import java.time.LocalDateTime;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail {
	
	@Id
	@NotNull(message = "Id cannot be blank")
	private Long productId;
	
	@NotNull(message = "Price is mandatory")
	@PositiveOrZero(message = "Price should be positive or zero")
	private double price;
	
	@NotBlank(message = "Size is mandatory")
	private String size;
	
	@NotBlank(message = "Design is mandatory")
	private String design;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
