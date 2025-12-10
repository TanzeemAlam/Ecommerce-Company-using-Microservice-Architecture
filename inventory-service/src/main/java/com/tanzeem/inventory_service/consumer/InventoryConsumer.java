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
import com.tanzeem.inventory_service.dto.InventoryAdjustmentRequestDto;
import com.tanzeem.inventory_service.entity.Inventory;
import com.tanzeem.inventory_service.event.CartEvent;
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
	        "cart-reserve-events", CartEvent.class,
	        "cart-release-events", CartEvent.class,
	        "cart-confirm-events", CartEvent.class
	);
	
	@KafkaListener(topics = "inventory-events", groupId = "inventory-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerProductAddEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		InventoryEvent event = (InventoryEvent) convertToEvent(record.value().toString(), topic);
		
		
		inventoryService.addInventoryProduct(new Inventory(
													event.getDto().getProductId(), 
													event.getDto().getSku()));
	}
	
	@KafkaListener(topics = "cart-reserve-events", groupId = "cart-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerInventoryReserveEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		CartEvent event = (CartEvent) convertToEvent(record.value().toString(), topic);
		
		
		inventoryService.reserveProduct(new InventoryAdjustmentRequestDto(
				event.getDto().getProductId(),
				event.getDto().getQuantity()));
	}
	
	@KafkaListener(topics = "cart-release-events", groupId = "cart-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerInventoryReleaseEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		CartEvent event = (CartEvent) convertToEvent(record.value().toString(), topic);
		
		
		inventoryService.releaseProduct(new InventoryAdjustmentRequestDto(
				event.getDto().getProductId(),
				event.getDto().getQuantity()));
	}
	
	@KafkaListener(topics = "cart-confirm-events", groupId = "cart-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerInventoryConfirmEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		CartEvent event = (CartEvent) convertToEvent(record.value().toString(), topic);
		
		
		String response = inventoryService.confirmProductSale(new InventoryAdjustmentRequestDto(
				event.getDto().getProductId(),
				event.getDto().getQuantity()));
		
		logger.info(response);
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
