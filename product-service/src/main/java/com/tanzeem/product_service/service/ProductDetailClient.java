package com.tanzeem.product_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.tanzeem.product_service.dto.ProductDetailDto;

@Component
public class ProductDetailClient {

	@Autowired
	private WebClient webClient;
	
	public ProductDetailDto getProductDetailsById(Long id, String token) {
		return webClient.get()
				.uri("http://localhost:8083/product-details/" + id + "/product")
				.header(HttpHeaders.AUTHORIZATION, token)
				.retrieve()
				.bodyToMono(ProductDetailDto.class)
				.block();
	}
	
	public List<ProductDetailDto> getAllProductDetails(String token) {
		return webClient.get()
				.uri("http://localhost:8083/product-details")
				.header(HttpHeaders.AUTHORIZATION, token)
				.retrieve()
				.bodyToFlux(ProductDetailDto.class)
				.collectList()
				.block();
	}
}
