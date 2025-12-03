package com.tanzeem.inventory_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

	@Id
	@NotBlank(message = "Id cannot be blank")
	private Long productId;
	
	@NotBlank(message = "SKU is mandatory")
	private String sku;
	
	private Long quantity = 0L;
	
	private Long reserved = 0L;

	private Long available = 0L;
	
	private LocalDateTime createdAt;
}
