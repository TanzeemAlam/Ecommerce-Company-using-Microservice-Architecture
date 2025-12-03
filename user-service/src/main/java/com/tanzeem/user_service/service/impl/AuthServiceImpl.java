package com.tanzeem.user_service.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanzeem.user_service.producer.NotificationProducer;
import com.tanzeem.user_service.service.*;
import com.tanzeem.user_service.util.AppConstants;
import com.tanzeem.user_service.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private NotificationProducer kafkaService;
	
	@Override
	public String generateToken(String username) {
		
		UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
		
		String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
		
		//Producing event
		try {
			kafkaService.produceKafkaEvent(AppConstants.KAFKA_TOKEN_GENERATED_EVENT,
											username,
											AppConstants.JWT_TOKEN + token,
											LocalDateTime.now());
		} catch (Exception e) {
			logger.error(AppConstants.KAFKA_ERROR +  e.getMessage());
		}
		
		return token;
	}
	
	@Override
	public boolean validateJwtToken(String token) {
		boolean response = jwtUtil.validateToken(token);
		
		//Producing event
		try {
			kafkaService.produceKafkaEvent(AppConstants.KAFKA_USER_LOGGED_IN_EVENT,
													jwtUtil.extractUsername(token),
													response ? AppConstants.TOKEN_VALIDATED : AppConstants.INVALID_TOKEN,
													LocalDateTime.now());
		} catch (Exception e) {
			logger.error(AppConstants.KAFKA_ERROR +  e.getMessage());
		}
		
		return response;
	}

	@Override
	public void authenticate(String username, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));		
	}
}
