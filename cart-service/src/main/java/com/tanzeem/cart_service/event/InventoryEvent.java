package com.tanzeem.cart_service.event;

import java.time.LocalDateTime;

import com.tanzeem.cart_service.dto.CartAdjustmentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEvent {
	private String eventType;
	private CartAdjustmentDto dto;
	private String message;
	private LocalDateTime timestamp;
}
