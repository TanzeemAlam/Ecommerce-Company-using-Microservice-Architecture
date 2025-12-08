package com.tanzeem.product_detail_service.service;

import java.util.List;

import com.tanzeem.product_detail_service.entity.ProductDetail;

import jakarta.validation.Valid;

public interface ProductDetailService {
	public ProductDetail addProduct(ProductDetail p);

	public List<ProductDetail> getAllProductDetails();

	public ProductDetail getProductDetail(Long id);
	
	public String updateProductDetail(Long id, ProductDetail updatedProduct);

	public String deleteProductDetail(Long id);
	
	public Boolean validateProduct(Long id, String token);

	public double getProductPrice(@Valid Long id);
}
