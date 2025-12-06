package com.tanzeem.inventory_service.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tanzeem.inventory_service.entity.Inventory;
import com.tanzeem.inventory_service.event.CartReleaseEvent;
import com.tanzeem.inventory_service.event.CartReserveEvent;
import com.tanzeem.inventory_service.event.InventoryEvent;
import com.tanzeem.inventory_service.service.InventoryService;

@Service
public class InventoryConsumer {

private static final Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);
	
	@Autowired
	private InventoryService inventoryService;
	
	private final ObjectMapper mapper = new ObjectMapper()
											.registerModule(new JavaTimeModule())
											.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			
	private static final Map<String, Class<?>> TOPIC_CLASS_MAP = Map.of(
	        "inventory-events", InventoryEvent.class,
	        "cart-reserve-events", CartReserveEvent.class,
	        "cart-release-events", CartReleaseEvent.class
	);
	
	@KafkaListener(topics = "inventory-events", groupId = "inventory-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerProductAddEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		InventoryEvent event = (InventoryEvent) convertToEvent(record.value().toString(), topic);
		
		
		inventoryService.addInventoryProduct(new Inventory(
													event.getDto().getProductId(), 
													event.getDto().getSku()));
		
		logger.info("NOTIFICATION RECEIVED:\n{\n" +
		         "  \"Event Type\": \"{}\",\n" +
		         "  \"Product ID\": \"{}\",\n" +
		         "  \"Message\": \"{}\",\n" +
		         "  \"Timestamp\": \"{}\"\n" +
		         "}", 
		         event.getEventType(),
		         event.getDto().getProductId(),
		         event.getMessage(),
		         event.getTimestamp());
	}
	
	/********** Helper methods  **********/
	
	/**
	 * Dynamically mapping producer object to respective event classes
	 */
	private Object convertToEvent(String json, String topic) {
		Class<?> clazz = TOPIC_CLASS_MAP.get(topic);
		
		try { return mapper.readValue(json, clazz); }
		catch (Exception e) { e.printStackTrace(); }
		
		return null;
	}
}
