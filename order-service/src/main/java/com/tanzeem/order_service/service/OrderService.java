package com.tanzeem.order_service.service;

import java.util.List;

import com.tanzeem.order_service.dto.CartItemDto;
import com.tanzeem.order_service.dto.OrderResponseDto;
import com.tanzeem.order_service.dto.OrderUpdateDto;
import com.tanzeem.order_service.entity.Order;

import jakarta.validation.Valid;

public interface OrderService {

	List<CartItemDto> getCart(Long userId);
	
	String createOrder(Order dto);
	
	Order getOrderStatus(Long orderId);

	String deleteOrder(Long orderId);

	void updateOrder(OrderUpdateDto dto);
}
