package com.tanzeem.user_service.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tanzeem.user_service.entity.*;
import com.tanzeem.user_service.repository.*;
import com.tanzeem.user_service.service.UserService;
import com.tanzeem.user_service.util.AppConstants;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	VerificationTokenRepository verificationTokenRepository;
	
	@Override
	public User register(@Valid User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setCreatedAt(LocalDateTime.now());
		
		if (user.getRole() == null)	user.setRole("USER");
		
		userRepository.save(user);
		
		return user;
	}

	@Override
	public void saveVerificationTokenForUser(String token, User user) {
		VerificationToken verificationToken = new VerificationToken(token, user);
		
		verificationTokenRepository.save(verificationToken);
	}

	@Override
	public String validateVerificationToken(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		
		if (verificationToken == null) return AppConstants.INVALID_TOKEN;
		
		User user = verificationToken.getUser();
		
		user.setEnabled(true);
		userRepository.save(user);
		
		return AppConstants.VALID_TOKEN;
	}

}
