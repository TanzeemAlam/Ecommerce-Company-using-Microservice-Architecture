package com.tanzeem.inventory_service.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanzeem.inventory_service.dto.*;
import com.tanzeem.inventory_service.entity.Inventory;
import com.tanzeem.inventory_service.repository.InventoryRepository;
import com.tanzeem.inventory_service.service.*;
import com.tanzeem.inventory_service.util.AppConstants;

import jakarta.validation.Valid;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Override
	public String addInventoryProduct(Inventory inventory) { 
		inventoryRepository.save(inventory);
			
		return AppConstants.PRODUCT_ADDED;
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
	public String addProductStockCount(Long id, InventoryDto dto) {
		Inventory product = inventoryRepository.findById(id).orElse(null);
		
		if (product != null) {
			product.setQuantity(product.getQuantity() + dto.getQuantity());
			product.setAvailable(product.getAvailable() + dto.getQuantity());
			
			inventoryRepository.save(product);
			
			return AppConstants.PRODUCT_QUANTITY_UPDATED;
		}
		
		return AppConstants.NOT_FOUND;
	}

	@Override
	public String updateProductStockCount(Long id, InventoryDto updatedProduct) {
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
				
				product.setAvailable(product.getAvailable() - dto.getQuantity());					//Removing requested quantity from total available product
				
				inventoryRepository.save(product);
				
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
			
			inventoryRepository.save(product);
			
			return AppConstants.PRODUCT_RELEASED;
		}
		
		return AppConstants.NOT_FOUND;
	}

	@Override
	public String confirmProductSale(InventoryAdjustmentRequestDto dto) {
		Inventory product = inventoryRepository.findById(dto.getProductId()).orElse(null);
		
		if (product != null) {
			if (product.getReserved() >= dto.getQuantity()) {
				product.setReserved(product.getReserved() - dto.getQuantity());						//Removing quantity from reserved product
				product.setAvailable(product.getAvailable() - dto.getQuantity());					//Removing quantity from available product
				product.setQuantity(product.getQuantity() - dto.getQuantity());
				
				inventoryRepository.save(product);
				
				return AppConstants.PRODUCT_SALE_CONFIRMED;
			}
			
			return AppConstants.PRODUCT_RESERVED_QUANTITY_LOW;
		}
		
		return AppConstants.NOT_FOUND;
	}

	@Override
	public String validateItemFromInventory(@Valid Long productId, @Valid Long quantity) {
		Inventory product = inventoryRepository.findById(productId).orElse(null);
		
		if (product != null) {
			if (product.getAvailable() >= quantity)	return AppConstants.VALID_INVENTORY_ITEM;
			
			return AppConstants.PRODUCT_QUANTITY_LOW;
		}
		
		return AppConstants.INVALID_PRODUCT;
	}
}
