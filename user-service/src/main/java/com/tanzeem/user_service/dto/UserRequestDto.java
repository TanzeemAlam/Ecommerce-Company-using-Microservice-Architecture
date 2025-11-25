package com.tanzeem.user_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
	@NotNull(message = "Field cannot be null")
	private String username;
	
	@Column(length = 60)
	private String password;
	
	private String role;
}
