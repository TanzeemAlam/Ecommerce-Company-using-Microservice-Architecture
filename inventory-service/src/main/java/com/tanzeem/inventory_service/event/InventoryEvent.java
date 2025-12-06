package com.tanzeem.inventory_service.event;

import java.time.LocalDateTime;

import com.tanzeem.inventory_service.dto.ProductRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEvent {
	private String eventType;
	private ProductRequestDto dto;
	private String message;
	private LocalDateTime timestamp;
}
