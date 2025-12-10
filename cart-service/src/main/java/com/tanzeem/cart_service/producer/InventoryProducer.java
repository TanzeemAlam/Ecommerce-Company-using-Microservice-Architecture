package com.tanzeem.cart_service.producer;

import java.time.LocalDateTime;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanzeem.cart_service.dto.CartAdjustmentDto;
import com.tanzeem.cart_service.event.InventoryEvent;

@Component
public class InventoryProducer {
	
	//Cart events topic
	public static final String KAFKA_CART_RESERVE_TOPIC						= "cart-reserve-events";
	public static final String KAFKA_CART_RELEASE_TOPIC 					= "cart-release-events";
	public static final String KAFKA_CART_CONFIRM_TOPIC 					= "cart-confirm-events";
		
	@Autowired
	private ObjectMapper objectMapper;
		
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
		
	public void produceCartItemReserveKafkaEvent(String eventType, CartAdjustmentDto dto, String message, LocalDateTime timestamp) throws JsonProcessingException {
		
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(
													KAFKA_CART_RESERVE_TOPIC,
													objectMapper.writeValueAsString(new InventoryEvent(
																							eventType, 
																							dto,
																							message,
																							timestamp)
													));
		
		kafkaTemplate.send(record);	
	}
	
	public void produceCartItemReleaseKafkaEvent(String eventType, CartAdjustmentDto dto, String message, LocalDateTime timestamp) throws JsonProcessingException {
		
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(
													KAFKA_CART_RELEASE_TOPIC,
													objectMapper.writeValueAsString(new InventoryEvent(
																							eventType, 
																							dto,
																							message,
																							timestamp)
													));
		
		kafkaTemplate.send(record);	
	}
	
	public void produceCartItemConfirmKafkaEvent(String eventType, CartAdjustmentDto dto, String message, LocalDateTime timestamp) throws JsonProcessingException {
		
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(
													KAFKA_CART_CONFIRM_TOPIC,
													objectMapper.writeValueAsString(new InventoryEvent(
																							eventType, 
																							dto,
																							message,
																							timestamp)
													));
		
		kafkaTemplate.send(record);	
	}
}
