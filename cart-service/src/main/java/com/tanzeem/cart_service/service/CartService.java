package com.tanzeem.cart_service.service;

import java.util.List;

import com.tanzeem.cart_service.dto.CartAdjustmentDto;
import com.tanzeem.cart_service.dto.CartItemDto;

import jakarta.validation.Valid;

public interface CartService {

	String addCartItem(@Valid Long userId, @Valid CartAdjustmentDto dto, String token);

	void removeCartItem(@Valid Long userId, @Valid Long itemId);

	String updateCartItem(@Valid Long userId, @Valid Long itemId, @Valid CartAdjustmentDto dto);

	List<CartItemDto> getAllCartItems(@Valid Long userId);

	String clearAllCartItems(@Valid Long userId);

	String getTotalCartAmount(@Valid Long userId);
}
