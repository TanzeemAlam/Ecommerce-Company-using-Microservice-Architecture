package com.tanzeem.cart_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tanzeem.cart_service.dto.CartAdjustmentDto;


@Service
public class InventoryClient {

	@Autowired
	private WebClient webClient;
	
	public String validateItemFromInventory(CartAdjustmentDto dto, String token) {
		return webClient.get()
				.uri("http://localhost:8084/inventory/" + dto.getProductId() + "/quantity/"+ dto.getQuantity() + "/validate")
				.header(HttpHeaders.AUTHORIZATION, token)
				.retrieve()
				.bodyToMono(String.class)
				.block();
	} 
}
