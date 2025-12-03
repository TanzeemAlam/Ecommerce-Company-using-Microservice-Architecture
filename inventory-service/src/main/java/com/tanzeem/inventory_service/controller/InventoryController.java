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
	
	@PostMapping
	public ResponseEntity<ApiResponse> addInventoryProduct(@Valid @RequestBody ProductRequestDto dto) {
		String response = inventoryService.addInventoryProduct(convertToEntity(dto));
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getInventoryProduct(@Valid @RequestParam Long id) {
		Inventory product = inventoryService.getInventoryProduct(id);
		
		if (product != null) 
			return ResponseEntity.status(HttpStatus.OK).body(convertToDto(product));
		else 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(AppConstants.NOT_FOUND));
	}
	
	@GetMapping
	public List<InventoryResponseDto> getAllInventoryProducts() {
		return convertToDtoList(inventoryService.getAllInventoryProduct());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateInventoryProduct(@PathVariable Long id, @Valid @RequestBody InventoryDto dto) {
		String response = inventoryService.updateInventoryProduct(id, dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@PostMapping("/reserve")
	public ResponseEntity<ApiResponse> reserveProductFromInventory(@Valid @RequestBody InventoryAdjustmentRequestDto dto) {
		String response = inventoryService.reserveProduct(dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@PostMapping("/release")
	public ResponseEntity<ApiResponse> releaseProductFromInventory(@Valid @RequestBody InventoryAdjustmentRequestDto dto) {
		String response = inventoryService.releaseProduct(dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}

	/**
	 * Mapper methods
	 */
	private Inventory convertToEntity(ProductRequestDto dto) {
		return mapper.map(dto, Inventory.class);
	}
	
	private InventoryResponseDto convertToDto(Inventory product) {
		return mapper.map(product, InventoryResponseDto.class);
	}
	
	private List<InventoryResponseDto> convertToDtoList(List<Inventory> inventoryList) {
		return inventoryList.stream()
				.map(product -> mapper.map(product, InventoryResponseDto.class))
				.collect(Collectors.toList());
	}
}
