package com.tanzeem.order_service.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tanzeem.order_service.event.OrderEvent;
import com.tanzeem.order_service.service.OrderService;

@Component
public class CartConsumer {
	private static final Logger logger = LoggerFactory.getLogger(CartConsumer.class);
	
	@Autowired
	private OrderService orderService;
	
	private final ObjectMapper mapper = new ObjectMapper()
											.registerModule(new JavaTimeModule())
											.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			
	private static final Map<String, Class<?>> TOPIC_CLASS_MAP = Map.of(
	        "order-confirm-events", OrderEvent.class
	);
	
	@KafkaListener(topics = "order-confirm-events", groupId = "order-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerCartConfirmEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		OrderEvent event = (OrderEvent) convertToEvent(record.value().toString(), topic);
		
		
		orderService.updateOrder(event.getDto());
		
		logger.info("Order CONFIRMATION RECEIVED:\n{\n" +
		         "  \"Event Type\": \"{}\",\n" +
		         "  \"Ordered Items\": \"{}\",\n" +
		         "  \"Message\": \"{}\",\n" +
		         "  \"Timestamp\": \"{}\"\n" +
		         "}", 
		         event.getEventType(),
		         event.getDto().getOrderedItemsJson(),
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
