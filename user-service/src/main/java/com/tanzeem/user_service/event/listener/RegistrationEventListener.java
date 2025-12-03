package com.tanzeem.user_service.event.listener;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.tanzeem.user_service.entity.User;
import com.tanzeem.user_service.event.RegistrationEvent;
import com.tanzeem.user_service.producer.NotificationProducer;
import com.tanzeem.user_service.service.UserService;
import com.tanzeem.user_service.util.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent>{
	
	@Autowired
	UserService userService;
	
	@Autowired
	private NotificationProducer kafkaService;
	
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
		
		try {
			kafkaService.produceKafkaEvent(AppConstants.KAFKA_USER_REGISTERED_EVENT,
								user.getUsername(), 
								AppConstants.USER_REGISTRATION_SUCCESS + " - " + AppConstants.VERIFY_REGISTRATION + url,
								LocalDateTime.now());
		} catch (Exception e) {
			logger.error(AppConstants.KAFKA_ERROR +  e.getMessage());
		}
	}
}
