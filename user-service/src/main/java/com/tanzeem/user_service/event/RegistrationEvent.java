package com.tanzeem.user_service.event;

import org.springframework.context.ApplicationEvent;

import com.tanzeem.user_service.entity.User;

public class RegistrationEvent extends ApplicationEvent {
	
	private final User user;
	private final String applicationUrl;
	
	public RegistrationEvent(User user, String applicationUrl) {
		super(user);
		
		this.user = user;
		this.applicationUrl = applicationUrl;
	}

	public User getUser() {
		return user;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}
}
