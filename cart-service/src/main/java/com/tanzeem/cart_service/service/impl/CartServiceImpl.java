package com.tanzeem.cart_service.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanzeem.cart_service.dto.*;
import com.tanzeem.cart_service.entity.CartItem;
import com.tanzeem.cart_service.entity.UserCart;
import com.tanzeem.cart_service.enums.CartStatusEnum;
import com.tanzeem.cart_service.repository.CartItemRepository;
import com.tanzeem.cart_service.repository.CartRepository;
import com.tanzeem.cart_service.service.CartService;
import com.tanzeem.cart_service.service.InventoryClient;
import com.tanzeem.cart_service.service.ProductDetailClient;
import com.tanzeem.cart_service.utility.AppConstant;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private InventoryClient inventoryClient;
	
	@Autowired
	private ProductDetailClient productDetailClient;
	
	private UserCart createCart(Long userId) {
		UserCart cart = cartRepository.findByUserId(userId);
		
		if (cart == null) {																	//Create new cart if not present
			cart = new UserCart();
			
			cart.setUserId(userId);
			cart.setTotalAmount(new BigDecimal(0));
			cart.setStatus(CartStatusEnum.ACTIVE.toString());
			
			cartRepository.save(cart);
			
			return cart;
		} 
		else if (cart.getStatus() == CartStatusEnum.ABANDONED.toString()) {					//If cart is abandoned, clear it and set it as ACTIVE
			clearAllCartItems(userId);
			
			cart.setStatus(CartStatusEnum.ACTIVE.toString());
			
			cartRepository.save(cart);
		}
		
		return cart;
	}
	
	@Override
	public String addCartItem(Long userId, CartAdjustmentDto dto, String token) {
		//Create cart for user if doesn't exists
		UserCart cart = createCart(userId);
		
		CartItem cartItem = cartItemRepository.findByCartId(cart.getCartId());
		
		if (cartItem != null)	return AppConstant.ITEM_ALREADY_ADDED;
		
		//Hit Inventory Service to check if item exists with requested quantity
		String inventoryResponse = inventoryClient.validateItemFromInventory(dto, token);
			
		if (inventoryResponse.equalsIgnoreCase(AppConstant.VALID_INVENTORY_ITEM)) {
				
			Double pricePerUnit = productDetailClient.getProductPriceById(dto.getProductId(), token);
						
			cartItem.setCartId(cart.getCartId());
			cartItem.setProductId(dto.getProductId());
			cartItem.setQuantity(dto.getQuantity());
			cartItem.setPricePerUnit(pricePerUnit);
		}
			
		return inventoryResponse;		
	}

	@Override
	public void removeCartItem(Long userId, Long itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String updateCartItem(Long userId, Long itemId, CartAdjustmentDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CartItemDto> getAllCartItems(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String clearAllCartItems(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTotalCartAmount(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
}
