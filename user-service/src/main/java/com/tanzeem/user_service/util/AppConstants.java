package com.tanzeem.user_service.util;

public class AppConstants {
	public static final String VALID_TOKEN 					= "Valid";
	public static final String INVALID_TOKEN 				= "Invalid";
	public static final String TOKEN_VALIDATED				= "Token Validated";
	public static final String TOKEN_NOT_FOUND				= "Token Not Found";
	public static final String JWT_TOKEN					= "JWT token: ";
	
	
	public static final String USER_REGISTRATION_SUCCESS 	= "User Registered Successfully";
	public static final String USER_SERVICE_STATUS			= "User Service is UP and running";
	public static final String USER_NOT_FOUND				= "User Not Found";
	public static final String VERIFY_REGISTRATION			= "Verify Registration: ";
	
	public static final String KAFKA_USER_REGISTERED_EVENT	= "USER_REGISTERED";
	public static final String KAFKA_TOKEN_GENERATED_EVENT	= "USER_TOKEN_REGISTERED";
	public static final String KAFKA_USER_LOGGED_IN_EVENT	= "USER_LOGGED_IN";
	
	public static final String KAFKA_ERROR					= "KAFKA Error: ";
	
	//Kafka Topics
	public static final String KAFKA_USER_TOPIC	= "user-events";
}
