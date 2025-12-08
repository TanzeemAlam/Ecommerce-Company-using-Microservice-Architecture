package com.tanzeem.inventory_service.event;

import java.time.LocalDateTime;

import com.tanzeem.inventory_service.dto.InventoryAdjustmentRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEvent {
	private String eventType;
	private InventoryAdjustmentRequestDto dto;
	private String message;
	private LocalDateTime timestamp;
}
