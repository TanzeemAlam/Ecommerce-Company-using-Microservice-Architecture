package com.tanzeem.user_service.producer;

import java.time.LocalDateTime;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanzeem.user_service.dto.UserEvent;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

@Service
public class NotificationProducer {

	private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);

	//User events topic
	public static final String KAFKA_USER_TOPIC	= "user-events";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	public void produceKafkaEvent(String eventType, String id, String message, LocalDateTime timestamp) throws JsonProcessingException {
		Span span = tracer.nextSpan().name("publish-notification-event").start();
		
		logger.info("Producing message under trace ID: {}", span.context().traceId());
		logger.info("Producing message under parent ID: {}", span.context().parentId());
		
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(
													KAFKA_USER_TOPIC,
													objectMapper.writeValueAsString(new UserEvent(
																							eventType, 
																							id, 
																							message, 
																							timestamp)
													));
		
		try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
			kafkaTemplate.send(record);	
		} finally {
			span.end();
		}
	}
}