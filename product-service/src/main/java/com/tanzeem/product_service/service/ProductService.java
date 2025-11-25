package com.tanzeem.product_service.service;

import java.util.List;

import com.tanzeem.product_service.dto.ProductResponseDto;
import com.tanzeem.product_service.entity.Product;

public interface ProductService {
	public Product addProduct(Product p);

	public List<ProductResponseDto> getAllProducts(String token);

	public ProductResponseDto getProduct(Long id, String token);
	
	public String updateProduct(Long id, Product updatedProduct);

	public String deleteProduct(Long id);
	
	public Boolean checkCustomerExists(Long id);
}
