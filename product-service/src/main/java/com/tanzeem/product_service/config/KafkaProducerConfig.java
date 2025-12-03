package com.tanzeem.product_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
		KafkaTemplate<String, Object> template = new KafkaTemplate<String, Object>(pf);
		
		template.setObservationEnabled(true);
		
		return template;
	}
}
