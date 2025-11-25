package com.tanzeem.product_detail_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductClient {
	
	@Autowired
	private WebClient webClient;
	
	public Boolean validateProduct(Long productId, String token) {
		Boolean exists =  webClient.get()
							.uri("http://localhost:8082/products/" + productId + "/exists")
							.header(HttpHeaders.AUTHORIZATION, token)
							.retrieve()
							.bodyToMono(Boolean.class)
							.block();
		
		return Boolean.TRUE.equals(exists);
	}
}
