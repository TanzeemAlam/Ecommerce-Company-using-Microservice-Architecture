package com.tanzeem.order_service.producer;

import java.time.LocalDateTime;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanzeem.order_service.event.CartEvent;

@Component
public class CartProducer {

	//Cart events topic
	public static final String KAFKA_CART_CONFIRM_TOPIC						= "cart-lock-events";
	public static final String KAFKA_CART_CLEAR_TOPIC						= "cart-clear-events";
			
	@Autowired
	private ObjectMapper objectMapper;
			
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
			
	public void produceCartItemConfirmKafkaEvent(String eventType, Long userId, String message, LocalDateTime timestamp) throws JsonProcessingException {
			
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(
														KAFKA_CART_CONFIRM_TOPIC,
														objectMapper.writeValueAsString(new CartEvent(
																								eventType, 
																								userId,
																								message,
																								timestamp)
														));
			
		kafkaTemplate.send(record);	
	} 
	
	public void produceCartClearKafkaEvent(String eventType, Long userId, String message, LocalDateTime timestamp) throws JsonProcessingException {
		
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(
														KAFKA_CART_CONFIRM_TOPIC,
														objectMapper.writeValueAsString(new CartEvent(
																								eventType, 
																								userId,
																								message,
																								timestamp)
														));
			
		kafkaTemplate.send(record);	
	} 
}
