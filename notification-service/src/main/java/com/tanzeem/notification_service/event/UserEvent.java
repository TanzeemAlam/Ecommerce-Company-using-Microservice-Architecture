package com.tanzeem.notification_service.event;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
	private String eventType;     
    private String userId;
    private String message;
    private LocalDateTime timestamp;
}
