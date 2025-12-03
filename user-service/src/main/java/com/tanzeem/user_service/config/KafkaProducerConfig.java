package com.tanzeem.user_service.config;

import org.springframework.context.annotation.*;
import org.springframework.kafka.core.*;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
		KafkaTemplate<String, Object> template = new KafkaTemplate<String, Object>(pf);
		
		template.setObservationEnabled(true);
		
		return template;
	}
}
