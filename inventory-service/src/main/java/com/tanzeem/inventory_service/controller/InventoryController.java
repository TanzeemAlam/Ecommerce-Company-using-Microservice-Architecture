package com.tanzeem.inventory_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.tanzeem.inventory_service.dto.*;
import com.tanzeem.inventory_service.entity.Inventory;
import com.tanzeem.inventory_service.service.InventoryService;
import com.tanzeem.inventory_service.util.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping("/{productId}")
	public ResponseEntity<ApiResponse> addProductStockCount(@Valid @PathVariable Long productId, @Valid @RequestBody InventoryDto dto) {
		String response = inventoryService.addProductStockCount(productId, dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<ApiResponse> updateProductStockCount(@PathVariable Long productId, @Valid @RequestBody InventoryDto dto) {
		String response = inventoryService.updateProductStockCount(productId, dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<?> getInventoryProduct(@Valid @PathVariable Long productId) {
		Inventory product = inventoryService.getInventoryProduct(productId);
		
		if (product != null) 
			return ResponseEntity.status(HttpStatus.OK).body(convertToDto(product));
		else 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(AppConstants.NOT_FOUND));
	}
	
	@GetMapping
	public List<InventoryResponseDto> getAllInventoryProducts() {
		return convertToDtoList(inventoryService.getAllInventoryProduct());
	}
	
	/*
	 * @PostMapping("/reserve") public ResponseEntity<ApiResponse>
	 * reserveProductFromInventory(@Valid @RequestBody InventoryAdjustmentRequestDto
	 * dto) { String response = inventoryService.reserveProduct(dto);
	 * 
	 * return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	 * }
	 * 
	 * @PostMapping("/release") public ResponseEntity<ApiResponse>
	 * releaseProductFromInventory(@Valid @RequestBody InventoryAdjustmentRequestDto
	 * dto) { String response = inventoryService.releaseProduct(dto);
	 * 
	 * return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	 * }
	 */
	
	@PostMapping("/sold")
	public ResponseEntity<ApiResponse> confirmProductSale(@Valid @RequestBody InventoryAdjustmentRequestDto dto) {
		String response = inventoryService.confirmProductSale(dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@GetMapping("/{productId}/quantity/{quantity}/validate")
	public String validateItemFromInventory(@Valid @PathVariable Long productId, @Valid @PathVariable Long quantity) {
		return inventoryService.validateItemFromInventory(productId, quantity);
	}
	
	/**
	 * Mapper methods
	 */
	
	private InventoryResponseDto convertToDto(Inventory product) {
		return mapper.map(product, InventoryResponseDto.class);
	}
	
	private List<InventoryResponseDto> convertToDtoList(List<Inventory> inventoryList) {
		return inventoryList.stream()
				.map(product -> mapper.map(product, InventoryResponseDto.class))
				.collect(Collectors.toList());
	}
}
