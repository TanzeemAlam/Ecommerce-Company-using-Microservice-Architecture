package com.tanzeem.user_service.event.listener;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.tanzeem.user_service.entity.User;
import com.tanzeem.user_service.event.RegistrationEvent;
import com.tanzeem.user_service.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent>{
	
	@Autowired
	UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(RegistrationEventListener.class);
	
	@Override
	public void onApplicationEvent(RegistrationEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		
		userService.saveVerificationTokenForUser(token, user);
		
		//Paste URL in logs
		String url = event.getApplicationUrl()
					+ "/verifyRegistration?token="
					+ token;
		
		logger.info("Click verfication link to verify user: {}", url);
	}
}
