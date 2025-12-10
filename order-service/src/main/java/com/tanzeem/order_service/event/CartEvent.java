package com.tanzeem.order_service.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEvent {
	private String eventType;
	private Long userId;
	private String message;
	private LocalDateTime timestamp;
}
