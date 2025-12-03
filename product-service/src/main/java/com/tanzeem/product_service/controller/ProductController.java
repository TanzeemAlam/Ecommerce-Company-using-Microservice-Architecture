package com.tanzeem.product_service.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.tanzeem.product_service.dto.*;
import com.tanzeem.product_service.entity.Product;
import com.tanzeem.product_service.service.ProductService;
import com.tanzeem.product_service.util.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${custom.message}")
	private String customMessage;
	
	@GetMapping("/config")
	public String getConfig() {
		return "App Name from Product Service(using public config): " + customMessage;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody ProductRequestDto dto, @RequestHeader("Authorization") String authHeader) {
		productService.addProduct(convertToEntity(dto), authHeader);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(AppConstants.PRODUCT_CREATED));
	}
	
	@GetMapping
	public PageImpl<ProductResponseDto> getAllProduct(@RequestHeader("Authorization") String authHeader, 
													@RequestParam(defaultValue = "0") int page, 
													@RequestParam(defaultValue = "3") int size) {
		List<ProductResponseDto> productsList = productService.getAllProducts(authHeader);
		
		Pageable pageable = PageRequest.of(page, size);
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productsList.size());
        List<ProductResponseDto> pagedList = productsList.subList(start, end);

		return new PageImpl<ProductResponseDto>(pagedList, pageable, pagedList.size());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getProduct(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
		ProductResponseDto responseDto = productService.getProduct(id, authHeader);
		
		if (responseDto != null) 
			return ResponseEntity.status(HttpStatus.OK).body(responseDto);
		else 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(AppConstants.NOT_FOUND));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody Product p) {
		String response = productService.updateProduct(id, p);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
		String response = productService.deleteProduct(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@GetMapping("/{id}/exists")
	public ResponseEntity<Boolean> checkCustomerExists(@PathVariable Long id) {
		return ResponseEntity.ok(productService.checkCustomerExists(id));
	}
	
	/**
	 * Model Mapper
	 * ProductRequest DTO create/update request to Product Entity
	 */
	private Product convertToEntity(@Valid ProductRequestDto dto) {
		return modelMapper.map(dto, Product.class);
	}
}
