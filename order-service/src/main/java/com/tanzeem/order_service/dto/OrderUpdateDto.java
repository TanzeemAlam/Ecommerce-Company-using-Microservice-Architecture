package com.tanzeem.order_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDto {
	@NotNull(message = "Cart Id cannot be null")
	private Long cartId;
	
	private Long userId;
	
	private double totalamount;
	
	private String orderedItemsJson;
}
