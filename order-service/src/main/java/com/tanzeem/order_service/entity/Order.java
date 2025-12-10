package com.tanzeem.order_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	@NotNull(message = "User id cannot be null")
	private Long userId;
	
	private String orderStatus;						//Enum OrderStatus
	
	@NotBlank(message = "Address cannot be null")
	private String billingAddress;
	
	private double orderAmount;
	
	private String orderedItemsJson;
	
	private String paymentStatus;					//Enum PaymentStatus
	
	private String paymentMode;
	
	private LocalDateTime createdAt;
}
