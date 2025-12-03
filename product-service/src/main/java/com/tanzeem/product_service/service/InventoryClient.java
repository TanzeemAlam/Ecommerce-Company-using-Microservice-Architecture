package com.tanzeem.product_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.tanzeem.product_service.dto.InventoryDto;
import com.tanzeem.product_service.util.ApiResponse;

import reactor.core.publisher.Mono;

@Component
public class InventoryClient {

	@Autowired
	private WebClient webClient;
	
	public Mono<ApiResponse> addProductInInventory(InventoryDto inventoryRequest, String token) {
		
		return webClient.post()
				.uri("http://localhost:8084/inventory")
				.header(HttpHeaders.AUTHORIZATION, token)
				.bodyValue(inventoryRequest)
				.retrieve()
				.bodyToMono(ApiResponse.class);
	}
}
