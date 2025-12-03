package com.tanzeem.product_detail_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.tanzeem.product_detail_service.dto.*;
import com.tanzeem.product_detail_service.entity.ProductDetail;
import com.tanzeem.product_detail_service.service.ProductClient;
import com.tanzeem.product_detail_service.service.ProductDetailService;
import com.tanzeem.product_detail_service.util.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product-details")
public class ProductDetailController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ProductDetailService productDetailService;
	
	@Value("${custom.message}")
	private String customMessage;
	
	@GetMapping("/config")
	public String getConfig() {
		return "App Name from Product Detail Service : " + customMessage;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse> addProductDetail(@Valid @RequestBody ProductDetailRequestDto dto, @RequestHeader("Authorization") String authHeader) {
		if (productDetailService.validateProduct(dto.getProductId(), authHeader)) {
			productDetailService.addProduct(convertToEntity(dto));
			
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(AppConstants.PRODUCT_DETAIL_CREATED));
		}
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(AppConstants.PRODUCT_DOEST_EXISTS));
	}
	
	@GetMapping
	public List<ProductDetailResponseDto> getAllProductDetails() {
		return convertToDtoList(productDetailService.getAllProductDetails());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductDetail(@PathVariable Long id) {
		ProductDetail productDetail = productDetailService.getProductDetail(id);
		
		if (productDetail != null) 
			return ResponseEntity.status(HttpStatus.OK).body(convertToDto(productDetail));
		else 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(AppConstants.NOT_FOUND));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProductDetail(@PathVariable Long id, @Valid @RequestBody ProductDetail productDetail) {
		String response = productDetailService.updateProductDetail(id, productDetail);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteProductDetail(@PathVariable Long id, @Valid @RequestBody ProductDetail productDetail) {
		String response = productDetailService.deleteProductDetail(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	/**
	 * All DTO to Entity and vice versa operations
	 */
	private ProductDetail convertToEntity(@Valid ProductDetailRequestDto dto) {
		return mapper.map(dto, ProductDetail.class);
	}
	
	private ProductDetailResponseDto convertToDto(ProductDetail productDetail) {
		return productDetail != null ? mapper.map(productDetail, ProductDetailResponseDto.class) : null;
	}
	
	private List<ProductDetailResponseDto> convertToDtoList(List<ProductDetail> productDetails) {
		return productDetails.stream()
				.map(productDetail -> mapper.map(productDetail , ProductDetailResponseDto.class))
				.collect(Collectors.toList());
	}
}
