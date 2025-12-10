package com.tanzeem.cart_service.event;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEvent {
	private String eventType;
	private Long userId;
	private String message;
	private LocalDateTime timestamp;
}
