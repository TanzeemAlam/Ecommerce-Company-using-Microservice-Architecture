package com.tanzeem.order_service.event;

import java.time.LocalDateTime;

import com.tanzeem.order_service.dto.OrderUpdateDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
	private String eventType;
	private OrderUpdateDto dto;
	private String message;
	private LocalDateTime timestamp;
}
