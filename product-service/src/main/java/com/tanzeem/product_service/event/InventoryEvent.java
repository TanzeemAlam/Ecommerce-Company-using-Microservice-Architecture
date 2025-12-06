package com.tanzeem.product_service.event;

import java.time.LocalDateTime;

import com.tanzeem.product_service.dto.InventoryDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEvent {
	private String eventType;
	private InventoryDto dto;
	private String message;
	private LocalDateTime timestamp;
}
