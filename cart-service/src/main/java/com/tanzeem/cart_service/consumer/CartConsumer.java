package com.tanzeem.cart_service.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tanzeem.cart_service.event.CartEvent;
import com.tanzeem.cart_service.service.CartService;

@Service
public class CartConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CartConsumer.class);
	
	@Autowired
	private CartService cartService;
	
	private final ObjectMapper mapper = new ObjectMapper()
											.registerModule(new JavaTimeModule())
											.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			
	private static final Map<String, Class<?>> TOPIC_CLASS_MAP = Map.of(
	        "cart-lock-events", CartEvent.class,
	        "cart-clear-events", CartEvent.class
	);
	
	@KafkaListener(topics = "cart-lock-events", groupId = "cart-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerCartConfirmEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws JsonProcessingException {
		
		CartEvent event = (CartEvent) convertToEvent(record.value().toString(), topic);
		
		
		cartService.confirmCart(event.getUserId());
		
		logger.info("CART CONFIRMATION RECEIVED:\n{\n" +
		         "  \"Event Type\": \"{}\",\n" +
		         "  \"User ID\": \"{}\",\n" +
		         "  \"Message\": \"{}\",\n" +
		         "  \"Timestamp\": \"{}\"\n" +
		         "}", 
		         event.getEventType(),
		         event.getUserId(),
		         event.getMessage(),
		         event.getTimestamp());
	}
	
	@KafkaListener(topics = "cart-clear-events", groupId = "cart-group", containerFactory = "kafkaListenerContainerFactory")
	public void consumerCartClearEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		CartEvent event = (CartEvent) convertToEvent(record.value().toString(), topic);
		
		cartService.clearAllCartItems(event.getUserId());
		
		logger.info("CART ITEMS CLEARING:\n{\n" +
		         "  \"Event Type\": \"{}\",\n" +
		         "  \"User ID\": \"{}\",\n" +
		         "  \"Message\": \"{}\",\n" +
		         "  \"Timestamp\": \"{}\"\n" +
		         "}", 
		         event.getEventType(),
		         event.getUserId(),
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
