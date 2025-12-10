package com.tanzeem.order_service.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanzeem.order_service.dto.CartItemDto;
import com.tanzeem.order_service.dto.OrderRequestDto;
import com.tanzeem.order_service.dto.OrderResponseDto;
import com.tanzeem.order_service.entity.Order;
import com.tanzeem.order_service.service.OrderService;
import com.tanzeem.order_service.util.ApiResponse;
import com.tanzeem.order_service.util.AppConstant;

import jakarta.validation.Valid;
import jakarta.ws.rs.Path;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody OrderRequestDto dto) {
		String response = orderService.createOrder(convertToEntity(dto));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(response));
	}
	
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponse> deleteOrder(@Valid @PathVariable Long orderId) {
		String response = orderService.deleteOrder(orderId);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@GetMapping("/{orderId}/cart")
	public ResponseEntity<?> getCart(@Valid @PathVariable Long orderId) {
		List<CartItemDto> list = orderService.getCart(orderId);
		
		if (list == null) return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(AppConstant.ORDER_PENDING_OR_NULL));
		
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/{orderId}/status")
	public ResponseEntity<?> getOrderStatus(@Valid @PathVariable Long orderId) {
		Order response = orderService.getOrderStatus(orderId);
		
		if (response == null) return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(AppConstant.ORDER_PENDING_OR_NULL));
		
		return ResponseEntity.status(HttpStatus.OK).body(convertToDto(response));
	}
	
	/**Utility Methods**/
	
	private Order convertToEntity(@Valid OrderRequestDto dto) {
		return mapper.map(dto, Order.class);
	}
	
	private OrderResponseDto convertToDto(@Valid Order order) {
		return mapper.map(order, OrderResponseDto.class);
	}
}