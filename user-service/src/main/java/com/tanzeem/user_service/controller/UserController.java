package com.tanzeem.user_service.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.tanzeem.user_service.dto.*;
import com.tanzeem.user_service.entity.User;
import com.tanzeem.user_service.event.RegistrationEvent;
import com.tanzeem.user_service.service.*;
import com.tanzeem.user_service.util.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthService authService;
	
	@Value("${custom.message}")
	private String customMessage;
	
	@GetMapping("/config")
	public String getConfig() {
		return "App Name from Config Server: " + customMessage;
	}
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRequestDto dto, HttpServletRequest request) {
		User user = userService.register(convertToEntity(dto));
		
		publisher.publishEvent(new RegistrationEvent(user, applicationURL(request)));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(AppConstants.USER_REGISTRATION_SUCCESS));
	}
	
	@GetMapping("/token")
	public ResponseEntity<ApiResponse> login(@RequestBody AuthRequestDto dto) {
		String jwtToken = null;
		
		try {
			authService.authenticate(dto.getUsername(), dto.getPassword());
			
			jwtToken = authService.generateToken(dto.getUsername());
			
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(AppConstants.JWT_TOKEN + jwtToken));
		}
		catch (Exception e) {			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(AppConstants.USER_NOT_FOUND));
		}
	}
	
	@GetMapping("/verifyRegistration")
	public ResponseEntity<ApiResponse> verifyRegistration(@RequestParam String token) {		
		String result = userService.validateVerificationToken(token);
		
		if (result.equalsIgnoreCase(AppConstants.VALID_TOKEN)) return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(AppConstants.TOKEN_VALIDATED));
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(AppConstants.TOKEN_NOT_FOUND));
	}
	
	@GetMapping("/validateToken")
	public ResponseEntity<ApiResponse> validateToken(@RequestParam String token) {
		if (authService.validateJwtToken(token))
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(AppConstants.VALID_TOKEN));
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(AppConstants.INVALID_TOKEN));
	}
	
	private String applicationURL(HttpServletRequest request) {
		return "http://" + 
				request.getServerName() + 
				":" +
				request.getServerPort() + 
				request.getContextPath() + 
				this.getClass().getAnnotation(RequestMapping.class).value()[0];
	}
	
	private User convertToEntity(UserRequestDto dto) {
		return modelMapper.map(dto, User.class);
	}
}
