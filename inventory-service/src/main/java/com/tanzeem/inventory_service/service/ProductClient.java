package com.tanzeem.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductClient {

	@Autowired
	private WebClient webClient;
	
	public Boolean validateProduct(Long id) {
		Boolean exists =  webClient.get()
							.uri("http://localhost:8082/products/" + id + "/exists")
							.retrieve()
							.bodyToMono(Boolean.class)
							.block();
		
		return Boolean.TRUE.equals(exists);
	}
}
