package com.tanzeem.notification_service.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tanzeem.notification_service.event.*;

import io.micrometer.tracing.*;

@Service
public class NotificationConsumer {

	private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);
	
	@Autowired
	private Tracer tracer;
	
	private final ObjectMapper mapper = new ObjectMapper()
											.registerModule(new JavaTimeModule())
											.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			
	private static final Map<String, Class<?>> TOPIC_CLASS_MAP = Map.of(
	        "user-events", UserEvent.class,
	        "product-events", ProductEvent.class
	);

	
	@KafkaListener(topics = "user-events", groupId = "notfication-group")
	public void consumeUserEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		Span current = tracer.currentSpan();
		
		logger.info("Consumed message under trace ID: {}", current.context().traceId());
		
		UserEvent event = (UserEvent) convertToEvent(record.value().toString(), topic);
		
		logger.info("NOTIFICATION RECEIVED:\n{\n" +
		         "  \"Event Type\": \"{}\",\n" +
		         "  \"Event ID\": \"{}\",\n" +
		         "  \"Message\": \"{}\",\n" +
		         "  \"Timestamp\": \"{}\"\n" +
		         "}", 
		         event.getEventType(),
		         event.getUserId(),
		         event.getMessage(),
		         event.getTimestamp());
	}
	
	@KafkaListener(topics = "product-events", groupId = "notification-group")
	public void consumerProductEvent(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
		Span current = tracer.currentSpan();
		
		logger.info("Consumed message under trace ID: {}", current.context().traceId());
		
		ProductEvent event = (ProductEvent) convertToEvent(record.value().toString(), topic);
		
		logger.info("NOTIFICATION RECEIVED:\n{\n" +
		         "  \"Event Type\": \"{}\",\n" +
		         "  \"Product ID\": \"{}\",\n" +
		         "  \"Message\": \"{}\",\n" +
		         "  \"Timestamp\": \"{}\"\n" +
		         "}", 
		         event.getEventType(),
		         event.getProductId(),
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
