package com.tanzeem.user_service.service;

import com.tanzeem.user_service.entity.User;

import jakarta.validation.Valid;

public interface UserService {
	User register(@Valid User user);

	void saveVerificationTokenForUser(String token, User user);

	String validateVerificationToken(String token);
}
