package com.tanzeem.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
	
	@NotNull(message = "User id cannot be null")
	private Long userId;
	
	private String orderStatus;						//Enum OrderStatus
	
	@NotBlank(message = "Address cannot be null")
	private String billingAddress;
	
	private double orderAmount;
	
	private String orderedItemsJson;
	
	private String paymentStatus;					//Enum PaymentStatus
	
	private String paymentMode;
}
