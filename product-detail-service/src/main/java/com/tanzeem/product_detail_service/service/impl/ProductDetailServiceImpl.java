package com.tanzeem.product_detail_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanzeem.product_detail_service.entity.ProductDetail;
import com.tanzeem.product_detail_service.repository.ProductDetailRepository;
import com.tanzeem.product_detail_service.service.ProductClient;
import com.tanzeem.product_detail_service.service.ProductDetailService;
import com.tanzeem.product_detail_service.util.AppConstants;

@Service
public class ProductDetailServiceImpl implements ProductDetailService{

	@Autowired
	private ProductDetailRepository productDetailRepository;
	
	@Autowired
	private ProductClient productClient;
	
	@Override
	public ProductDetail addProduct(ProductDetail p) {
		p.setCreatedAt(LocalDateTime.now());
		
		return productDetailRepository.save(p);
	}

	@Override
	public List<ProductDetail> getAllProductDetails() {
		return  productDetailRepository.findAll();
	}

	@Override
	public ProductDetail getProductDetail(Long id) {
		return productDetailRepository.findById(id).orElse(null);
	}

	@Override
	public String updateProductDetail(Long id, ProductDetail updatedProductDetail) {
		ProductDetail p = productDetailRepository.findById(id).orElse(null);
		
		if (p != null) {
			p.setSize(updatedProductDetail.getSize());
			p.setPrice(updatedProductDetail.getPrice());
			p.setDesign(updatedProductDetail.getDesign());
			
			p.setUpdatedAt(LocalDateTime.now());
			
			productDetailRepository.save(p);
			
			return AppConstants.PRODUCT_DETAIL_UPDATED;
		}
		
		return AppConstants.NOT_FOUND;
	}

	@Override
	public String deleteProductDetail(Long id) {
		ProductDetail p = productDetailRepository.findById(id).orElse(null);
		
		if (p != null) {
			productDetailRepository.delete(p);
			
			return AppConstants.PRODUCT_DETAIL_DELETED;
		}
		
		return AppConstants.NOT_FOUND;
	}
	
	@Override
	public Boolean validateProduct(Long id, String token) {
		return productClient.validateProduct(id, token);
	}
}
