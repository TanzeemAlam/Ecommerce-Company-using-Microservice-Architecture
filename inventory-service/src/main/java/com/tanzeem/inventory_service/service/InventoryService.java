package com.tanzeem.inventory_service.service;

import java.util.List;

import com.tanzeem.inventory_service.dto.InventoryDto;
import com.tanzeem.inventory_service.dto.InventoryAdjustmentRequestDto;
import com.tanzeem.inventory_service.entity.Inventory;

public interface InventoryService {

	public String addInventoryProduct(Inventory inventory);
	
	public String addProductStockCount(Long id, InventoryDto inventory);

	public String updateProductStockCount(Long id, InventoryDto updatedProduct);
	
	public Inventory getInventoryProduct(Long id);
	
	public List<Inventory> getAllInventoryProduct();
	
	public String reserveProduct(InventoryAdjustmentRequestDto dto);
	
	public String releaseProduct(InventoryAdjustmentRequestDto dto);
	
	public String confirmProductSale(InventoryAdjustmentRequestDto dto);
}
