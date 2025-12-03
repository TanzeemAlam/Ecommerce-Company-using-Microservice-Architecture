package com.tanzeem.inventory_service.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanzeem.inventory_service.dto.*;
import com.tanzeem.inventory_service.entity.Inventory;
import com.tanzeem.inventory_service.repository.InventoryRepository;
import com.tanzeem.inventory_service.service.*;
import com.tanzeem.inventory_service.util.AppConstants;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private ProductClient productClient;
	
	@Override
	public String addInventoryProduct(Inventory inventory) { 
		if (productClient.validateProduct(inventory.getProductId())) {
			inventoryRepository.save(inventory);
			
			return AppConstants.PRODUCT_ADDED;
		}
		
		return AppConstants.INVALID_PRODUCT;	
	}

	@Override
	public Inventory getInventoryProduct(Long id) {
		return inventoryRepository.findById(id).orElse(null);
	}

	@Override
	public List<Inventory> getAllInventoryProduct() {
		return inventoryRepository.findAll();			
	}

	@Override
	public String updateInventoryProduct(Long id, InventoryDto updatedProduct) {
		Inventory product = inventoryRepository.findById(id).orElse(null);
		
		if (product != null) {
			product.setQuantity(updatedProduct.getQuantity());
			
			inventoryRepository.save(product);
			
			return AppConstants.PRODUCT_QUANTITY_UPDATED;
		}
		
		return AppConstants.NOT_FOUND;
	}

	@Override
	public String reserveProduct(InventoryAdjustmentRequestDto dto) {
		Inventory product = inventoryRepository.findById(dto.getProductId()).orElse(null);
		
		if (product != null) {
			
			if (product.getAvailable() >= dto.getQuantity()) {
				product.setReserved(product.getReserved() + dto.getQuantity());					//Adding requested quantity into reserved product
				
				product.setAvailable(product.getAvailable() - dto.getQuantity());				//Removing requested quantity from available product
				
				return AppConstants.PRODUCT_RESERVED;
			}
			
			return AppConstants.PRODUCT_QUANTITY_LOW;
		}
		
		return AppConstants.NOT_FOUND;
	}

	@Override
	public String releaseProduct(InventoryAdjustmentRequestDto dto) {
		Inventory product = inventoryRepository.findById(dto.getProductId()).orElse(null);
		
		if (product != null) {
			product.setReserved(product.getReserved() - dto.getQuantity());						//Removing quantity from reserved product
			
			product.setAvailable(product.getAvailable() + dto.getQuantity());					//Adding quantity into available product
			
			return AppConstants.PRODUCT_RELEASED;
		}
		
		return AppConstants.NOT_FOUND;
	}
}
