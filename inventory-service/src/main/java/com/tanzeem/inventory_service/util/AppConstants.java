package com.tanzeem.inventory_service.util;

public class AppConstants {
	public static final String PRODUCT_ADDED 						= "Product added to Inventory";
	public static final String PRODUCT_QUANTITY_UPDATED				= "Product quantity updated in Inventory";
	public static final String PRODUCT_DELETED						= "Product deleted from Inventory";
	
	public static final String PRODUCT_RESERVED						= "Requested quantity is reserved";
	public static final String PRODUCT_RELEASED						= "Requested quantity is released";
	public static final String PRODUCT_QUANTITY_LOW					= "Requested quantity cannot be reserved : Available quantity is low";
	public static final String PRODUCT_RESERVED_QUANTITY_LOW		= "Requested quantity greater than reserved : Reserved quantity is low";
	public static final String PRODUCT_SALE_CONFIRMED				= "Reserved product quantity is sold: Sale confirmed";
	
	public static final String NOT_FOUND							= "Product Not Found in Inventory";
	public static final String INVALID_PRODUCT						= "Invalid Product: Product not present in Inventory";
	public static final String VALID_INVENTORY_ITEM 				= "Item Valid";
}
