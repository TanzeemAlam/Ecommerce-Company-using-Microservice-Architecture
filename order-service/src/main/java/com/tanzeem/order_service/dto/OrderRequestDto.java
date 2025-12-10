package com.tanzeem.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
	@NotNull(message = "User id cannot be null")
	private Long userId;
	
	@NotBlank(message = "Address cannot be blank")
	private String billingAddress;
	
	@NotBlank(message = "Payment mode needs to be mentioned")
	private String paymentMode;
}
