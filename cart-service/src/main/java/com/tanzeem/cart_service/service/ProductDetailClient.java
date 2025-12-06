package com.tanzeem.cart_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductDetailClient {
	
	@Autowired
	private WebClient webClient;
	
	public Double getProductPriceById(Long id, String token) {
		return webClient.get()
				.uri("http://localhost:8083/product-details/" + id + "/price")
				.header(HttpHeaders.AUTHORIZATION, token)
				.retrieve()
				.bodyToMono(Double.class)
				.block();
	}
}
