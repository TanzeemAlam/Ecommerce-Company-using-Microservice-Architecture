package com.tanzeem.user_service.service;

public interface AuthService {
	
	void authenticate(String username, String password);
	
	String generateToken(String username);

	boolean validateJwtToken(String token);
}
