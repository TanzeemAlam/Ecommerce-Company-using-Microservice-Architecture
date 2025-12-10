package com.tanzeem.cart_service.producer;

import java.time.LocalDateTime;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanzeem.cart_service.dto.OrderUpdateDto;
import com.tanzeem.cart_service.event.OrderEvent;

@Component
public class OrderProducer {

	//Order events
	public static final String KAFKA_ORDER_CONFIRM_TOPIC			= "order-confirm-events";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	public void produceOrderConfirmKafkaEvent(String eventType, OrderUpdateDto dto, String message, LocalDateTime timestamp) throws JsonProcessingException {
		
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(
													KAFKA_ORDER_CONFIRM_TOPIC,
													objectMapper.writeValueAsString(new OrderEvent(
																							eventType, 
																							dto,
																							message,
																							timestamp)
													));
		
		kafkaTemplate.send(record);	
	}
}
