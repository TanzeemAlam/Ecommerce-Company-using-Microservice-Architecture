package com.tanzeem.notification_service.event;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
	private String eventType;
	private String productId;
	private String message;
	private LocalDateTime timestamp;
}
