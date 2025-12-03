package com.tanzeem.user_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
	private String eventType;
	private String userId;
	private String message;
	private LocalDateTime timestamp;
}
