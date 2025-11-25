package com.tanzeem.product_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	private String description;
	
	@NotBlank(message = "Product sku is mandatory")
	private String sku;
	
	@NotBlank(message = "Product category is mandatory")
	private String category;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
