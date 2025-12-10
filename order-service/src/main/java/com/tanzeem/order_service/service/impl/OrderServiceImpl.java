package com.tanzeem.order_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanzeem.order_service.dto.CartItemDto;
import com.tanzeem.order_service.dto.OrderUpdateDto;
import com.tanzeem.order_service.entity.Order;
import com.tanzeem.order_service.enums.OrderStatus;
import com.tanzeem.order_service.enums.PaymentStatus;
import com.tanzeem.order_service.producer.CartProducer;
import com.tanzeem.order_service.repository.OrderRepository;
import com.tanzeem.order_service.service.OrderService;
import com.tanzeem.order_service.util.AppConstant;

import jakarta.validation.Valid;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartProducer producer;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public String createOrder(Order order) {
		order.setOrderStatus(OrderStatus.PENDING.toString());
		order.setPaymentStatus(PaymentStatus.PENDING.toString());
		
		order.setCreatedAt(getCurrentTime());
		
		orderRepository.save(order);
		
		//Kafka Event to Cart for confirming the cart items
		try {
			producer.produceCartItemConfirmKafkaEvent(
					AppConstant.CART_CONFIRM_EVENT,
					order.getUserId(),
					AppConstant.CART_CONFIRM_BY_USER + order.getUserId().toString(),
					getCurrentTime());
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return AppConstant.ORDER_CREATED + order.getUserId();
	}

	@Override
	public Order getOrderStatus(Long orderId) {
		return orderRepository.findById(orderId).orElse(null);	
	}

	@Override
	public List<CartItemDto> getCart(Long orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		
		if (order != null) {
			String orderedItemsJson = order.getOrderedItemsJson();
			
			if (orderedItemsJson.isEmpty() || orderedItemsJson == null) {
				return null;
			}
			
			try {
				return objectMapper.readValue(
						orderedItemsJson, 
						new TypeReference<List<CartItemDto>>() {});
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		return null;
	}

	@Override
	public String deleteOrder(@Valid Long orderId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void updateOrder(OrderUpdateDto dto) {
		Order order = orderRepository.findByUserId(dto.getUserId());
		
		order.setOrderAmount(dto.getTotalamount());
		order.setOrderedItemsJson(dto.getOrderedItemsJson());
		order.setOrderStatus(OrderStatus.CONFIRMED.toString());
		
		orderRepository.save(order);
		
		//Produce clear cart Kafka event
		try {
			producer.produceCartClearKafkaEvent(
					AppConstant.ORDER_CONFIRMED,
					order.getUserId(),
					AppConstant.ORDER_CONFIRMED_BY_USER + order.getUserId().toString(),
					getCurrentTime());
		}
		catch (Exception e) { e.printStackTrace(); }
		
	}
	
	/*****Private Helper Methods *****/
	
	private LocalDateTime getCurrentTime() { return LocalDateTime.now(); }
}
