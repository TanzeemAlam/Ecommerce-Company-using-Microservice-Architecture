package com.tanzeem.product_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
	private String eventType;
	private String productId;
	private String message;
	private LocalDateTime timestamp;
}
