package com.tanzeem.product_service.producer;

import java.time.LocalDateTime;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanzeem.product_service.dto.InventoryDto;
import com.tanzeem.product_service.event.InventoryEvent;

import io.micrometer.tracing.Tracer;

@Service
public class InventoryProducer {
	private static final Logger logger = LoggerFactory.getLogger(InventoryProducer.class);
	
	//User events topic
	public static final String KAFKA_PRODUCT_TOPIC	= "inventory-events";
		
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private Tracer tracer;
		
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
		
	public void produceKafkaEvent(String eventType, InventoryDto dto, String message, LocalDateTime timestamp) throws JsonProcessingException {
		
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(
													KAFKA_PRODUCT_TOPIC,
													objectMapper.writeValueAsString(new InventoryEvent(
																							eventType, 
																							dto,
																							message,
																							timestamp)
													));
		
		kafkaTemplate.send(record);	
	}
}
